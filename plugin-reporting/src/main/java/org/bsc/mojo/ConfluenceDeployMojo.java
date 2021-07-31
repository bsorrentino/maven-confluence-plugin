package org.bsc.mojo;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.report.projectinfo.AbstractProjectInfoRenderer;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.tools.plugin.DefaultPluginToolsRequest;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.generator.GeneratorUtils;
import org.apache.maven.tools.plugin.scanner.MojoScanner;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.confluence.ConfluenceService.Storage.Representation;
import org.bsc.confluence.DeployStateManager;
import org.bsc.confluence.ParentChildTuple;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownProcessor;
import org.bsc.mojo.configuration.MarkdownProcessorInfo;
import org.bsc.reporting.plugin.PluginConfluenceDocGenerator;
import org.bsc.reporting.renderer.*;
import org.bsc.reporting.sink.ConfluenceSink;
import org.codehaus.plexus.component.repository.ComponentDependency;
import org.codehaus.plexus.i18n.I18N;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.confluence.model.SitePrinter.print;
import static org.bsc.confluence.model.SiteProcessor.processPageUri;

/**
 *
 * Generate Project's documentation in confluence wiki format and deploy it
 *
 */
@Mojo( name="deploy", threadSafe = true )
public class ConfluenceDeployMojo extends AbstractConfluenceDeployMojo {

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
     *
     */
    @Component
    protected ArtifactMetadataSource artifactMetadataSource;
    /**
     *
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
     * Overrides system locale used for content generation
     *
     * @since 6.6
     */
    @Parameter
    private String locale;
    /**
     * Markdown processor Info<br>
     * <pre>
     *   &lt;markdownProcessor>
     *     &lt;name>processor name&lt;/name> &lt;git branch!-- default: pegdown -->
     *   &lt;/markdownProcessor>
     * </pre>
     *
     * @since 6.8
     */
    @Parameter( alias="markdownProcessor" )
    private MarkdownProcessorInfo markdownProcessorInfo = new MarkdownProcessorInfo();

    /**
     *
     * @return site
     */
    private Site loadSite() {
        Site site = super.createSiteFromModel(getSiteModelVariables());

        if( site != null ) {

            site.setBasedir(getSiteDescriptor().toPath());

            if( site.getHome().getName()!=null ) {
                setPageTitle( site.getHome().getName() );
            }
            else {
                site.getHome().setName(getPageTitle());
            }

            java.util.List<String> _labels = getLabels();
            if( !_labels.isEmpty() ) {
                site.getLabels().addAll(_labels);
            }
        }
        else {
            site = super.createSiteFromFolder();

            try {
                final Path p = templateWiki.toPath();
                site.setBasedir(p);
            }
            catch( Exception e ) {
                site.setBasedir(getSiteDescriptor().toPath());
            }

        }

        print( site, System.out );

        return site;
    }

    /**
     *
     * @param confluence
     * @throws Exception
     */
    @Override
    public void execute( ConfluenceService confluence ) throws Exception {

        getLog().info(format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));

        // Init markdown Processor
        MarkdownProcessor.shared.init();

        final Site site = loadSite();

        site.setDefaultFileExt(getFileExt());

        initTemplateProperties( site );

        final Locale parsedLocale = ofNullable(locale)
                                        .filter( l -> !l.isEmpty())
                                        .map(Locale::new)
                                        .orElseGet(Locale::getDefault);

        if ( project.getPackaging().equals( "maven-plugin" ) )
       /////////////////////////////////////////////////////////////////
       // PLUGIN
       /////////////////////////////////////////////////////////////////
        {
            generatePluginReport(confluence, site, parsedLocale);
        }
        else
       /////////////////////////////////////////////////////////////////
       // PROJECT
       /////////////////////////////////////////////////////////////////
        {
            generateProjectReport(confluence, site, parsedLocale);
        }

        getDeployStateManager().ifPresent( DeployStateManager::save );

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
                 getLog().debug(format("variable %s not defined in template", PROJECT_SUMMARY_VAR));
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
                getLog().debug(format("variable %s not defined in template", PROJECT_TEAM_VAR));
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
                getLog().debug(format("variable %s not defined in template", PROJECT_SCM_MANAGER_VAR));
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
                getLog().debug(format("variable %s not defined in template", PROJECT_DEPENDENCIES_VAR));
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
                    getLog().debug(format("variable %s not defined in template", GITLOG_JIRA_ISSUES_VAR));
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

