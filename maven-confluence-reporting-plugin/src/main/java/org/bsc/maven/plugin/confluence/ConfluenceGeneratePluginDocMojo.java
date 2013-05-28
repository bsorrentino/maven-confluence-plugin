package org.bsc.maven.plugin.confluence;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.doxia.siterenderer.Renderer;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.settings.Proxy;
import org.apache.maven.tools.plugin.DefaultPluginToolsRequest;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.extractor.ExtractionException;
import org.apache.maven.tools.plugin.scanner.MojoScanner;
import org.bsc.maven.reporting.AbstractConfluenceReportMojo;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.tools.plugin.generator.Generator;
import org.apache.maven.tools.plugin.generator.GeneratorUtils;
import org.apache.maven.tools.plugin.scanner.DefaultMojoScanner;
import org.codehaus.plexus.component.repository.ComponentDependency;

/**
 * Generate Plugin's documentation in confluence's wiki format
 * 
 * 
 *
 */
//@MojoExecute(phase="compile")
//@MojoGoal("plugin-confluence-summary")
@Mojo(name="plugin-confluence-summary",threadSafe=true,requiresDependencyResolution= ResolutionScope.COMPILE,defaultPhase= LifecyclePhase.SITE)
public class ConfluenceGeneratePluginDocMojo extends AbstractConfluenceReportMojo {

    /**
     * Report output directory.
     *
     */
    //@MojoParameter(expression="${project.build.directory}/generated-site/confluence",required=true)
    @Parameter( defaultValue="${project.build.directory}/generated-site/confluence",required=true )
    private String outputDirectory;


    /**
      * Location of the local repository.
      *
      * @since 3.0
      */
     @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
     protected ArtifactRepository local;    
     
     /**
      * The set of dependencies for the current project
      *
      * @since 3.0
      */
     @Parameter( defaultValue = "${project.artifacts}", required = true, readonly = true )
     protected Set<Artifact> dependencies;
 
     /**
      * List of Remote Repositories used by the resolver
      *
      * @since 3.0
      */
     @Parameter( defaultValue = "${project.remoteArtifactRepositories}", required = true, readonly = true )
     protected List<ArtifactRepository> remoteRepos;
     
     /**
     * Mojo scanner tools.
     *
     */
    //@MojoComponent
    @Component
    protected MojoScanner mojoScanner;


    /**
    * The file encoding of the source files.
    *
    */
    //@MojoParameter( expression="${encoding}", defaultValue="${project.build.sourceEncoding}")
    @Parameter( property="encoding", defaultValue="${project.build.sourceEncoding}" )
    private String encoding;    

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     */
    @Override
    protected String getOutputDirectory()
    {
        return outputDirectory;
    }

    
     protected static List<ComponentDependency>  toComponentDependencies(List<Dependency>   dependencies)
     {
         //return PluginUtils.toComponentDependencies( dependencies )
         return GeneratorUtils.toComponentDependencies(dependencies);
         /*
         List<ComponentDependency>   componentDeps = new LinkedList<ComponentDependency>();
 
         for ( Iterator<Dependency> it = dependencies.iterator(); it.hasNext(); )
         {
             Dependency dependency = it.next();
             
             ComponentDependency cd = new ComponentDependency();
 
             cd.setArtifactId( dependency.getArtifactId() );
             cd.setGroupId( dependency.getGroupId() );
             cd.setVersion( dependency.getVersion() );
             cd.setType( dependency.getType() );
 
             componentDeps.add( cd );
         }
         
         return componentDeps;
         */ 
     }
 

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void executeReport( Locale locale )  throws MavenReportException
    {
        getLog().info( String.format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));

        if ( !project.getPackaging().equals( "maven-plugin" ) )
        {
            return;
        }

        try {
            loadUserInfoFromSettings();
        } catch (MojoExecutionException ex) {
            
            throw new MavenReportException("error reading credential", ex);
        }
        
