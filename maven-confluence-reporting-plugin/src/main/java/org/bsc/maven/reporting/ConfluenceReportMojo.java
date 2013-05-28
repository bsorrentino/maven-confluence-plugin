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
import org.apache.maven.doxia.sink.SinkFactory;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
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

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Proxy;
import org.bsc.maven.reporting.model.Site;
import org.codehaus.swizzle.confluence.ConfluenceFactory;

/**
 * Generate Project's documentation in confluence's wiki format
 */
//@MojoPhase("site")
//@MojoGoal("confluence-summary")
@Mojo( name="confluence-summary", defaultPhase = LifecyclePhase.SITE )
public class ConfluenceReportMojo extends AbstractConfluenceSiteReportMojo {

    private static final String PROJECT_DEPENDENCIES_VAR = "project.dependencies";
    private static final String PROJECT_SCM_MANAGER_VAR = "project.scmManager";
    private static final String PROJECT_SUMMARY_VAR = "project.summary";
    
    /**
     * Local Repository.
     *
     */
    //@MojoParameter(expression = "${localRepository}", required = true, readonly = true)
    @Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
    protected ArtifactRepository localRepository;
    /**
     */
    //@MojoComponent
    @Component
    protected ArtifactMetadataSource artifactMetadataSource;
    /**
     */
    //@MojoComponent
    @Component
    private ArtifactCollector collector;
    /**
     *
     */
    //@MojoComponent
    @Component(role=org.apache.maven.artifact.factory.ArtifactFactory.class)
    protected ArtifactFactory factory;
    /**
     * Maven Project Builder.
     *
     */
    //@MojoComponent
    @Component
    private MavenProjectBuilder mavenProjectBuilder;

    //@MojoComponent
    @Component
    protected I18N i18n;
    
    //@MojoComponent()
    //protected Renderer siteRenderer;
    //@MojoParameter(expression = "${project.reporting.outputDirectory}")
    @Parameter(property = "project.reporting.outputDirectory")
    protected java.io.File outputDirectory;
    /**
     * Maven SCM Manager.
     *
     */
    //@MojoParameter(expression = "${component.org.apache.maven.scm.manager.ScmManager}", required = true, readonly = true)
    //@Parameter(defaultValue = "${component.org.apache.maven.scm.manager.ScmManager}", required = true, readonly = true)
    @Component(role=ScmManager.class)
    protected ScmManager scmManager;
    
    /**
     * The directory name to checkout right after the scm url
     *
     */
    //@MojoParameter(expression = "${project.artifactId}", required = true)
    @Parameter(defaultValue = "${project.artifactId}", required = true)
    private String checkoutDirectoryName;
    /**
     * The scm anonymous connection url.
     *
     */
    //@MojoParameter(defaultValue = "${project.scm.connection}")
    @Parameter(defaultValue = "${project.scm.connection}")
    private String anonymousConnection;
    /**
     * The scm developer connection url.
     *
     */
    //@MojoParameter(defaultValue = "${project.scm.developerConnection}")
    @Parameter(defaultValue = "${project.scm.developerConnection}")
    private String developerConnection;
    /**
     * The scm web access url.
     *
     */
    //@MojoParameter(defaultValue = "${project.scm.url}")
    @Parameter(defaultValue = "${project.scm.url}")
    private String webAccessUrl;

    //private Writer confluenceWriter;
    //protected Sink confluenceSink;
    /**
     * 
     */
    public ConfluenceReportMojo() {
        super();
    }

    @Override
    public void execute() throws MojoExecutionException {

        getLog().info("execute");

        try {
            super.execute();
        } catch (Throwable t) {
            getLog().error(t);
        }
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
    protected void executeReport(Locale locale) throws MavenReportException {
        getLog().info(String.format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));
        try {
            loadUserInfoFromSettings();
        } catch (MojoExecutionException ex) {
            
            throw new MavenReportException("error reading credential", ex);
        }

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
            throw new MavenReportException(msg, e);
        }
        
        super.addProperties(t);

       /////////////////////////////////////////////////////////////////
       // SUMMARY
       /////////////////////////////////////////////////////////////////
       {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w, getSink());
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
            final Sink sink = new ConfluenceSink(w, getSink());
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
            final Sink sink = new ConfluenceSink(w, getSink());
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

    protected void executeReport3_2_x(Locale locale) throws MavenReportException {
        getLog().info(String.format("executeReport isSnapshot = [%b] isRemoveSnapshots = [%b]", isSnapshot(), isRemoveSnapshots()));
        try {
            loadUserInfoFromSettings();
        } catch (MojoExecutionException ex) {
            
            throw new MavenReportException("error reading credential", ex);
        }
        

        // Issue 32
        final String title = getTitle();
        //String title = project.getArtifactId() + "-" + project.getVersion();

        super.initTemplateProperties();
        
        MiniTemplator t = null;

        if (templateWiki == null || !templateWiki.exists()) {
            getLog().warn("template not set! default using ...");

            java.net.URL sourceUrl = getClass().getClassLoader().getResource("defaultTemplate.confluence");

            if (sourceUrl == null) {
                final String msg = "default template cannot be found";
                getLog().error(msg);
                throw new MavenReportException(msg);
            }

            try {
                t = new MiniTemplator.Builder()
                        .setSkipUndefinedVars(true)
                        .build( sourceUrl );
            } catch (Exception e) {
                final String msg = "error loading template";
                getLog().error(msg, e);
                throw new MavenReportException(msg, e);
            }
            
/*            
            java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("defaultTemplate.confluence");

            if (is == null) {
                final String msg = "default template cannot be found";
                getLog().error(msg);
                throw new MavenReportException(msg);
            }

            try {
                t = new MiniTemplator(new java.io.InputStreamReader(is));
            } catch (Exception e) {
                final String msg = "error loading template";
                getLog().error(msg, e);
                throw new MavenReportException(msg, e);
            }
         
*/
        } else {
            try {
                
                  t = new MiniTemplator.Builder()
                    .setSkipUndefinedVars(true)
                    .build( Site.processUri(templateWiki.toURI()) );

            } catch (Exception e) {
                final String msg = "error loading template";
                getLog().error(msg, e);
                throw new MavenReportException(msg, e);
            }

        }

        super.addProperties(t);

        { // SUMMARY

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w, getSink());
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

        //getSink().pageBreak();

        {

            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w, getSink());
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

        //getSink().pageBreak();

        {
            final StringWriter w = new StringWriter(10 * 1024);
            final Sink sink = new ConfluenceSink(w, getSink());
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

        //String wiki = confluenceWriter.toString();

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


            Page p = ConfluenceUtils.getOrCreatePage(confluence, getSpaceKey(), getParentPageTitle(), title);

            p.setContent(wiki);

            p = confluence.storePage(p);

            for( String label : getLabels() ) {
                
                confluence.addLabelByName(label, Long.parseLong(p.getId()) );
            }
            
            generateChildren(confluence, getSpaceKey(), title, title);

            generateAttachments(confluence, p);

        } catch (Exception e) {
            getLog().warn("has been imposssible connect to confluence due exception", e);
        } finally {
            confluenceLogout(confluence);
        }

        /*
        System.out.println( "========================================");
        System.out.println( wiki);
        System.out.println( "========================================");
         */
    }

    @Override
    public void generate(Sink aSink, SinkFactory aSinkFactory, Locale aLocale) throws MavenReportException {

        getLog().info("generate " + aSink);


        super.generate(aSink, aSinkFactory, aLocale);
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

    @Override
    protected String getOutputDirectory() {
        return outputDirectory.toString();
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
