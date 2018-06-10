package org.bsc.maven.confluence.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.confluence.ConfluenceService.Storage.Representation;
import org.bsc.confluence.DeployStateManager;
import org.bsc.confluence.model.ProcessUriException;
import org.bsc.confluence.model.Site;
import org.bsc.functional.Tuple2;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
/**
 *
 * @author bsorrentino
 */
public abstract class AbstractConfluenceMojo extends AbstractBaseConfluenceMojo {

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
	   * prefix child page with the title of the parent
     *
     * @since 4.9
     */
    @Parameter(property = "confluence.childrenTitlesPrefixed", required = false,  defaultValue = "true")
    protected boolean childrenTitlesPrefixed = true;

    /**
     * Labels to add
     */
    @Parameter()
    java.util.List<String> labels;

    /**
     * Confluence Page Title
     * @since 3.1.3
     */

    @Parameter(alias="title", property = "project.build.finalName", required = false)
    private String title;

    /**
     * Children files extension
     * @since 3.2.1
     */
    @Parameter(property = "wikiFilesExt", required = false, defaultValue=".wiki")
    private String wikiFilesExt;

    /**
    * The file encoding of the source files.
    *
    */
    @Parameter( property="encoding", defaultValue="${project.build.sourceEncoding}" )
    private String encoding;

    
    /**
     * Deploy State Manager - Store the last deployed state
     * 
     * @since 6.0.0
     */
    @Parameter
    protected DeployStateManager deployState /*= new DeployStateManager() */;
    
