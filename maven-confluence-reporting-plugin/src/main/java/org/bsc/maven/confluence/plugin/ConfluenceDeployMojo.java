package org.bsc.maven.confluence.plugin;

import org.bsc.maven.reporting.*;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

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
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.scm.manager.ScmManager;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.bsc.maven.reporting.renderer.DependenciesRenderer;
import org.bsc.maven.reporting.renderer.ProjectSummaryRenderer;
import org.bsc.maven.reporting.renderer.ScmRenderer;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import java.util.List;
import java.util.Set;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.settings.Proxy;
import org.apache.maven.tools.plugin.DefaultPluginToolsRequest;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.extractor.ExtractionException;
import org.apache.maven.tools.plugin.generator.Generator;
import org.apache.maven.tools.plugin.generator.GeneratorUtils;
import org.apache.maven.tools.plugin.scanner.MojoScanner;
import org.bsc.maven.reporting.model.Site;
import org.codehaus.plexus.component.repository.ComponentDependency;
import org.codehaus.swizzle.confluence.ConfluenceFactory;

/**
 * 
 * Generate Project's documentation in confluence's wiki format and deploy it
 * 
 */
@Mojo( name="deploy", threadSafe = true )
public class ConfluenceDeployMojo extends AbstractConfluenceSiteMojo {

    private static final String PROJECT_DEPENDENCIES_VAR = "project.dependencies";
    private static final String PROJECT_SCM_MANAGER_VAR = "project.scmManager";
    private static final String PROJECT_SUMMARY_VAR = "project.summary";
    
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
     * 
     */
    public ConfluenceDeployMojo() {
        super();
    }
    
    
    /**
     * 
     * @param confluence
     */
    private void confluenceLogout(Confluence confluence) {

        if (null == confluence) {
            return;
        }

        try {
            if (!confluence.logout()) {
                getLog().warn("confluence logout has failed!");
            }
        } catch (Exception e) {
            getLog().warn("confluence logout has failed due exception ", e);
        }


    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        final Locale locale = Locale.getDefault();
        
        getLog().info(String.format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));

        loadUserInfoFromSettings();

        Site site = null;
        
        if( isSiteDescriptorValid() ) {
            site = super.createFromModel();
        }
        
        if( site == null ) {
            site = super.createFromFolder();
        }
        else {
            site.setBasedir(getSiteDescriptor());
            if( site.getHome().getName()!=null ) {
                setTitle( site.getHome().getName() );
            }

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
    
    
    private void generateProjectReport( Site site, Locale locale ) throws MojoExecutionException
    {
        // Issue 32
        final String title = getTitle();
        //String title = project.getArtifactId() + "-" + project.getVersion();
        
        MiniTemplator t = null;
        try {
            t = new MiniTemplator.Builder()
                    .setSkipUndefinedVars(true)
                    .build( Site.processUri(site.getHome().getUri()) );
            
        } catch (Exception e) {
            final String msg = "error loading template";
            getLog().error(msg, e);
            throw new MojoExecutionException(msg, e);
        }
        
        super.addProperties(t);

       /////////////////////////////////////////////////////////////////
       // SUMMARY
       /////////////////////////////////////////////////////////////////
       {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w);
            //final Sink sink = getSink();

            new ProjectSummaryRenderer(sink,
                    project,
                    i18n,
                    locale).render();

            try {
                final String project_summary_var = w.toString();
                
                getProperties().put(PROJECT_SUMMARY_VAR,project_summary_var); // to share with children
                
                t.setVariable(PROJECT_SUMMARY_VAR, project_summary_var);
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(String.format("variable %s not defined in template", PROJECT_SUMMARY_VAR));
            }

        }

       /////////////////////////////////////////////////////////////////
       // SCM
       /////////////////////////////////////////////////////////////////

        {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w);
            //final Sink sink = getSink();

            new ScmRenderer(scmManager,
                    sink,
                    project.getModel(),
                    i18n,
                    locale,
                    checkoutDirectoryName,
                    webAccessUrl,
                    anonymousConnection,
                    developerConnection).render();

            try {
                final String project_scm_var = w.toString();
                
                getProperties().put(PROJECT_SCM_MANAGER_VAR,project_scm_var); // to share with children
                
                t.setVariable(PROJECT_SCM_MANAGER_VAR, project_scm_var );
                
            } catch (VariableNotDefinedException e) {
                getLog().warn(String.format("variable %s not defined in template", PROJECT_SCM_MANAGER_VAR));
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
                getLog().warn(String.format("variable %s not defined in template", PROJECT_DEPENDENCIES_VAR));
            }
        }

        String wiki = t.generateOutput();

        //
        // write in confluence
        // 
        Confluence confluence = null;