    private CompletableFuture<Boolean> removeSnaphot(
            final ConfluenceService confluence,
            final Model.Page parentPage,
            final String title )
    {

        if (!isSnapshot() && isRemoveSnapshots()) {
            final String snapshot = title.concat("-SNAPSHOT");
            getLog().info(format("removing page [%s]!", snapshot));

            return confluence.removePage( parentPage, snapshot)
                .thenApply( deleted -> {
                    if (deleted) {
                        getLog().info(format("Page [%s] has been removed!", snapshot));
                    }
                    return deleted;
                })
                .exceptionally( ex ->
                    throwRTE(format("Page [%s] cannot be removed!", snapshot), ex))
                ;
        }
        else {
            return completedFuture(false);
        }

    }

    private CompletableFuture<Storage> getHomeContent(
                final Site site,
                final Optional<Model.Page> homePage,
                final Locale locale )
    {

        final Site.Page home = site.getHome();
        final java.net.URI uri = home.getUri();
        final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed())
                    ? ofNullable(this.getPageTitle())
                    : Optional.empty();

        return processPageUri( site, home, homePage, uri, pagePrefixToApply)
                .thenCompose( content -> {

                    try {
                        final MiniTemplator t = MiniTemplator.builder()
                                .setSkipUndefinedVars(true)
                                .setCharset(getCharset() )
                                .build( content.getInputStream(getCharset()) );

                        generateProjectHomeTemplate( t, site, locale );

                        return completedFuture( Storage.of(t.generateOutput(),content.getType()) );

                    } catch (Exception ex) {
                        CompletableFuture<Storage> result = new CompletableFuture<>();
                        result.completeExceptionally(ex);
                        return result;
                    }

                });


    }

    /**
     *
     * @param confluence
     * @param site
     * @param locale
     */
    private void generateProjectReport(
            final ConfluenceService confluence,
            final Site site,
            final Locale locale ) 
    {
        //
        // Issue 32
        //
        final String _homePageTitle = getPageTitle();

        final AsyncProcessPageFunc updateHomePage = p ->
            updatePageIfNeeded(site.getHome(), p, () ->
                    getHomeContent(site, Optional.of(p), locale)
                            .thenCompose( content -> confluence.storePage(p, content )));

        final AsyncProcessPageFunc createHomePage = _parentPage ->
                    getHomeContent(  site, Optional.empty(), locale )
                        .thenCompose( content -> confluence.createPage(_parentPage, _homePageTitle,content) );


        loadParentPage(confluence, Optional.of(site))
            .thenCompose( _parentPage ->
                removeSnaphot(confluence, _parentPage, _homePageTitle)
                    .thenCompose( deleted -> confluence.getPage(_parentPage.getSpace(), _homePageTitle))
                    .thenCompose( page -> {
                        return ( page.isPresent() ) ?
                                updateHomePage.apply(page.get()) :
                                createHomePage.apply(_parentPage);
                    })
                    .thenCompose( page -> confluence.addLabelsByName(page.getId(), site.getHome().getComputedLabels() ).thenApply( v -> page ))
            )
            .thenAccept( confluenceHomePage ->
                    generateChildren(
                            confluence,
                            site,
                            site.getHome(),
                            confluenceHomePage,
                            new HashMap<>())
            ).join();

    }

   /**
     *
     * @return
     */
    private ReportingResolutionListener resolveProject() {
        Map<String,Artifact> managedVersions = null;
        try {
            managedVersions = createManagedVersionMap(project.getId(), project.getDependencyManagement());
        } catch (ProjectBuildingException e) {
            getLog().error("An error occurred while resolving project dependencies.", e);
        }

        ReportingResolutionListener listener = new ReportingResolutionListener();

        try {
            collector.collect(  project.getDependencyArtifacts(),
                                project.getArtifact(),
                                managedVersions,
                                localRepository,
                                project.getRemoteArtifactRepositories(),
                                artifactMetadataSource, null,
                                Collections.singletonList(listener));
        } catch (Exception e) {
            getLog().warn("An error occurred while resolving project dependencies.", e);
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
    private Map<String,Artifact> createManagedVersionMap(String projectId, DependencyManagement dependencyManagement) throws ProjectBuildingException {
        Map<String,Artifact> map;
        if (dependencyManagement != null && dependencyManagement.getDependencies() != null) {
            map = new HashMap<>();
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
            map = Collections.emptyMap();
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

    @SuppressWarnings("unchecked")
    private void generatePluginReport( ConfluenceService confluence, final Site site, final Locale locale )  throws Exception
    {

        final String goalPrefix = PluginDescriptor.getGoalPrefixFromArtifactId(project.getArtifactId());
        final PluginDescriptor pluginDescriptor = new PluginDescriptor();
        pluginDescriptor.setGroupId(project.getGroupId());
        pluginDescriptor.setArtifactId(project.getArtifactId());
        pluginDescriptor.setVersion(project.getVersion());
        pluginDescriptor.setGoalPrefix(goalPrefix);

        final java.util.List<ComponentDependency> deps = new java.util.ArrayList<>();

        deps.addAll(toComponentDependencies(project.getRuntimeDependencies()));
        deps.addAll(toComponentDependencies(project.getCompileDependencies()));

        pluginDescriptor.setDependencies(deps);
        pluginDescriptor.setDescription(project.getDescription());

        final PluginToolsRequest req = new DefaultPluginToolsRequest(project, pluginDescriptor);
        req.setEncoding(getEncoding());
        req.setLocal(local);
        req.setRemoteRepos(remoteRepos);
        req.setSkipErrorNoDescriptorsFound(false);
        req.setDependencies(dependencies);

        try {

            mojoScanner.populatePluginDescriptor(req);

        } catch (InvalidPluginDescriptorException e) {
            // this is OK, it happens to lifecycle plugins. Allow generation to proceed.
            getLog().warn(format("Plugin without mojos. %s\nMojoScanner:%s", e.getMessage(), mojoScanner.getClass()));

        }

        final Model.Page parentPage = loadParentPage(confluence, Optional.of(site)).join();

        outputDirectory.mkdirs();

        getLog().info( format("speceKey=%s parentPageTitle=%s", parentPage.getSpace(), parentPage.getTitle()) );

        final PluginGenerator generator = new PluginGenerator();

        final PluginToolsRequest request =
                new DefaultPluginToolsRequest(project, pluginDescriptor);

        final Model.Page confluenceHomePage = generator.processMojoDescriptors(
            request.getPluginDescriptor(),
            confluence,
            parentPage,
            site,
            locale );

        confluence.addLabelsByName(confluenceHomePage.getId(), site.getHome().getComputedLabels() ).join();

        final Map<String, Model.Page> varsToParentPageMap = new HashMap<>();

        generateChildren(   confluence,
                            site,
                            site.getHome(),
                            confluenceHomePage,
                            varsToParentPageMap);

        generator.generateGoalsPages(confluence, confluenceHomePage, varsToParentPageMap);


        //
        // Write the overview
        // PluginOverviewRenderer r = new PluginOverviewRenderer( getSink(), pluginDescriptor, locale );
        // r.render();
        //
    }

    class PluginGenerator extends PluginConfluenceDocGenerator {


        final java.util.List<Goal> goals = new ArrayList<>();

        void generateGoalsPages(final ConfluenceService confluence,
                                final Model.Page confluenceHome,
                                final Map<String, Model.Page> varsToParentPageMap) {

            // GENERATE GOAL
            getLog().info(format("Get the right page to generate the %s pages under", PLUGIN_GOALS_VAR));

            Model.Page goalsParentPage = confluenceHome;

            if (varsToParentPageMap.containsKey(PLUGIN_GOALS_VAR)) {
                goalsParentPage = varsToParentPageMap.get(PLUGIN_GOALS_VAR);
            }

            getLog().info(format("Plugin Goals parentPage is: %s", goalsParentPage.getTitle()));

            for (PluginConfluenceDocGenerator.Goal goal : goals) {
                try {
                    getLog().info(format("- generating: %s", goal.getPageName(confluenceHome.getTitle()) ));
                    goal.generatePage(confluence, goalsParentPage, confluenceHome.getTitle());
                } catch (Exception ex) {
                    getLog().warn(format("error generatig page for goal [%s]", goal.descriptor.getGoal()), ex);
                }
            }

        }

        /**
         *
         * @param site
         * @param homePage
         * @param pluginDescriptor
         * @param locale
         * @return
         */
        private CompletableFuture<Storage> getHomeContent(
            final Site site,
            final Optional<Model.Page> homePage,
            final PluginDescriptor pluginDescriptor,
            final Locale locale)
        {
            final List<MojoDescriptor> mojos = pluginDescriptor.getMojos();

            if (mojos == null) {
                getLog().warn("no mojos found [pluginDescriptor.getMojos()]");
            } else if (getLog().isDebugEnabled()) {
                getLog().debug("Found the following Mojos:");
                for (MojoDescriptor mojo : mojos) {
                    getLog().debug(format("  - %s : %s", mojo.getFullGoalName(), mojo.getDescription()));
                }
            }

            final String title = getPageTitle();
            final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed())
                    ? ofNullable(getPageTitle())
                    : Optional.empty();

            return processPageUri(site, site.getHome(), homePage, site.getHome().getUri(), pagePrefixToApply )
                    .thenCompose( content -> {

                        try {

                            final MiniTemplator t = MiniTemplator.builder()
                                    .setSkipUndefinedVars(true)
                                    .setCharset( getCharset() )
                                    .build( content.getInputStream(getCharset()) );

                            /////////////////////////////////////////////////////////////////
                            // SUMMARY
                            /////////////////////////////////////////////////////////////////

                            {
                                final StringWriter writer = new StringWriter(100 * 1024);

                                writeSummary(writer, pluginDescriptor);

                                writer.flush();

                                try {
                                    final String summary = writer.toString();

                                    getProperties().put( PLUGIN_SUMMARY_VAR, summary );

                                    t.setVariable(PLUGIN_SUMMARY_VAR, summary);

                                } catch (VariableNotDefinedException e) {
                                    getLog().debug(format("variable %s or %s not defined in template", PLUGIN_SUMMARY_VAR, PROJECT_SUMMARY_VAR));
                                }

                            }

                            generateProjectHomeTemplate(t, site, locale);

                            /////////////////////////////////////////////////////////////////
                            // GOALS
                            /////////////////////////////////////////////////////////////////

                            {
                                final StringWriter writer = new StringWriter(100 * 1024);

                                //writeGoals(writer, mojos);
                                goals.addAll(writeGoalsAsChildren(writer, title, mojos));

                                writer.flush();

                                try {
                                    final String plugin_goals = writer.toString();

                                    getProperties().put( PLUGIN_GOALS_VAR, plugin_goals );

                                    t.setVariable(PLUGIN_GOALS_VAR, plugin_goals );

                                } catch (VariableNotDefinedException e) {
                                    getLog().debug(String.format("variable %s not defined in template", "plugin.goals"));
                                }

                            }

                            /*
                            // issue#102
                            final StringBuilder wiki = new StringBuilder()
                            .append(ConfluenceUtils.getBannerWiki())
                            .append(t.generateOutput());
                            page.setContent(wiki.toString());
                            */

                            return completedFuture(Storage.of(t.generateOutput(), Representation.WIKI));

                        } catch (Exception ex) {
                            final CompletableFuture<Storage> result = new CompletableFuture<>();
                            result.completeExceptionally(ex);
                            return result;
                        }
                    }) ;

        }

        private CompletableFuture<Model.Page> updateHomeContent(
                    final ConfluenceService confluence,
                    final Site site,
                    final Model.Page homePage,
                    final PluginDescriptor pluginDescriptor,
                    final Locale locale
                )
        {
            final List<MojoDescriptor> mojos = pluginDescriptor.getMojos();

            if (mojos == null) {
                getLog().warn("no mojos found [pluginDescriptor.getMojos()]");
            } else if (getLog().isDebugEnabled()) {
                getLog().debug("Found the following Mojos:");
                for (MojoDescriptor mojo : mojos) {
                    getLog().debug(format("  - %s : %s", mojo.getFullGoalName(), mojo.getDescription()));
                }
            }

            final String title = getPageTitle();
            final Optional<String> pagePrefixToApply = (isChildrenTitlesPrefixed())
                    ? ofNullable(getPageTitle())
                    : Optional.empty();

            return processPageUri(site, site.getHome(), Optional.of(homePage), site.getHome().getUri(), pagePrefixToApply )
                    .thenCompose( content -> {

                    try {

                        final MiniTemplator t = MiniTemplator.builder()
                                .setSkipUndefinedVars(true)
                                .setCharset(getCharset())
                                .build( content.getInputStream(getCharset())  );

                        /////////////////////////////////////////////////////////////////
                        // SUMMARY
                        /////////////////////////////////////////////////////////////////

                        {
                            final StringWriter writer = new StringWriter(100 * 1024);

                            writeSummary(writer, pluginDescriptor);

                            writer.flush();

                            try {
                                final String summary = writer.toString();

                                getProperties().put( PLUGIN_SUMMARY_VAR, summary );

                                t.setVariable(PLUGIN_SUMMARY_VAR, summary);

                            } catch (VariableNotDefinedException e) {
                                getLog().debug(format("variable %s or %s not defined in template", PLUGIN_SUMMARY_VAR, PROJECT_SUMMARY_VAR));
                            }

                        }

                        generateProjectHomeTemplate(t, site, locale);

                        /////////////////////////////////////////////////////////////////
                        // GOALS
                        /////////////////////////////////////////////////////////////////

                        {
                            final StringWriter writer = new StringWriter(100 * 1024);

                            //writeGoals(writer, mojos);
                            goals.addAll(writeGoalsAsChildren(writer, title, mojos));

                            writer.flush();

                            try {
                                final String plugin_goals = writer.toString();

                                getProperties().put( PLUGIN_GOALS_VAR, plugin_goals );

                                t.setVariable(PLUGIN_GOALS_VAR, plugin_goals );

                            } catch (VariableNotDefinedException e) {
                                getLog().debug(String.format("variable %s not defined in template", "plugin.goals"));
                            }

                        }

                        /*
                        // issue#102
                        final StringBuilder wiki = new StringBuilder()
                        .append(ConfluenceUtils.getBannerWiki())
                        .append(t.generateOutput());
                        page.setContent(wiki.toString());
                        */

                        return confluence.storePage(homePage, Storage.of(t.generateOutput(), Representation.WIKI));

                    } catch (Exception ex) {
                        final CompletableFuture<Model.Page> result = new CompletableFuture<>();
                        result.completeExceptionally(ex);
                        return result;
                    }
                }) ;

        }

        /**
         *
         * @param pluginDescriptor
         * @param confluence
         * @param parentPage
         * @param site
         * @param locale
         * @return
         * @throws Exception
         */
        public Model.Page processMojoDescriptors(
                                final PluginDescriptor pluginDescriptor,
                                final ConfluenceService confluence,
                                final Model.Page parentPage,
                                final Site site,
                                final Locale locale) throws Exception
        {

            // issue#102
            //final String title = format( "%s-%s", pluginDescriptor.getArtifactId(), pluginDescriptor.getVersion() );
            final String title = getPageTitle();

            getProperties().put("pageTitle",    title);
            getProperties().put("artifactId",   getProject().getArtifactId());
            getProperties().put("version",      getProject().getVersion());

            final Function<Model.Page, CompletableFuture<Model.Page>> updatePage = (p) ->
                updatePageIfNeeded( site.getHome(),p,
                        () -> getHomeContent( site, Optional.of(p), pluginDescriptor, locale)
                                        .thenCompose( content -> confluence.storePage(p, content)));

            final Function<Model.Page, CompletableFuture<Model.Page>> createPage = (parent) ->
                        getHomeContent(site, Optional.empty(), pluginDescriptor, locale)
                        .thenCompose( content ->confluence.createPage(parent, title, content));

            return removeSnaphot(confluence, parentPage, title)
                    .thenCompose( deleted -> confluence.getPage(parentPage.getSpace(), parentPage.getTitle()) )
                    .exceptionally( ex ->
                        throwRTE( "cannot find parent page [%s] in space [%s]", parentPage.getTitle(), ex ))
                    .thenApply( parent ->
                        parent.orElseThrow( () -> RTE( "cannot find parent page [%s] in space [%s]", parentPage.getTitle())) )
                    .thenCombine( confluence.getPage(parentPage.getSpace(), title), ParentChildTuple::of)
                    .thenCompose( tuple -> ( tuple.getChild().isPresent() )
                            ? updatePage.apply(tuple.getChild().get())
                            : createPage.apply(tuple.getParent()) )
                    .join();

        }
    }
}
