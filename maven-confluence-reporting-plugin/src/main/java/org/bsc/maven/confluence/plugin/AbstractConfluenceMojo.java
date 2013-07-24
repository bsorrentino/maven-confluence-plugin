package org.bsc.maven.confluence.plugin;

import org.bsc.maven.reporting.*;
import org.bsc.maven.reporting.model.Child;
import java.io.File;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.bsc.maven.reporting.model.ProcessUriException;
import org.bsc.maven.reporting.model.Site;
import org.codehaus.swizzle.confluence.Attachment;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * 
 * @author bsorrentino
 */
public abstract class AbstractConfluenceMojo extends AbstractMojo {
	
    
    /**
     * additional properties pass to template processor
     */
    @Parameter()
    private java.util.Map properties;
    /**
     * Confluence end point url 
     */
    @Parameter(property = "confluence.endPoint", defaultValue = "http://localhost:8080/rpc/xmlrpc")
    private String endPoint;
    /**
     * Confluence target confluence's spaceKey 
     */
    @Parameter(property = "confluence.spaceKey", required = true)
    private String spaceKey;
    /**
     * Confluence target confluence's spaceKey 
     */
    @Parameter(property = "confluence.parentPage", defaultValue = "Home")
    private String parentPageTitle;
    /**
     * Confluence username 
     */
    @Parameter(property = "confluence.userName", required = false)
    private String username;
    /**
     * Confluence password 
     */
    @Parameter(property = "confluence.password", required = false)
    private String password;

    /**
     * Maven Project
     */
    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;
    
    /**
     * Home page template source. Template name will be used also as template source for children
     */
    @Parameter(defaultValue = "${basedir}/src/site/confluence/template.wiki")
    protected java.io.File templateWiki;
    
    /**
     * child pages - 
     * <pre>&lt;child&gt;&lt;name/&gt;[&lt;source/&gt]&lt;/child&gt</pre>
     * 
     * @deprecated use children folder instead
     */
    @Parameter()
    private java.util.List children;
    
    /**
     * attachment folder
     */
    @Parameter(defaultValue = "${basedir}/src/site/confluence/attachments")
    private java.io.File attachmentFolder;
    
    /**
     * children folder
     */
    @Parameter(defaultValue = "${basedir}/src/site/confluence/children")
    private java.io.File childrenFolder;
    
    /**
     * During publish of documentation related to a new release, if it's true, the pages related to SNAPSHOT will be removed 
     */
    @Parameter(property = "confluence.removeSnapshots", required = false,  defaultValue = "false")
    protected boolean removeSnapshots = false;
    
    
    /**
     * Labels to add
     */
    @Parameter()
    java.util.List<String> labels;
    
    
    /**
     * @parameter expression="${settings}"
     * @readonly
     * @since 3.1.1
     */
    @Parameter(readonly = true, property = "settings")
    protected org.apache.maven.settings.Settings mavenSettings;

    /**
     * Confluence Page Title
     * @since 3.1.3
     */
    
    @Parameter(property = "project.build.finalName", required = false)
    private String title;

    /**
     * Children files extension
     * @since 3.2.1
     */   
    @Parameter(property = "wikiFilesExt", required = false, defaultValue=".wiki")
    private String wikiFilesExt;
    
    
    /**
     * Issue 39
     * 
     * Server's <code>id</code> in <code>settings.xml</code> to look up username and password.
     * Defaults to <code>${url}</code> if not given.
     *
     * @since 1.0
     */
    @Parameter(property="confluence.serverId")
    private String serverId;
    
    /**
     * Issue 39
     * 
     * MNG-4384
     * 
     * @since 1.5
     */
    @Component(role=org.sonatype.plexus.components.sec.dispatcher.SecDispatcher.class,hint="default")
    private SecDispatcher securityDispatcher;    
    
    /**
     * 
     */
    public AbstractConfluenceMojo() {
        children = Collections.emptyList();
    }

    protected List<Child> getChildren() {
        return children;
    }

