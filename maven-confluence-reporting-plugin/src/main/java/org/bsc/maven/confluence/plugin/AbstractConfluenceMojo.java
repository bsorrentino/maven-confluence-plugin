package org.bsc.maven.confluence.plugin;

import static java.util.concurrent.CompletableFuture.completedFuture;

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
     * Exprimental feature
     * Deploy State Manager - Store the last deployed state
     *
     * If declared a local file will be generated that stores the last update of all documents involved in publication.
     * If such file is present during every publication the plugin will check if the last update of each document has changed and skip the document if no update is detected
     *
     * <pre>
     *   &lt;deployState&gt;
     *     &lt;active&gt; true|false &lt;/active&gt; ==> default: true
     *     &lt;outdir&gt; target dir &lt;/outdir&gt;  ==> default: ${project.build.directory}
     *   &lt;/deployState&gt;
     * </pre>
     *
     *
     * @since 6.0.0
     */
    @Parameter
    protected DeployStateManager.Parameters deployState;

    /**
     *
     */
    protected DeployStateManager deployStateManager;

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
            getLog().debug("encoding is null! default charset will be used");
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
     * @param parentPage
     * @return
     */
    protected <T extends Site.Page> Model.Page  generateChild(  final ConfluenceService confluence,
                                                                final Site site,                                                           
                                                                final T child,
                                                                final Model.Page parentPage)
    {

        final String homeTitle = site.getHome().getName();
        
        final java.net.URI source = child.getUri(getFileExt());

        getLog().debug( String.format("generateChild\n\tspacekey=[%s]\n\thome=[%s]\n\tparent=[%s]\n\tpage=[%s]\n\t%s",
                parentPage.getSpace(),
                homeTitle,
                parentPage.getTitle(),
                child.getName(),
                getPrintableStringForResource(source)));

        final String pageTitle = !isChildrenTitlesPrefixed()
                ? child.getName() : String.format("%s - %s", homeTitle, child.getName());

        if (!isSnapshot() && isRemoveSnapshots()) {
            final String snapshot = pageTitle.concat("-SNAPSHOT");

            confluence.removePage(parentPage, snapshot)
            .thenAccept( deleted -> {
                getLog().info(String.format("Page [%s] has been removed!", snapshot));
            })
            .exceptionally( ex -> throwRTE( "page [%s] not found!", snapshot, ex ))
            ;

        }

        final Model.Page result =
            confluence.getPage(parentPage.getSpace(), pageTitle)
            .thenCompose( page -> {
                return ( page.isPresent() ) ?
                    completedFuture(page.get()) :
                    resetUpdateStatusForResource(source)
                        .thenCompose( reset -> confluence.createPage(parentPage, pageTitle));
            })
            .thenCompose( p ->
                canProceedToUpdateResource(source)
                    .thenCompose( update -> {
                        if(update) return updatePageContent(confluence, p, site, source, child, pageTitle);
                        else {
                            getLog().info( String.format("page [%s] has not been updated (deploy skipped)",
                                    getPrintableStringForResource(source) ));
                            return confluence.storePage(p);
                        }}))
            .join();

            try {

                for( String label : child.getComputedLabels() ) {

                    confluence.addLabelByName(label, Long.parseLong(result.getId()) );
                }

                child.setName( pageTitle );

                return result;

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

    protected CompletableFuture<Void> resetUpdateStatusForResource( java.net.URI uri) {
        if( uri != null && deployStateManager != null )
            deployStateManager.resetState(uri);
        return CompletableFuture.completedFuture(null);
    }

    /**
     *
     * @param uri
     * @return
     */
    protected CompletableFuture<Boolean> canProceedToUpdateResource( java.net.URI uri) {
        if( uri == null )
            return CompletableFuture.completedFuture(false);
        if( deployStateManager == null )
            return CompletableFuture.completedFuture(true);

        return CompletableFuture.completedFuture(deployStateManager.isUpdated(uri));
    }
}
