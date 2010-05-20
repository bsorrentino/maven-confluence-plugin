package org.bsc.maven.reporting;
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
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.scm.manager.ScmManager;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.bsc.maven.reporting.renderer.DependenciesRenderer;
import org.bsc.maven.reporting.renderer.ProjectSummaryRenderer;
import org.bsc.maven.reporting.renderer.ScmRenderer;
import org.bsc.maven.reporting.sink.ConfluenceSink;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoPhase;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;



/**
 * Generate Project's documentation in confluence's wiki format
 */
@MojoPhase("site")
@MojoGoal("confluence-summary")
public class ConfluenceReportMojo extends AbstractMavenReport {

	private static final String PROJECT_DEPENDENCIES_VAR = "project.dependencies";

	private static final String PROJECT_SCM_MANAGER_VAR = "project.scmManager";

	private static final String PROJECT_SUMMARY_VAR = "project.summary";

	/**
	 * Confluence end point url 
	 */
	@MojoParameter(expression="${confluence.endPoint}", defaultValue="http://localhost:8080/rpc/xmlrpc")
	private String endPoint;

	/**
	 * Confluence target confluence's spaceKey 
	 */
	@MojoParameter(expression="${confluence.spaceKey}", required=true)
	private String spaceKey;

	/**
	 * Confluence target confluence's spaceKey 
	 */
	@MojoParameter(expression="${confluence.parentPage}",defaultValue="Home")
	private String parentPageTitle;
	
	/**
	 * Confluence username 
	 */
	@MojoParameter(expression="${confluence.userName}",defaultValue="admin")
	private String username;

	/**
	 * Confluence password 
	 */
	@MojoParameter(expression="${confluence.password}")
	private String password;
	
	@MojoParameter(expression="${project}")
	protected MavenProject project;

    /**
     * Local Repository.
     *
     */
	@MojoParameter(expression="${localRepository}",required=true,readonly=true)
    protected ArtifactRepository localRepository;

    /**
     */
	@MojoComponent
    protected ArtifactMetadataSource artifactMetadataSource;

    /**
     */
	@MojoComponent
    private ArtifactCollector collector;
	
    /**
     *
     */
	@MojoComponent
    protected ArtifactFactory factory;
	
    /**
     * Maven Project Builder.
     *
     */
	@MojoComponent
    private MavenProjectBuilder mavenProjectBuilder;

	@MojoComponent
	protected I18N i18n;

	//@MojoComponent()
	//protected Renderer siteRenderer;

	@MojoParameter(expression="${project.reporting.outputDirectory}")
	protected java.io.File outputDirectory;

    /**
     * Maven SCM Manager.
     *
     */
	@MojoParameter(expression="${component.org.apache.maven.scm.manager.ScmManager}",required=true,readonly=true)
	protected ScmManager scmManager;

    /**
     * The directory name to checkout right after the scm url
     *
     */
	@MojoParameter(expression="${project.artifactId}",required=true)
	private String checkoutDirectoryName;

    /**
     * The scm anonymous connection url.
     *
     */
	@MojoParameter(defaultValue="${project.scm.connection}")
    private String anonymousConnection;

    /**
     * The scm developer connection url.
     *
     */
	@MojoParameter(defaultValue="${project.scm.developerConnection}")
    private String developerConnection;

    /**
     * The scm web access url.
     *
     */
	@MojoParameter(defaultValue="${project.scm.url}")
    private String webAccessUrl;
	
	@MojoParameter(defaultValue="${basedir}/src/site/confluence/template.wiki", description="MiniTemplator source. Default location is ${basedir}/src/site/confluence")
	private java.io.File templateWiki;
	
	@MojoParameter(description="child pages - &lt;child&gt;&lt;name/&gt;[&lt;source/&gt]&lt;/child&gt")
	private java.util.List<Child> children;
	
	//private Writer confluenceWriter;
	//protected Sink confluenceSink;
	

	/**
	 * 
	 */
	public ConfluenceReportMojo() {
		children = Collections.emptyList();
	}