    protected File getChildrenFolder() {
        return childrenFolder;
    }

    protected File getAttachmentFolder() {
        return attachmentFolder;
    }

    
    /**
     * 
     * @return 
     */
    protected final String getTitle() {
        return title;
    }

    /**
     * 
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return 
     */
    public String getFileExt() {
        return (wikiFilesExt.charAt(0)=='.' ) ? wikiFilesExt : ".".concat(wikiFilesExt);
    }
    
    
    @SuppressWarnings("unchecked")
    public final java.util.Map<String, String> getProperties() {
        if (null == properties) {
            properties = new java.util.HashMap<String, String>(5);
        }
        return properties;
    }

    public final String getEndPoint() {
        return endPoint;
    }

    public final String getSpaceKey() {
        return spaceKey;
    }

    public final String getParentPageTitle() {
        return parentPageTitle;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    public MavenProject getProject() {
        return project;
    }

    public boolean isRemoveSnapshots() {
        return removeSnapshots;
    }

    public boolean isSnapshot() {
        final String version = project.getVersion();

        return (version != null && version.endsWith("-SNAPSHOT"));

    }

    public List<String> getLabels() {
        
        if( labels==null ) {
            return Collections.emptyList();
        }
        return labels;
    }

    
    /**
     * Issue 39
     * 
     * Load username password from settings if user has not set them in JVM properties
     * 
     * @throws MojoExecutionException
     */
    protected void loadUserInfoFromSettings() throws MojoExecutionException
    {

        if ( ( getUsername() == null || getPassword() == null ) && ( mavenSettings != null ) )
        {
            if ( this.serverId == null ) throw new MojoExecutionException("SettingKey must be set! (username and/or password are not provided)");

            Server server = this.mavenSettings.getServer( this.serverId );

            if ( server == null ) throw new MojoExecutionException( String.format("server with id [%s] not found in settings!", this.serverId ));

            if ( getUsername() == null && server.getUsername() !=null  ) username = server.getUsername();

            if( getPassword() == null && server.getPassword() != null ) {
                    try
                    {
                        //
                        // FIX to resolve
                        // org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException: 
                        // java.io.FileNotFoundException: ~/.settings-security.xml (No such file or directory)
                        //
                        if( securityDispatcher instanceof DefaultSecDispatcher ) {
                        
                            
                            //System.setProperty( DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION, sb.toString() );
                                
                            ((DefaultSecDispatcher)securityDispatcher).setConfigurationFile("~/.m2/settings-security.xml");
                        }
                        
                        password = securityDispatcher.decrypt( server.getPassword() );
                    }
                    catch ( SecDispatcherException e )
                    {
                        throw new MojoExecutionException( e.getMessage() );
                    }
            }
        }
    }
    
    /**
     * initialize properties shared with template
     */
    protected void initTemplateProperties() {
    
        processProperties();
        
        getProperties().put("pageTitle", getTitle());
        getProperties().put("parentPageTitle", getParentPageTitle());
        getProperties().put("artifactId", project.getArtifactId());
        getProperties().put("version", project.getVersion());
        getProperties().put("groupId", project.getGroupId());
        getProperties().put("name", project.getName());
        getProperties().put("description", project.getDescription());

        java.util.Properties projectProps = project.getProperties();
        
        if( projectProps!=null ) {
            
            for(Map.Entry<Object,Object> e : projectProps.entrySet()){
                getProperties().put( String.valueOf(e.getKey()), String.valueOf(e.getValue()) );
            }
        }
        
    }
    
    public void addProperties(MiniTemplator t) {
        java.util.Map<String, String> props = getProperties();

        if (props == null || props.isEmpty()) {
            getLog().info("no properties set!");
        } else {
            for (java.util.Map.Entry<String, String> e : props.entrySet()) {
                //getLog().debug(String.format("property %s = %s", e.getKey(), e.getValue()));

                try {
                    t.setVariable(e.getKey(), e.getValue(), true /* isOptional */);
                } catch (VariableNotDefinedException e1) {
                    getLog().warn(String.format("variable %s not defined in template", e.getKey()));
                }
            }
        }

    }

    protected <T extends Site.Page> Page  generateChild(Confluence confluence,  T child, String spaceKey, String parentPageTitle, String titlePrefix) {

        java.net.URI source = child.getUri(getProject(), getFileExt());

        getLog().info( String.format("generateChild spacekey=[%s] parentPageTtile=[%s]\n%s", spaceKey, parentPageTitle, child.toString()));

        try {

            if (!isSnapshot() && isRemoveSnapshots()) {
                final String snapshot = titlePrefix.concat("-SNAPSHOT");
                boolean deleted = ConfluenceUtils.removePage(confluence, spaceKey, parentPageTitle, snapshot);

                if (deleted) {
                    getLog().info(String.format("Page [%s] has been removed!", snapshot));
                }
            }

            final String pageName = String.format("%s - %s", titlePrefix, child.getName());

            Page p = ConfluenceUtils.getOrCreatePage(confluence, spaceKey, parentPageTitle, pageName);

            if( source != null /*&& source.isFile() && source.exists() */) {

                final MiniTemplator t = new MiniTemplator.Builder()
                    .setSkipUndefinedVars(true)
                    .build( Site.processUri(source) );
            
                addProperties(t);

                p.setContent(t.generateOutput());
            }
            

            p = confluence.storePage(p);
            
            for( String label : child.getLabels() ) {
                
                confluence.addLabelByName(label, Long.parseLong(p.getId()) );
            }

            child.setName( pageName );
            
            return p;

        } catch (Exception e) {
            final String msg = "error loading template";
            getLog().error(msg, e);
            //throw new MavenReportException(msg, e);

            return null;
        }

    }

    @Deprecated
    protected <T extends Site.Page> void generateChildrenFromChild(final Confluence confluence, final java.io.File folder, final String spaceKey, final T parentChild ) /*throws MavenReportException*/ {

        getLog().info(String.format("generateChildrenFromChild [%s]", folder.getAbsolutePath()) );

        if (folder.exists() && folder.isDirectory()) {

            folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    getLog().info(String.format("generateChildrenFromChild\n\t process file [%s]", file.getPath()) );

                    if( file.isHidden() || file.getName().charAt(0)=='.') return false ;
                    
                    if( file.isDirectory() ) {
                        Child child = new Child();

                        child.setName(file.getName());
                        child.setSource( new java.io.File(file,templateWiki.getName()));

                        if( generateChild(confluence,child,  spaceKey, parentChild.getName(), parentChild.getName()) != null ) {
 
                            generateChildrenFromChild(confluence, file, spaceKey, child );    
                        }
                       return true;
                    }
                    
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith( getFileExt() ) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Child child = new Child();
                    final int extensionLen = getFileExt().length();

                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    child.setSource(file);
                    

                    generateChild(confluence, child, spaceKey, parentChild.getName(), parentChild.getName() );
                    return false;

                }
            });
        }

    }
    
    /**
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    protected void generateChildren(final Confluence confluence, final String spaceKey, final String parentPageTitle, final String titlePrefix) /*throws MavenReportException*/ {

        getLog().info(String.format("generateChildren # [%d]", children.size()));

        for (Child child : (java.util.List<Child>) children) {

            generateChild(confluence, child, spaceKey, parentPageTitle, titlePrefix );
        }

        if (childrenFolder.exists() && childrenFolder.isDirectory()) {

            childrenFolder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    getLog().info(String.format("generateChildren\n\t process file [%s]", file.getPath()) );

                    if( file.isHidden() || file.getName().charAt(0)=='.') return false ;

                    if( file.isDirectory() ) {
                       
                        Child parentChild = new Child();

                        parentChild.setName(file.getName());
                        parentChild.setSource( new java.io.File(file,templateWiki.getName()));

                        if( generateChild(confluence, parentChild, spaceKey, parentPageTitle, titlePrefix) != null ) {
 
                            generateChildrenFromChild(confluence, file, spaceKey, parentChild );    
                        }
                        
                        return false;
                    }
                     
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith(getFileExt()) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Child child = new Child();
                    
                    final int extensionLen = getFileExt().length();
                    
                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    child.setSource(file);

                    generateChild(confluence, child, spaceKey, parentPageTitle, titlePrefix);
                    return false;

                }
            });
        }

    }

    @Deprecated
    protected void generateAttachments(Confluence confluence, Page page) /*throws MavenReportException*/ {

        getLog().info(String.format("generateAttachments pageId [%s]", page.getId()));

        java.io.File[] files = attachmentFolder.listFiles();

        if (files == null || files.length == 0) {
            getLog().info(String.format("No attachments found in folder [%s] ", attachmentFolder.getPath()));
            return;
        }

        final String version = "0";
        for (java.io.File f : files) {

            if (f.isDirectory() || f.isHidden()) {
                continue;
            }

            Attachment a = null;

            try {
                a = confluence.getAttachment(page.getId(), f.getName(), version);
            } catch (Exception e) {
                getLog().warn(String.format("Error getting attachment [%s] from confluence: [%s]", f.getName(), e.getMessage()));
            }

            if (a != null) {


                java.util.Date date = a.getCreated();

                if (date == null) {
                    getLog().warn(String.format("creation date of attachments [%s] is undefined. It will be replaced! ", a.getFileName()));
                } else {
                    if (f.lastModified() > date.getTime()) {
                        getLog().info(String.format("attachment [%s] is more recent than the remote one. It will be replaced! ", a.getFileName()));
                    } else {
                        getLog().info(String.format("attachment [%s] skipped! no updated detected", a.getFileName()));
                        continue;

                    }
                }
            } else {
                a = new Attachment();
                a.setComment(String.format( "%s - attached by maven-confluence-plugin", f.getName()));
                a.setFileName(f.getName());
                a.setContentType("application/octet-stream");

            }

            try {
                ConfluenceUtils.addAttchment(confluence, page, a, f);
            } catch (Exception e) {
                getLog().error(String.format("Error uploading attachment [%s] ", f.getName()), e);
            }

        }

    }
       
    /**
     * Issue 46
     * 
     **/ 
    private void processProperties() {
        
        for( Map.Entry<String,String> e : this.getProperties().entrySet() ) {
            
            try {
                
                final java.net.URI uri = new java.net.URI( e.getValue() );
                
                if( uri.getScheme() == null ) {
                    continue;
                }
                getProperties().put( e.getKey(), processUri( uri ));
                
            } catch (ProcessUriException ex) {
                getLog().warn( String.format("error processing value of property [%s]\n%s", e.getKey(), ex.getMessage()));
                if( ex.getCause() != null )
                    getLog().debug( ex.getCause() );
                
            } catch (URISyntaxException ex) {
   
                // DO Nothing
                getLog().debug( String.format("property [%s] is not a valid uri", e.getKey()));
            }
            
        }
    }
    
    /**
     * 
     * @param uri
     * @return
     * @throws org.bsc.maven.reporting.AbstractConfluenceMojo.ProcessUriException 
     */
    private String processUri( java.net.URI uri ) throws ProcessUriException {
    
        try {
            return toString( Site.processUri(uri) );
        } catch (Exception ex) {
            throw new ProcessUriException("error reading content!", ex);
        }
    }
    
    /**
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private String toString(java.io.Reader reader) throws IOException {
        if (reader == null) {
            throw new IllegalArgumentException("reader");
        }

        java.io.Reader r = null;
        
        try {

            if (reader instanceof java.io.BufferedReader) {
                r = reader;
            } else {

                r = new java.io.BufferedReader(reader);
            }

            StringBuilder contents = new StringBuilder(4096);

            int c;
            while ((c = r.read()) != -1) {

                contents.append((char) c);
            }

            return contents.toString();

        }
        finally {
            if( r!= null ) {
                r.close();
            }
        }
    }
	    
}
