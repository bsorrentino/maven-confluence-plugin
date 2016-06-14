package org.bsc.maven.confluence.plugin;

import org.bsc.maven.reporting.sink.ConfluenceSink;
import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import java.io.IOException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.tools.plugin.DefaultPluginToolsRequest;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.extractor.ExtractionException;
import org.apache.maven.tools.plugin.generator.GeneratorUtils;
import org.apache.maven.tools.plugin.scanner.MojoScanner;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.bsc.maven.reporting.model.Site;
import org.bsc.maven.reporting.renderer.DependenciesRenderer;
import org.bsc.maven.reporting.renderer.GitLogJiraIssuesRenderer;
import org.bsc.maven.reporting.renderer.ProjectSummaryRenderer;
import org.bsc.maven.reporting.renderer.ScmRenderer;
import org.codehaus.plexus.component.repository.ComponentDependency;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

import java.io.StringWriter;
import java.util.*;
import org.apache.maven.report.projectinfo.AbstractProjectInfoRenderer;
import org.bsc.maven.reporting.renderer.ProjectTeamRenderer;

import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.reporting.MavenReportException;
import static org.bsc.maven.confluence.plugin.PluginConfluenceDocGenerator.DEFAULT_PLUGIN_TEMPLATE_WIKI;
import static java.lang.String.format;
/**
 * 
 * Generate Project's documentation in confluence's wiki format and deploy it
 * 
 */
@Mojo( name="deploy", threadSafe = true )
public class ConfluenceDeployMojo extends AbstractConfluenceSiteMojo {

    private static final String PROJECT_DEPENDENCIES_VAR    = "project.dependencies";
    private static final String PROJECT_SCM_MANAGER_VAR     = "project.scmManager";
    private static final String PROJECT_TEAM_VAR            = "project.team";    
    private static final String PROJECT_SUMMARY_VAR         = "project.summary";
    private static final String GITLOG_JIRA_ISSUES_VAR      = "gitlog.jiraIssues";
    private static final String GITLOG_SINCE_TAG_NAME       = "gitlog.sinceTagName";

    public static final String PLUGIN_SUMMARY_VAR           = "plugin.summary";
    public static final String PLUGIN_GOALS_VAR             = "plugin.goals";

    /**
     * Local Repository.
     *
     */
    @Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
    protected ArtifactRepository localRepository;
    /**
     */
    @Component
    protected ArtifactMetadataSource artifactMetadataSource;
    /**
     */
    @Component
    private ArtifactCollector collector;
    /**
     *
     */
    @Component(role=org.apache.maven.artifact.factory.ArtifactFactory.class)
    protected ArtifactFactory factory;
    /**
     * Maven Project Builder.
     *
     */
    @Component
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * 
     */
    @Component
    protected I18N i18n;

    /**
     * 
     */
    //@Parameter(property = "project.reporting.outputDirectory")
    @Parameter( property="project.build.directory/generated-site/confluence",required=true )
    protected java.io.File outputDirectory;

    /**
     * Maven SCM Manager.
     *
     */
    @Component(role=ScmManager.class)
    protected ScmManager scmManager;
    
    /**
     * The directory name to checkout right after the scm url
     *
     */
    @Parameter(defaultValue = "${project.artifactId}", required = true)
    private String checkoutDirectoryName;
    /**
     * The scm anonymous connection url.
     *
     */
    @Parameter(defaultValue = "${project.scm.connection}")
    private String anonymousConnection;
    /**
     * The scm developer connection url.
     *
     */
    @Parameter(defaultValue = "${project.scm.developerConnection}")
    private String developerConnection;
    /**
     * The scm web access url.
     *
     */
    @Parameter(defaultValue = "${project.scm.url}")
    private String webAccessUrl;

    /**
     * Set to true for enabling substitution of ${gitlog.jiraIssues} build-in variable
     *      
     * @since 4.2
     */
    @Parameter(defaultValue = "false")
    private Boolean gitLogJiraIssuesEnable;

    /**
     * Parse git log commits since last occurrence of specified tag name
     *      
     * @since 4.2
     */
    @Parameter(defaultValue = "")
    private String gitLogSinceTagName;