	@Override
	public void execute() throws MojoExecutionException {

		getLog().info( "execute" );
		
		super.execute();
	}

	/**
	 * 
	 * @param confluence
	 */
	private void confluenceLogout( Confluence confluence ) {

		if(null==confluence) return;
		
		try {
			if(!confluence.logout()) {
				getLog().warn("confluence logout has failed!" );
			}
		} catch (Exception e) {
			getLog().warn("confluence logout has failed due exception ", e );
		}
			
		
	}
	
	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void generateChildren(Confluence confluence, String spaceKey, String parentPageTitle, String titlePrefix ) /*throws MavenReportException*/ {
		
		getLog().info( String.format( "generateChildren # [%d]", children.size()) );

		for( Child child : (java.util.List<Child>)children ) {
		
			java.io.File source = child.getSource(getProject());
			
			getLog().info( child.toString() );
			
			try {
				
				final MiniTemplator t = new MiniTemplator(new java.io.FileReader(source));
				
	            Page p = ConfluenceUtils.getOrCreatePage( confluence, spaceKey, parentPageTitle, String.format("%s - %s", titlePrefix, child.getName())  );
	            
	            p.setContent(t.generateOutput());
	            
	            confluence.storePage(p);

				
			} catch (Exception e) {
				final String msg = "error loading template";
				getLog().error(msg,e);
				//throw new MavenReportException(msg, e);
			}
		}
		
	}
	
	@Override
	protected void executeReport(Locale locale) throws MavenReportException {
		getLog().info( "executeReport " );
		
		MiniTemplator t = null;
		
		if( templateWiki==null || !templateWiki.exists()) {
			getLog().warn("template not set! default using ...");
			
			java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("defaultTemplate.wiki");
			
			if( is==null ) {
				final String msg = "default template cannot be found";
				getLog().error( msg);
				throw new MavenReportException(msg);
			}
			
			try {
				t = new MiniTemplator(new java.io.InputStreamReader(is));
			} catch (Exception e) {
				final String msg = "error loading template";
				getLog().error(msg,e);
				throw new MavenReportException(msg, e);
			}
		}
		else {
			try {
				t = new MiniTemplator(templateWiki);
			} catch (Exception e) {
				final String msg = "error loading template";
				getLog().error(msg,e);
				throw new MavenReportException(msg, e);
			}
			
		}
		
		{ // SUMMARY
		
			final StringWriter w = new StringWriter( 10 * 1024 );
			final Sink sink = new ConfluenceSink( w ,(org.codehaus.doxia.sink.Sink) getSink());
			//final Sink sink = getSink();

			new ProjectSummaryRenderer( sink, 
				project, 
				i18n, 
				locale ).render();
        
			try {
				t.setVariable(PROJECT_SUMMARY_VAR, w.toString());
			} catch (VariableNotDefinedException e) {
				getLog().warn( String.format( "variable %s not defined in template", PROJECT_SUMMARY_VAR));
			}
        
		}
		
        //getSink().pageBreak();
        
		{
		
			final StringWriter w = new StringWriter( 10 * 1024 );
			final Sink sink = new ConfluenceSink(w,(org.codehaus.doxia.sink.Sink) getSink());
			//final Sink sink = getSink();

			new ScmRenderer( scmManager, 
							sink, 
	        				project.getModel(), 
	        				i18n, 
	        				locale, 
	        				checkoutDirectoryName, 
	        				webAccessUrl, 
	        				anonymousConnection, 
	        				developerConnection).render();

			try {
				t.setVariable(PROJECT_SCM_MANAGER_VAR, w.toString());
			} catch (VariableNotDefinedException e) {
				getLog().warn( String.format( "variable %s not defined in template", PROJECT_SCM_MANAGER_VAR));
			}
		}
		
        //getSink().pageBreak();

		{
			final StringWriter w = new StringWriter( 10 * 1024 );
			final Sink sink = new ConfluenceSink(w,(org.codehaus.doxia.sink.Sink) getSink());
			//final Sink sink = getSink();

	        new DependenciesRenderer( sink, 
					project, 
					mavenProjectBuilder, 
					localRepository, 
					factory, 
					i18n, 
					locale, 
					resolveProject(), 
					getLog() ).render();

			try {
				t.setVariable(PROJECT_DEPENDENCIES_VAR, w.toString());
			} catch (VariableNotDefinedException e) {
				getLog().warn( String.format( "variable %s not defined in template", PROJECT_DEPENDENCIES_VAR));
			}
		}
		
		String wiki = t.generateOutput();

		//String wiki = confluenceWriter.toString();
		
		//
		// write in confluence
		// 
		Confluence confluence = null;
		
		try {
			confluence = new Confluence(endPoint);
			
			confluence.login(username, password);
			
	    	String title = project.getArtifactId() + "-" + project.getVersion();

            Page p = ConfluenceUtils.getOrCreatePage( confluence, spaceKey, parentPageTitle, title );
            
            p.setContent(wiki);
            
            confluence.storePage(p);
            
            generateChildren(confluence, spaceKey, title, title);
			
		} catch (Exception e) {
			getLog().warn( "has been imposssible connect to confluence due exception", e );
		}
		finally {
			confluenceLogout(confluence);
		}
		
		System.out.println( "========================================");
		System.out.println( wiki);
		System.out.println( "========================================");
		
	}
	