        try {
            //confluence = new Confluence(getEndPoint());

            //confluence.login(getUsername(), getPassword());
            
            Confluence.ProxyInfo proxyInfo = null;

            final Proxy activeProxy = mavenSettings.getActiveProxy();

            if( activeProxy!=null ) {
                
                proxyInfo = 
                        new Confluence.ProxyInfo( 
                                activeProxy.getHost(),
                                activeProxy.getPort(), 
                                activeProxy.getUsername(), 
                                activeProxy.getPassword()
                                );
            }
            
            confluence = ConfluenceFactory.createInstanceDetectingVersion(getEndPoint(), proxyInfo, getUsername(), getPassword());

            getLog().info(ConfluenceUtils.getVersion(confluence));

            if (!isSnapshot() && isRemoveSnapshots()) {
                final String snapshot = title.concat("-SNAPSHOT");
                getLog().info(String.format("removing page [%s]!", snapshot));
                boolean deleted = ConfluenceUtils.removePage(confluence, getSpaceKey(), getParentPageTitle(), snapshot);

                if (deleted) {
                    getLog().info(String.format("Page [%s] has been removed!", snapshot));
                }
            }


            Page confluencePage = ConfluenceUtils.getOrCreatePage(confluence, getSpaceKey(), getParentPageTitle(), title);

            confluencePage.setContent(wiki);

            confluencePage = confluence.storePage(confluencePage);

            for( String label : site.getLabels() ) {
                
                confluence.addLabelByName(label, Long.parseLong(confluencePage.getId()) );
            }
                               
            generateChildren( confluence, site.getHome(), confluencePage, getSpaceKey(), title, title);

        } catch (Exception e) {
            getLog().warn("has been imposssible connect to confluence due exception", e);
        } finally {
            confluenceLogout(confluence);
        }


         
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
            for (Iterator i = dependencyManagement.getDependencies().iterator(); i.hasNext();) {
                Dependency d = (Dependency) i.next();

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
    * The file encoding of the source files.
    *
    */
    @Parameter( property="encoding", defaultValue="${project.build.sourceEncoding}" )
    private String encoding;    
  
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
     
    private void generatePluginReport( Site site, Locale locale )  throws MojoExecutionException
    {
        
        String goalPrefix = PluginDescriptor.getGoalPrefixFromArtifactId( project.getArtifactId() );
        PluginDescriptor pluginDescriptor = new PluginDescriptor();
        pluginDescriptor.setGroupId( project.getGroupId() );
        pluginDescriptor.setArtifactId( project.getArtifactId() );
        pluginDescriptor.setVersion( project.getVersion() );
        pluginDescriptor.setGoalPrefix( goalPrefix );

        try
        {
            java.util.List deps = new java.util.ArrayList();
            
            deps.addAll(toComponentDependencies( project.getRuntimeDependencies() ));
            deps.addAll(toComponentDependencies( project.getCompileDependencies() ));

            pluginDescriptor.setDependencies( deps );
            pluginDescriptor.setDescription( project.getDescription() );

            PluginToolsRequest request = new DefaultPluginToolsRequest( project, pluginDescriptor );
            request.setEncoding( encoding );
            request.setLocal(local);
            request.setRemoteRepos(remoteRepos);
            request.setSkipErrorNoDescriptorsFound(false);
            request.setDependencies( dependencies );

            
            try {
                
                mojoScanner.populatePluginDescriptor(request);
                
            } catch (InvalidPluginDescriptorException e) {
                // this is OK, it happens to lifecycle plugins. Allow generation to proceed.
                getLog().warn( String.format("Plugin without mojos. %s\nMojoScanner:%s", e.getMessage(), mojoScanner.getClass()));

            }
            
            // Generate the plugin's documentation
            generatePluginDocumentation( pluginDescriptor, site );

            // Write the overview
            //PluginOverviewRenderer r = new PluginOverviewRenderer( getSink(), pluginDescriptor, locale );
            //r.render();
        }
        catch ( ExtractionException e )
        {
            throw new MojoExecutionException( 
                    String.format("Error extracting plugin descriptor: %s", 
                                e.getLocalizedMessage()),
                                            e );
        }
    }
    
    private void generatePluginDocumentation( PluginDescriptor pluginDescriptor, Site site )  throws MojoExecutionException
    {
        Confluence confluence = null;
        
        try
        {
    		      
            //Confluence confluence = new Confluence( getEndPoint() );
            //confluence.login(getUsername(), getPassword());
            Confluence.ProxyInfo proxyInfo = null;

            final Proxy activeProxy = mavenSettings.getActiveProxy();

            if( activeProxy!=null ) {
                
                proxyInfo = 
                        new Confluence.ProxyInfo( 
                                activeProxy.getHost(),
                                activeProxy.getPort(), 
                                activeProxy.getUsername(), 
                                activeProxy.getPassword()
                                );
            }
            
            confluence = ConfluenceFactory.createInstanceDetectingVersion(getEndPoint(), proxyInfo, getUsername(), getPassword());

            getLog().info( ConfluenceUtils.getVersion(confluence) );

            outputDirectory.mkdirs();

            getLog().info( "speceKey=" + getSpaceKey() + " parentPageTitle=" + getParentPageTitle());
            
            Page confluencePage = confluence.getPage(getSpaceKey(), getParentPageTitle());
           
            Generator generator =
            		new PluginConfluenceDocGenerator( this, confluence, confluencePage, templateWiki ); /*PluginXdocGenerator()*/;

            PluginToolsRequest request = new DefaultPluginToolsRequest( project, pluginDescriptor );
            		
            generator.execute( outputDirectory, request );

            // Issue 32
            final String title = getTitle();
            //String title = project.getArtifactId() + "-" + project.getVersion();
            
            generateChildren( confluence, site.getHome(), confluencePage, getSpaceKey(), title, title);
            //generateChildren(confluence, getSpaceKey(), title, title);
            
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error writing plugin documentation", e );
        }
        finally {
            confluenceLogout(confluence);
        }

    }
    
}