    /**
     * Parse git log commits until first occurrence of specified tag name
     * 
     * @since 4.2
     */
    @Parameter(defaultValue = "")
    private String gitLogUntilTagName;

    /**
     * If specified, plugin will try to calculate and replace actual gitLogSinceTagName value
     * based on current project version ${project.version} and provided rule.
     * Possible values are
     * <ul>
     *     <li>NO_RULE</li>
     *     <li>CURRENT_MAJOR_VERSION</li>
     *     <li>CURRENT_MINOR_VERSION</li>
     *     <li>LATEST_RELEASE_VERSION</li>
     * </ul>
     *      
     * @since 4.2
     */
    @Parameter(defaultValue="NO_RULE")
    private CalculateRuleForSinceTagName gitLogCalculateRuleForSinceTagName;


    /**
     * Specify JIRA projects key to extract issues from gitlog
     * By default it will try extract all strings that match pattern (A-Za-z+)-\d+
     * 
     * @since 4.2
     */
    @Parameter(defaultValue="")
    private List<String> gitLogJiraProjectKeyList;

    /**
     * The pattern to filter out tagName. Can be used for filter only version tags.
     * 
     * @since 4.2
     */
    @Parameter(defaultValue="")
    private String gitLogTagNamesPattern;

    /**
     * Enable grouping by versions tag
     *      
     * @since 4.2
     */
    @Parameter(defaultValue="false")
    private Boolean gitLogGroupByVersions;


    /**
     * 
     */
    public ConfluenceDeployMojo() {
        super();
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Locale locale = Locale.getDefault();
        
        getLog().info(format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));

        loadUserInfoFromSettings();

        Site site = null;
        
        if( isSiteDescriptorValid() ) {
            site = super.createFromModel();
        }
        
        if( site != null ) {
            site.setBasedir(getSiteDescriptor());
            if( site.getHome().getName()!=null ) {
                setTitle( site.getHome().getName() );
            }
            else {
                site.getHome().setName(getTitle());
            }
            
            java.util.List<String> _labels = getLabels();
            if( !_labels.isEmpty() ) {
                site.getLabels().addAll(_labels);
            }
        }
        else {
            site = super.createFromFolder();

        }
        site.print( System.out );

        
        super.initTemplateProperties();
        
 
        