        super.initTemplateProperties();
        
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
                getLog().warn("Plugin without mojos. " + e.getMessage());

            }
            
            // Generate the plugin's documentation
            generatePluginDocumentation( pluginDescriptor );

            // Write the overview
            //PluginOverviewRenderer r = new PluginOverviewRenderer( getSink(), pluginDescriptor, locale );
            //r.render();
        }
        catch ( ExtractionException e )
        {
            throw new MavenReportException( "Error extracting plugin descriptor: \'" + e.getLocalizedMessage() + "\'",
                                            e );
        }
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
     */
    public String getDescription( Locale locale )
    {
        return getBundle( locale ).getString( "report.plugin.description" );
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)
     */
    public String getName( Locale locale )
    {
        return "confluence-plugin-report";
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return "confluence-plugin-report";
    }

    private void generatePluginDocumentation( PluginDescriptor pluginDescriptor )  throws MavenReportException
    {
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
            final Confluence confluence = ConfluenceFactory.createInstanceDetectingVersion(getEndPoint(), proxyInfo, getUsername(), getPassword());

            getLog().info( ConfluenceUtils.getVersion(confluence) );

            File outputDir = new File( getOutputDirectory() );
            outputDir.mkdirs();

            getLog().info( "speceKey=" + getSpaceKey() + " parentPageTitle=" + getParentPageTitle());
            
            Page p = confluence.getPage(getSpaceKey(), getParentPageTitle());
           
            Generator generator =
            		new PluginConfluenceDocGenerator( this, confluence, p, templateWiki ); /*PluginXdocGenerator()*/;

            PluginToolsRequest request = new DefaultPluginToolsRequest( project, pluginDescriptor );
            		
            generator.execute( outputDir, request );

            // Issue 32
            final String title = getTitle();
            //String title = project.getArtifactId() + "-" + project.getVersion();
            
            generateChildren(confluence, getSpaceKey(), title, title);

            
            confluence.logout();
        }
        catch ( Exception e )
        {
            throw new MavenReportException( "Error writing plugin documentation", e );
        }

    }

    private static ResourceBundle getBundle( Locale locale )
    {
        return ResourceBundle.getBundle( "plugin-report", locale, ConfluenceGeneratePluginDocMojo.class.getClassLoader() );
    }

    @Override
    protected Renderer getSiteRenderer() {
        getLog().info("getSiteRenderer");
        return null;
    }

    /**
     * Generates an overview page with the list of goals
     * and a link to the goal's page.
     */
    /*
    static class PluginOverviewRenderer extends AbstractMavenReportRenderer
    {
        private final PluginDescriptor pluginDescriptor;

        private final Locale locale;

        public PluginOverviewRenderer( Sink sink, PluginDescriptor pluginDescriptor, Locale locale )
        {
            super( sink );

            this.pluginDescriptor = pluginDescriptor;

            this.locale = locale;
        }

        public String getTitle()
        {
            return getBundle( locale ).getString( "report.plugin.title" );
        }

        @SuppressWarnings("unchecked")
		public void renderBody()
        {
            startSection( getTitle() );

            paragraph( getBundle( locale ).getString( "report.plugin.goals.intro" ) );

            startTable();

            String goalColumnName = getBundle( locale ).getString( "report.plugin.goals.column.goal" );
            String descriptionColumnName = getBundle( locale ).getString( "report.plugin.goals.column.description" );

            tableHeader( new String[]{goalColumnName, descriptionColumnName} );

            List<MojoDescriptor> mojos = pluginDescriptor.getMojos();
        	
        	if( mojos!=null ) {

	            for ( MojoDescriptor mojo : mojos )
	            {
	                String goalName = mojo.getFullGoalName();
	                String goalDocumentationLink = "./" + mojo.getGoal() + "-mojo.html";
	                String description = mojo.getDescription();
	                if ( StringUtils.isEmpty( mojo.getDescription() ) )
	                {
	                    description = getBundle( locale ).getString( "report.plugin.goal.nodescription" );
	
	                }
	
	                sink.tableRow();
	                tableCell( createLinkPatternedText( goalName, goalDocumentationLink ) );
	                tableCell( description, true );
	                sink.tableRow_();
	            }
        	}
            endTable();

            endSection();
        }
    }
*/
}
