package org.bsc.maven.plugin.confluence;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.tools.plugin.DefaultPluginToolsRequest;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.extractor.ExtractionException;
import org.apache.maven.tools.plugin.scanner.MojoScanner;
import org.apache.maven.tools.plugin.util.PluginUtils;
import org.bsc.maven.reporting.AbstractConfluenceReportMojo;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 * Generate Plugin's documentation in confluence's wiki format
 * 
 * 
 *
 */
@MojoGoal("plugin-confluence-summary")
public class ConfluenceGeneratePluginDocMojo extends AbstractConfluenceReportMojo {

    /**
     * Report output directory.
     *
     */
    @MojoParameter(expression="${project.build.directory}/generated-site/confluence",required=true)
    private String outputDirectory;


    /**
     * Mojo scanner tools.
     *
     */
    @MojoComponent
    protected MojoScanner mojoScanner;


     /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     */
    protected String getOutputDirectory()
    {
        return outputDirectory;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     */
    @SuppressWarnings("unchecked")
	protected void executeReport( Locale locale )  throws MavenReportException
    {
        if ( !project.getPackaging().equals( "maven-plugin" ) )
        {
            return;
        }

        getLog().info( String.format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));

        String goalPrefix = PluginDescriptor.getGoalPrefixFromArtifactId( project.getArtifactId() );

        PluginDescriptor pluginDescriptor = new PluginDescriptor();

        pluginDescriptor.setGroupId( project.getGroupId() );

        pluginDescriptor.setArtifactId( project.getArtifactId() );

        pluginDescriptor.setVersion( project.getVersion() );

        pluginDescriptor.setGoalPrefix( goalPrefix );

        try
        {
            java.util.List dependencies = new java.util.ArrayList();
            
            dependencies.addAll(PluginUtils.toComponentDependencies( project.getRuntimeDependencies() ));
            dependencies.addAll(PluginUtils.toComponentDependencies( project.getCompileDependencies() ));

            pluginDescriptor.setDependencies( dependencies );

            PluginToolsRequest request = new DefaultPluginToolsRequest( project, pluginDescriptor );
                    
            mojoScanner.populatePluginDescriptor( request );
            
            pluginDescriptor.setDescription( project.getDescription() );

            // Generate the plugin's documentation
            generatePluginDocumentation( pluginDescriptor );

            // Write the overview
            //PluginOverviewRenderer r = new PluginOverviewRenderer( getSink(), pluginDescriptor, locale );
            //r.render();
        }
        catch ( InvalidPluginDescriptorException e )
        {
            throw new MavenReportException( 
            		"Error extracting plugin descriptor: \'" + e.getLocalizedMessage() + "\'", e );
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
        return getBundle( locale ).getString( "report.plugin.name" );
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return "plugin-info";
    }

    private void generatePluginDocumentation( PluginDescriptor pluginDescriptor )  throws MavenReportException
    {
        try
        {
    		
            
            //Confluence confluence = new Confluence( getEndPoint() );
            //confluence.login(getUsername(), getPassword());
            final Confluence confluence = ConfluenceFactory.createInstanceDetectingVersion(getEndPoint(), getUsername(), getPassword());

            getLog().info( ConfluenceUtils.getVersion(confluence) );

            File outputDir = new File( getOutputDirectory() );
            outputDir.mkdirs();

            getLog().info( "speceKey=" + getSpaceKey() + " parentPageTitle=" + getParentPageTitle());
            
            Page p = confluence.getPage(getSpaceKey(), getParentPageTitle());
            
            org.apache.maven.tools.plugin.generator.Generator generator = 
            		new PluginConfluenceDocGenerator( this, confluence, p, templateWiki ); /*PluginXdocGenerator()*/;

            PluginToolsRequest request = new DefaultPluginToolsRequest( project, pluginDescriptor );
            		
            generator.execute( outputDir, request );

            String title = project.getArtifactId() + "-" + project.getVersion();
            
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