        if ( project.getPackaging().equals( "maven-plugin" ) )
       /////////////////////////////////////////////////////////////////
       // PLUGIN
       /////////////////////////////////////////////////////////////////
        {
            generatePluginReport(site, locale);
        }
        else
       /////////////////////////////////////////////////////////////////
       // PROJECT
       /////////////////////////////////////////////////////////////////
        {
            generateProjectReport(site, locale);
        }
       
    }
       
    protected String createProjectHome( final Site site, final Locale locale) throws MojoExecutionException {
        
        try {
            final java.io.InputStream is = Site.processUri(site.getHome().getUri(), this.getTitle()) ;
            
            final MiniTemplator t = new MiniTemplator.Builder()
                                            .setSkipUndefinedVars(true)
                                            .build( is, getCharset() );

            generateProjectHomeTemplate( t, site, locale );
            
            return t.generateOutput();
        
            
        } catch (Exception e) {
            final String msg = "error loading template";
            getLog().error(msg, e);
            throw new MojoExecutionException(msg, e);
        }
        
    }
    
    protected void generateProjectHomeTemplate( final MiniTemplator t, final Site site, final Locale locale) throws MojoExecutionException {
        if( t == null ) {
            throw new IllegalArgumentException( "templator is null!"); 
        }
        
        super.addStdProperties(t);

        /////////////////////////////////////////////////////////////////
        // SUMMARY
        /////////////////////////////////////////////////////////////////
        {

             final StringWriter w = new StringWriter(10 * 1024);
             final Sink sink = new ConfluenceSink(w);

             final ProjectSummaryRenderer summary = new ProjectSummaryRenderer(sink,
                     project,
                     i18n,
                     locale);

             summary.render();

             try {
                 final String project_summary_var = w.toString();

                 getProperties().put(PROJECT_SUMMARY_VAR,project_summary_var); // to share with children

                 t.setVariable(PROJECT_SUMMARY_VAR, project_summary_var);

             } catch (VariableNotDefinedException e) {
                 getLog().warn(format("variable %s not defined in template", PROJECT_SUMMARY_VAR));
             }

         }
            
        
       /////////////////////////////////////////////////////////////////
       // TEAM
       /////////////////////////////////////////////////////////////////

        {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w);
            
            final AbstractProjectInfoRenderer renderer = 
                    new ProjectTeamRenderer( sink,
                            project.getModel(),
                            i18n,
                            locale,
                            getLog(),
                            false /* showAvatarImages */
                    );
            
            renderer.render();
            
            try {
                final String project_team_var = w.toString();
                
                getProperties().put(PROJECT_TEAM_VAR,project_team_var); // to share with children
                
                t.setVariable(PROJECT_TEAM_VAR, project_team_var);
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(format("variable %s not defined in template", PROJECT_TEAM_VAR));
            }

        }

       /////////////////////////////////////////////////////////////////
       // SCM
       /////////////////////////////////////////////////////////////////

        {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w);
            //final Sink sink = getSink();
            final String scmTag = "";
            
            new ScmRenderer(getLog(),
                    scmManager,
                    sink,
                    project.getModel(),
                    i18n,
                    locale,
                    checkoutDirectoryName,
                    webAccessUrl,
                    anonymousConnection,
                    developerConnection,
                    scmTag).render();

            try {
                final String project_scm_var = w.toString();
                
                getProperties().put(PROJECT_SCM_MANAGER_VAR,project_scm_var); // to share with children
                
                t.setVariable(PROJECT_SCM_MANAGER_VAR, project_scm_var );
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(format("variable %s not defined in template", PROJECT_SCM_MANAGER_VAR));
            }
        }

       /////////////////////////////////////////////////////////////////
       // DEPENDENCIES
       /////////////////////////////////////////////////////////////////

        {
            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w);
            //final Sink sink = getSink();

            new DependenciesRenderer(sink,
                    project,
                    mavenProjectBuilder,
                    localRepository,
                    factory,
                    i18n,
                    locale,
                    resolveProject(),
                    getLog()).render();

            try {
                final String project_dependencies_var = w.toString();
                
                getProperties().put(PROJECT_DEPENDENCIES_VAR,project_dependencies_var); // to share with children

                t.setVariable(PROJECT_DEPENDENCIES_VAR, project_dependencies_var);
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(format("variable %s not defined in template", PROJECT_DEPENDENCIES_VAR));
            }
        }



        /////////////////////////////////////////////////////////////////
        // CHANGELOG JIRA ISSUES
        /////////////////////////////////////////////////////////////////
        if (gitLogJiraIssuesEnable) {

            {

                final StringWriter w = new StringWriter(10 * 1024);
                final Sink sink = new ConfluenceSink(w);
                final String currentVersion = project.getVersion();

                final GitLogJiraIssuesRenderer gitLogJiraIssuesRenderer = new GitLogJiraIssuesRenderer(sink,
                        gitLogSinceTagName,
                        gitLogUntilTagName,
                        gitLogJiraProjectKeyList,
                        currentVersion,
                        gitLogCalculateRuleForSinceTagName,
                        gitLogTagNamesPattern,
                        gitLogGroupByVersions,
                        getLog());
                gitLogJiraIssuesRenderer.render();

                gitLogSinceTagName = gitLogJiraIssuesRenderer.getGitLogSinceTagName();

                try {
                    final String gitlog_jiraissues_var = w.toString();
                    getProperties().put(GITLOG_JIRA_ISSUES_VAR, gitlog_jiraissues_var); // to share with children
                    t.setVariable(GITLOG_JIRA_ISSUES_VAR, gitlog_jiraissues_var);

                } catch (VariableNotDefinedException e) {
                    getLog().info(format("variable %s not defined in template", GITLOG_JIRA_ISSUES_VAR));
                }
            }

            try {
                if (gitLogSinceTagName==null){
                    gitLogSinceTagName="beginning of gitlog";
                }
                getProperties().put(GITLOG_SINCE_TAG_NAME, gitLogSinceTagName); // to share with children
                t.setVariable(GITLOG_SINCE_TAG_NAME, gitLogSinceTagName);
            } catch (VariableNotDefinedException e) {
                getLog().debug(format("variable %s not defined in template", GITLOG_SINCE_TAG_NAME));
            }

        }

    }
    
    private void generateProjectReport( final Site site, final Locale locale ) throws MojoExecutionException
    {
        
        super.confluenceExecute(new ConfluenceTask() {

            @Override
            public void execute(Confluence confluence) throws Exception {
                //
                // Issue 32
                //
                final String title = getTitle();
        
                if (!isSnapshot() && isRemoveSnapshots()) {
                   final String snapshot = title.concat("-SNAPSHOT");
                   getLog().info(format("removing page [%s]!", snapshot));
                   boolean deleted = ConfluenceUtils.removePage(confluence, getSpaceKey(), getParentPageTitle(), snapshot);

                   if (deleted) {
                       getLog().info(format("Page [%s] has been removed!", snapshot));
                   }
               }

                final String titlePrefix = title;
                
                final String wiki = createProjectHome(site, locale);

                Page confluenceHomePage = ConfluenceUtils.getOrCreatePage(confluence, getSpaceKey(), getParentPageTitle(), title);

                confluenceHomePage.setContent(wiki);

                confluenceHomePage = confluence.storePage(confluenceHomePage);

                for( String label : site.getHome().getComputedLabels() ) {

                    confluence.addLabelByName(label, Long.parseLong(confluenceHomePage.getId()) );
                }
                
                generateChildren( confluence, site.getHome(), confluenceHomePage, getSpaceKey(), title, titlePrefix);
            }

        });

         
    }

   /**
     * 
     * @return
     */
    private ReportingResolutionListener resolveProject() {
        Map managedVersions = null;
        try {
            managedVersions = createManagedVersionMap(project.getId(), project.getDependencyManagement());
        } catch (ProjectBuildingException e) {
            getLog().error("An error occurred while resolving project dependencies.", e);
        }

        ReportingResolutionListener listener = new ReportingResolutionListener();

        try {
            collector.collect(project.getDependencyArtifacts(), project.getArtifact(), managedVersions,
                    localRepository, project.getRemoteArtifactRepositories(), artifactMetadataSource, null,
                    Collections.singletonList(listener));
        } catch (ArtifactResolutionException e) {
            getLog().error("An error occurred while resolving project dependencies.", e);
        }

        return listener;
    }

    /**
     * 
     * @param projectId
     * @param dependencyManagement
     * @return
     * @throws ProjectBuildingException
     */
    private Map createManagedVersionMap(String projectId, DependencyManagement dependencyManagement) throws ProjectBuildingException {
        Map map;
        if (dependencyManagement != null && dependencyManagement.getDependencies() != null) {
            map = new HashMap();
            for (Dependency d : dependencyManagement.getDependencies()) {
                try {
                    VersionRange versionRange = VersionRange.createFromVersionSpec(d.getVersion());
                    Artifact artifact = factory.createDependencyArtifact(d.getGroupId(), d.getArtifactId(),
                            versionRange, d.getType(), d.getClassifier(),
                            d.getScope());
                    map.put(d.getManagementKey(), artifact);
                } catch (InvalidVersionSpecificationException e) {
                    throw new ProjectBuildingException(projectId, "Unable to parse version '" + d.getVersion()
                            + "' for dependency '" + d.getManagementKey() + "': " + e.getMessage(), e);
                }
            }
        } else {
            map = Collections.EMPTY_MAP;
        }
        return map;
    }

    public String getDescription(Locale locale) {
        return "confluence";
    }

    public String getOutputName() {
        return "confluence";
    }

    public String getName(Locale locale) {
        return "confluence";
    }
    
    
    
    