    /**
     *
     */
    public AbstractConfluenceMojo() {
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
    public String getEncoding() {
        return encoding;
    }

    /**
     *
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     *
     * @return
     */
    protected final Charset getCharset() {

        if( encoding == null ) {
            getLog().warn("encoding is null! default charset will be used");
            return Charset.defaultCharset();
        }

        try {
            Charset result = Charset.forName(encoding);
            return result;

        } catch (UnsupportedCharsetException e) {
            getLog().warn( String.format("encoding [%s] is not valid! default charset will be used", encoding));
            return Charset.defaultCharset();

        }
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

    public boolean isChildrenTitlesPrefixed() {
        return childrenTitlesPrefixed;
    }

    public List<String> getLabels() {

        if( labels==null ) {
            return Collections.emptyList();
        }
        return labels;
    }

    protected String getPrintableStringForResource( java.net.URI uri ) {
        
        try {
            Path p = Paths.get( uri );
            return getProject().getBasedir().toPath().relativize(p).toString();
            
        } catch (Exception e) {
            return uri.toString();
        }
        
        
    }

    /**
     * initialize properties shared with template
     */
    protected void initTemplateProperties( Site site ) {

        processProperties( site );

        getProperties().put("pageTitle", getTitle());
        getProperties().put("artifactId", project.getArtifactId());
        getProperties().put("version", project.getVersion());
        getProperties().put("groupId", project.getGroupId());
        getProperties().put("name", project.getName());
        getProperties().put("description", project.getDescription());

        final java.util.Properties projectProps = project.getProperties();

        if( projectProps!=null ) {

            for(Map.Entry<Object,Object> e : projectProps.entrySet()){
                getProperties().put( String.valueOf(e.getKey()), String.valueOf(e.getValue()) );
            }
        }

    }

    public void addStdProperties(MiniTemplator t) {
        java.util.Map<String, String> props = getProperties();

        if (props == null || props.isEmpty()) {
            getLog().info("no properties set!");
        } else {
            for (java.util.Map.Entry<String, String> e : props.entrySet()) {

                try {
                    t.setVariable(e.getKey(), e.getValue(), true /* isOptional */);
                } catch (VariableNotDefinedException e1) {
                    getLog().debug(String.format("variable %s not defined in template", e.getKey()));
                }
            }
        }

    }

    /**
     * 
     * @param pageToUpdate
     * @param site
     * @param source
     * @return
     */
    private <T extends Site.Page> CompletableFuture<Model.Page> updatePageContent(   
            final ConfluenceService confluence, 
            final Model.Page pageToUpdate, 
            final Site site, 
            final java.net.URI source,
            final T child,
            final String pageName ) 
    {
        
        return site.processPageUri(source, this.getTitle(), ( err, tuple2) -> {
              
            final CompletableFuture<Model.Page> result = 
                        new CompletableFuture<Model.Page>();
            
            if( err.isPresent() ) {
                result.completeExceptionally(RTE( "error processing uri [%s]", source, err.get() ));
                return result;
            }

            try {
                final MiniTemplator t = new MiniTemplator.Builder()
                        .setSkipUndefinedVars(true)
                        .build( tuple2.value1.get(), getCharset() );
                
                if( !child.isIgnoreVariables() ) {
                    
                    addStdProperties(t);
                    
                    t.setVariableOpt("childTitle", pageName);
                }
                
                return confluence.storePage(pageToUpdate, new Storage(t.generateOutput(), tuple2.value2) );
                
            } catch (Exception ex) {
                result.completeExceptionally(RTE("error storing page [%s]", pageToUpdate.getTitle(),ex));
            }
            
            return result;
        }) ;
        
    }
    
    /**
     * 
     * @param site
     * @param confluence
     * @param child
     * @param spaceKey
     * @param parentPageTitle
     * @param titlePrefix
     * @return
     */
    protected <T extends Site.Page> Model.Page  generateChild(  final Site site,
                                                                final ConfluenceService confluence,  
                                                                final T child, 
                                                                String spaceKey, 
                                                                String parentPageTitle, 
                                                                String titlePrefix) 
    {

        java.net.URI source = child.getUri(getFileExt());

        getLog().debug( String.format("generateChild\n\tspacekey=[%s]\n\tparentPageTitle=[%s]\n\t%s", 
                spaceKey, 
                parentPageTitle, 
                getPrintableStringForResource(source)));

        if (!isSnapshot() && isRemoveSnapshots()) {
            final String snapshot = titlePrefix.concat("-SNAPSHOT");

            confluence.getPage(spaceKey, parentPageTitle)
            .thenApply( p -> p.orElseThrow(() -> RTE("parentPage [%s] not found!", parentPageTitle) ))
            .thenCompose( page -> confluence.removePage(page, snapshot) )
            .thenAccept( deleted -> {
                getLog().info(String.format("Page [%s] has been removed!", snapshot));
            })             
            .exceptionally( ex -> throwRTE( "page [%s] not found!", snapshot, ex ))
            ;
                           
        }

        final String pageName = !isChildrenTitlesPrefixed()
            ? child.getName() : String.format("%s - %s", titlePrefix, child.getName());
            
        CompletableFuture<Model.Page> result =  
            confluence.getPage(spaceKey, parentPageTitle)
            .exceptionally( ex -> 
                throwRTE( "cannot find parent page [%s] in space [%s]", parentPageTitle, ex ))
            .thenApply( parent -> 
                parent.orElseThrow( () -> RTE( "cannot find parent page [%s] in space [%s]", parentPageTitle)) )
            .thenCombine( confluence.getPage(spaceKey, title), Tuple2::of)
            .thenCompose( tuple -> {
                return ( tuple.value2.isPresent() ) ?
                    CompletableFuture.completedFuture(tuple.value2.get()) :
                    confluence.createPage(tuple.value1, title);
            })
            .thenCompose( p -> 
                ( source != null ) ?
                        updatePageContent(confluence, p, site, source, child, pageName) :
                        confluence.storePage(p))
            ;
        
            try {
                Model.Page p =  result.get();
                
                for( String label : child.getComputedLabels() ) {

                    confluence.addLabelByName(label, Long.parseLong(p.getId()) );
                }

                child.setName( pageName );
                
                return p;
 
            } catch ( Exception e) {
                throw new RuntimeException(e);
            }
            

    }

    /**
     * Issue 46
     *
     **/
    private void processProperties( Site site ) {

        for( Map.Entry<String,String> e : this.getProperties().entrySet() ) {

            try {

                String v = e.getValue();
                if( v == null ) {
                    getLog().warn( String.format("property [%s] has null value!", e.getKey()));
                    continue;
                }
                final java.net.URI uri = new java.net.URI( v );

                if( uri.getScheme() == null ) {
                    continue;
                }

                getProperties().put( e.getKey(), processUriContent( site, uri, getCharset() ));

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
    private String processUriContent( Site site, java.net.URI uri, final Charset charset ) throws ProcessUriException {

        try {
            return  site.processUriContent(uri, this.getTitle(), (InputStream is, Representation r) -> {
                
                    try {
                        return AbstractConfluenceMojo.this.toString( is, charset );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
            }) ;

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
    private String toString(java.io.InputStream stream, Charset charset) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("stream");
        }

        try( final java.io.Reader r = new java.io.InputStreamReader(stream, charset) ) {

            final StringBuilder contents = new StringBuilder(4096);

            int c;
            while ((c = r.read()) != -1) {
                contents.append((char) c);
            }

            return contents.toString();

        }
    }

}