	@Override
	public void generate(org.codehaus.doxia.sink.Sink sink, Locale locale) throws MavenReportException {

		getLog().info( "generate " + sink );
/*
		confluenceWriter = new StringWriter( 10 * 1024 );
		
		confluenceSink = new ConfluenceSink(confluenceWriter,sink);
		
		super.generate((org.codehaus.doxia.sink.Sink) confluenceSink, locale);
*/
		super.generate(sink, locale);
		
	}

	/**
	 * 
	 * @return
	 */
    private ReportingResolutionListener resolveProject()
    {
        Map managedVersions = null;
        try
        {
            managedVersions = createManagedVersionMap( project.getId(), project.getDependencyManagement() );
        }
        catch ( ProjectBuildingException e )
        {
            getLog().error( "An error occurred while resolving project dependencies.", e );
        }

        ReportingResolutionListener listener = new ReportingResolutionListener();

        try
        {
            collector.collect( project.getDependencyArtifacts(), project.getArtifact(), managedVersions,
                               localRepository, project.getRemoteArtifactRepositories(), artifactMetadataSource, null,
                               Collections.singletonList( listener ) );
        }
        catch ( ArtifactResolutionException e )
        {
            getLog().error( "An error occurred while resolving project dependencies.", e );
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
    private Map createManagedVersionMap( String projectId, DependencyManagement dependencyManagement )  throws ProjectBuildingException
    {
	    Map map;
	    if ( dependencyManagement != null && dependencyManagement.getDependencies() != null )
	    {
	        map = new HashMap();
	        for ( Iterator i = dependencyManagement.getDependencies().iterator(); i.hasNext(); )
	        {
	            Dependency d = (Dependency) i.next();
	
	            try
	            {
	                VersionRange versionRange = VersionRange.createFromVersionSpec( d.getVersion() );
	                Artifact artifact = factory.createDependencyArtifact( d.getGroupId(), d.getArtifactId(),
	                                                                      versionRange, d.getType(), d.getClassifier(),
	                                                                      d.getScope() );
	                map.put( d.getManagementKey(), artifact );
	            }
	            catch ( InvalidVersionSpecificationException e )
	            {
	                throw new ProjectBuildingException( projectId, "Unable to parse version '" + d.getVersion() +
	                    "' for dependency '" + d.getManagementKey() + "': " + e.getMessage(), e );
	            }
	        }
	    }
	    else
	    {
	        map = Collections.EMPTY_MAP;
	    }
	    return map;
	}
	
	
	@Override
	protected String getOutputDirectory() {
		return outputDirectory.toString();
	}

	@Override
	protected MavenProject getProject() {
		return project;
	}

	
	@Override
	protected Renderer getSiteRenderer() {

		getLog().info("getSiteRenderer");
		return null;
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



	
}