/////////////////////////////////////////////////////////
///    
/// PLUGIN SECTION
///    
/////////////////////////////////////////////////////////
     //@Parameter( defaultValue="${project.build.directory}/generated-site/confluence",required=true )
     //private String outputDirectory;

     @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
     private ArtifactRepository local;    
     
     /**
      * The set of dependencies for the current project
      *
      * @since 3.0
      */
     @Parameter( defaultValue = "${project.artifacts}", required = true, readonly = true )
     private Set<Artifact> dependencies;
 
     /**
      * List of Remote Repositories used by the resolver
      *
      * @since 3.0
      */
     @Parameter( defaultValue = "${project.remoteArtifactRepositories}", required = true, readonly = true )
     private List<ArtifactRepository> remoteRepos;
    
     /**
     * Mojo scanner tools.
     *
     */
    //@MojoComponent
    @Component
    protected MojoScanner mojoScanner;
    
    
     private static List<ComponentDependency>  toComponentDependencies(List<Dependency>   dependencies)
     {
         //return PluginUtils.toComponentDependencies( dependencies )
         return GeneratorUtils.toComponentDependencies(dependencies);
     }
     
    private void generatePluginReport( final Site site, final Locale locale )  throws MojoExecutionException
    {
        
        final String goalPrefix = PluginDescriptor.getGoalPrefixFromArtifactId(project.getArtifactId());
        final PluginDescriptor pluginDescriptor = new PluginDescriptor();
        pluginDescriptor.setGroupId(project.getGroupId());
        pluginDescriptor.setArtifactId(project.getArtifactId());
        pluginDescriptor.setVersion(project.getVersion());
        pluginDescriptor.setGoalPrefix(goalPrefix);

        try {
            final java.util.List deps = new java.util.ArrayList();

            deps.addAll(toComponentDependencies(project.getRuntimeDependencies()));
            deps.addAll(toComponentDependencies(project.getCompileDependencies()));

            pluginDescriptor.setDependencies(deps);
            pluginDescriptor.setDescription(project.getDescription());

            PluginToolsRequest request = new DefaultPluginToolsRequest(project, pluginDescriptor);
            request.setEncoding(getEncoding());
            request.setLocal(local);
            request.setRemoteRepos(remoteRepos);
            request.setSkipErrorNoDescriptorsFound(false);
            request.setDependencies(dependencies);


            try {

                mojoScanner.populatePluginDescriptor(request);

            } catch (InvalidPluginDescriptorException e) {
                // this is OK, it happens to lifecycle plugins. Allow generation to proceed.
                getLog().warn(format("Plugin without mojos. %s\nMojoScanner:%s", e.getMessage(), mojoScanner.getClass()));

            }

            // Generate the plugin's documentation
            super.confluenceExecute(new ConfluenceTask() {
                
                @Override
                public void execute(Confluence confluence) throws Exception {

                    outputDirectory.mkdirs();

                    getLog().info( format("speceKey=%s parentPageTitle=%s", getSpaceKey(), getParentPageTitle()) );

                    Page confluencePage = confluence.getPage(getSpaceKey(), getParentPageTitle());

                    final PluginGenerator generator = new PluginGenerator();
                                        
                    final PluginToolsRequest request = 
                            new DefaultPluginToolsRequest(project, pluginDescriptor);

                    generator.processMojoDescriptors( 
                            request.getPluginDescriptor(),
                            confluence,
                            confluencePage,
                            site,
                            locale );

                    for( String label : site.getHome().getComputedLabels() ) {

                        confluence.addLabelByName(label, Long.parseLong(confluencePage.getId()) );
                    }

                    // Issue 32
                    final String title = getTitle();

                    generateChildren(   confluence, 
                                        site.getHome(), 
                                        confluencePage, 
                                        getSpaceKey(), 
                                        title, 
                                        title);


                }
            });

            //
            // Write the overview
            // PluginOverviewRenderer r = new PluginOverviewRenderer( getSink(), pluginDescriptor, locale );
            // r.render();
            //
        } catch (ExtractionException e) {
            throw new MojoExecutionException(
                    format("Error extracting plugin descriptor: %s",
                    e.getLocalizedMessage()),
                    e);
        }
    }
    
    class PluginGenerator extends PluginConfluenceDocGenerator {
        
   /**
     *
     * @param mojoDescriptor
     * @param destinationDirectory
     * @throws IOException
     */
    public void processMojoDescriptors(  final PluginDescriptor pluginDescriptor,
                                            final Confluence confluence,
                                            final Page parentPage,
                                            final Site site, 
                                            final Locale locale) throws Exception 
    {
        final List<MojoDescriptor> mojos = pluginDescriptor.getMojos();

        if (mojos == null) {
            getLog().warn("no mojos found [pluginDescriptor.getMojos()]");
            return;
        }

        // issue#102
        //final String title = format( "%s-%s", pluginDescriptor.getArtifactId(), pluginDescriptor.getVersion() );
        final String title = getTitle();
        
        getProperties().put("pageTitle",    title);
        getProperties().put("artifactId",   getProject().getArtifactId());
        getProperties().put("version",      getProject().getVersion());

        MiniTemplator t = null;

        if (templateWiki == null || !templateWiki.exists()) {

            getLog().warn("template not set! default using ...");

            java.net.URL sourceUrl = getClass().getClassLoader().getResource(DEFAULT_PLUGIN_TEMPLATE_WIKI);

            if (sourceUrl == null) {
                final String msg = "default template cannot be found";
                getLog().error(msg);
                throw new MavenReportException(msg);
            }

            try {

                t = new MiniTemplator.Builder()
                        .setSkipUndefinedVars(true)
                        .build(sourceUrl, getCharset());

            } catch (Exception e) {
                final String msg = "error loading template";
                getLog().error(msg, e);
                throw new MavenReportException(msg, e);
            }
        } else {
            try {
                t = new MiniTemplator.Builder()
                        .setSkipUndefinedVars(true)
                        .build(templateWiki.toURI().toURL(), getCharset());

                //t = new MiniTemplator(templateWiki);
            } catch (Exception e) {
                final String msg = "error loading template";
                getLog().error(msg, e);
                throw new MavenReportException(msg, e);
            }

        }

        Page page = ConfluenceUtils.getOrCreatePage(confluence, parentPage, title);

        if (!isSnapshot() && isRemoveSnapshots()) {
            final String snapshot = title.concat("-SNAPSHOT");
            getLog().info(format("removing page [%s]!", snapshot));
            boolean deleted = ConfluenceUtils.removePage(confluence, parentPage, snapshot);

            if (deleted) {
                getLog().info(format("Page [%s] has been removed!", snapshot));
            }
        }

        /////////////////////////////////////////////////////////////////
        // SUMMARY
        /////////////////////////////////////////////////////////////////
        
        {
            final StringWriter writer = new StringWriter(100 * 1024);

            writeSummary(writer, pluginDescriptor, mojos);

            writer.flush();

            try {
                final String summary = writer.toString();
                
                t.setVariable(PLUGIN_SUMMARY_VAR, summary);
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(format("variable %s or %s not defined in template", PLUGIN_SUMMARY_VAR, PROJECT_SUMMARY_VAR));
            }

        }

        generateProjectHomeTemplate(t, site, locale);
        
        /////////////////////////////////////////////////////////////////
        // GOALS
        /////////////////////////////////////////////////////////////////
        
        final java.util.List<Goal> goals;
        {
            StringWriter writer = new StringWriter(100 * 1024);

            //writeGoals(writer, mojos);
            goals = writeGoalsAsChildren(writer, page, title, mojos);

            writer.flush();

            try {
                t.setVariable(PLUGIN_GOALS_VAR, writer.toString());
            } catch (VariableNotDefinedException e) {
                getLog().warn(String.format("variable %s not defined in template", "plugin.goals"));
            }

        }
        
        /* 
        // issue#102
        final StringBuilder wiki = new StringBuilder()
                .append(ConfluenceUtils.getBannerWiki()) 
                .append(t.generateOutput());
        page.setContent(wiki.toString());
        */
        page.setContent(t.generateOutput());

        page = confluence.storePage(page);

        // GENERATE GOAL
        for( Goal goal : goals ) {
            try {
                goal.generatePage(confluence, page, title);
            }
            catch( Exception ex ) {
                getLog().warn( format("error generatig page for goal [%s]", goal.descriptor.getGoal()), ex);
            }
        }
    }
        
    }
}
