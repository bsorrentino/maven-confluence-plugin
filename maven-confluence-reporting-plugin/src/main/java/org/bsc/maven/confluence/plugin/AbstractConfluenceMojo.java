package org.bsc.maven.confluence.plugin;

import java.io.File;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.maven.reporting.model.ProcessUriException;
import org.bsc.maven.reporting.model.Site;

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


    /**
     * initialize properties shared with template
     */
    protected void initTemplateProperties() {

        processProperties();

        getProperties().put("pageTitle", getTitle());
        //getProperties().put("parentPageTitle", getParentPageTitle());
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

    public void addStdProperties(MiniTemplator t) {
        java.util.Map<String, String> props = getProperties();

        if (props == null || props.isEmpty()) {
            getLog().info("no properties set!");
        } else {
            for (java.util.Map.Entry<String, String> e : props.entrySet()) {

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

            final String pageName = !isChildrenTitlesPrefixed()
                ? child.getName() : String.format("%s - %s", titlePrefix, child.getName());

            Page p = ConfluenceUtils.getOrCreatePage(confluence, spaceKey, parentPageTitle, pageName);

            if( source != null /*&& source.isFile() && source.exists() */) {

                final java.io.InputStream is = Site.processUri(source, this.getTitle()) ;

                final MiniTemplator t = new MiniTemplator.Builder()
                    .setSkipUndefinedVars(true)
                    .build( is, getCharset() );

                if( !child.isIgnoreVariables() ) {

                    addStdProperties(t);

                    t.setVariableOpt("childTitle", pageName);
                }


                p.setContent(t.generateOutput());
            }


            p = confluence.storePage(p);

            for( String label : child.getComputedLabels() ) {

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

    /**
     * Issue 46
     *
     **/
    private void processProperties() {

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

                getProperties().put( e.getKey(), processUri( uri, getCharset() ));

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
    private String processUri( java.net.URI uri, Charset charset ) throws ProcessUriException {

        try {
            final java.io.InputStream is = Site.processUri(uri, this.getTitle()) ;

            return toString( is, charset );
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

        java.io.Reader r = null;

        try {

            r = new java.io.InputStreamReader(stream, charset);

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
