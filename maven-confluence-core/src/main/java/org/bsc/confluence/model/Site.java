/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.functional.Tuple2;
import org.bsc.markdown.ToConfluenceSerializer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

/**
 *
 * @author bsorrentino
 */
@XmlRootElement(name = "site", namespace = Site.NAMESPACE)
public class Site {

    public static final String NAMESPACE = "https://github.com/bsorrentino/maven-confluence-plugin";
    /**
     *
     */
    protected static final java.util.Stack<Site> _SITE = new java.util.Stack<Site>();

    /**
     * 
     */
    public Site() {
        _SITE.push(this);
    }

    /**
     * 
     * @param is
     * @return
     */
    static java.io.InputStream processMarkdown(final java.io.InputStream is, final String homePageTitle)
            throws IOException {

        final char[] contents = IOUtils.toCharArray(is);

        final PegDownProcessor p = new PegDownProcessor(ToConfluenceSerializer.extensions());

        final RootNode root = p.parseMarkdown(contents);

        ToConfluenceSerializer ser = new ToConfluenceSerializer() {

            @Override
            protected void notImplementedYet(Node node) {

                final int lc[] = ToConfluenceSerializer.lineAndColFromNode(new String(contents), node);
                throw new UnsupportedOperationException(String.format("Node [%s] not supported yet. line=[%d] col=[%d]",
                        node.getClass().getSimpleName(), lc[0], lc[1]));
            }

            @Override
            protected String getHomePageTitle() {
                return homePageTitle;
            }

        };

        root.accept(ser);

        return new java.io.ByteArrayInputStream(ser.toString().getBytes());
    }

    /**
     *
     * @param uri
     * @return
     * @throws Exception
     */
    public <T> T processUriContent(final java.net.URI uri, final String homePageTitle,
            final BiFunction<java.io.InputStream, Storage.Representation, T> onSuccess)
            throws /* ProcessUri */Exception {
        Objects.requireNonNull(uri, "uri is null!");

        String scheme = uri.getScheme();

        Objects.requireNonNull(scheme, String.format("uri [%s] is invalid!", String.valueOf(uri)));

        final String source = uri.getRawSchemeSpecificPart();

        final String path = uri.getRawPath();

        final boolean isMarkdown = (path != null && path.endsWith(".md"));
        final boolean isStorage = (path != null && (path.endsWith(".xml") || path.endsWith(".xhtml")));

        final Storage.Representation representation = (isStorage) ? Storage.Representation.STORAGE
                : Storage.Representation.WIKI;

        java.io.InputStream result = null;

        if ("classpath".equalsIgnoreCase(scheme)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            result = cl.getResourceAsStream(source);

            if (result == null) {
                // getLog().warn(String.format("resource [%s] doesn't exist in context
                // classloader", source));

                cl = Site.class.getClassLoader();

                final java.io.InputStream is = cl.getResourceAsStream(source);

                result = (isMarkdown) ? processMarkdown(is, homePageTitle) : is;

                if (result == null) {
                    throw new Exception(String.format("resource [%s] doesn't exist in classloader", source));
                }

            }

        } else {

            try {

                java.net.URL url = uri.toURL();

                final java.io.InputStream is = url.openStream();

                result = (isMarkdown) ? processMarkdown(is, homePageTitle) : is;

            } catch (IOException e) {
                throw new Exception(String.format("error opening url [%s]!", source), e);
            }
        }

        return onSuccess.apply(result, representation);
    }

    /**
     * 
     * @param uri
     * @param onSuccess
     * @return
     */
    public <T> T processUri(final java.net.URI uri, java.util.function.BiFunction<Optional<Exception>,Optional<java.io.InputStream>, T> callback) {
        Objects.requireNonNull(uri, "uri is null!");
        Objects.requireNonNull(callback, "callback is null!");

        final String scheme = uri.getScheme();

        Objects.requireNonNull(scheme, String.format("uri [%s] is invalid!", String.valueOf(uri)));
        
        final String source = uri.getRawSchemeSpecificPart();

        java.io.InputStream result = null;

        if ("classpath".equalsIgnoreCase(scheme)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            result = cl.getResourceAsStream(source);

            if (result == null) {

                cl = Site.class.getClassLoader();

                result = cl.getResourceAsStream(source);

                final Exception ex = new Exception(String.format("resource [%s] doesn't exist in classloader", source));
                return callback.apply( Optional.of(ex), Optional.empty());

            }

        } else {

            try {
                
                java.net.URL url = uri.toURL();

                result = url.openStream();

            } catch (IOException e) {
                final Exception ex = new Exception(String.format("error opening url [%s]!", source), e);
                return callback.apply( Optional.of(ex), Optional.empty());
            }
        }

        return callback.apply( Optional.empty(), Optional.of(result));
    }

    /**
     *
     * @param uri
     * @return
     * @throws Exception
     */
    public <T> T processPageUri(final java.net.URI uri, final String homePageTitle,
            final BiFunction<Optional<Exception>, Tuple2<Optional<java.io.InputStream>, Storage.Representation>, T> callback)
    {
        Objects.requireNonNull(uri, "uri is null!");

        String scheme = uri.getScheme();

        Objects.requireNonNull(scheme, String.format("uri [%s] is invalid!", String.valueOf(uri)));

        final String source = uri.getRawSchemeSpecificPart();

        final String path = uri.getRawPath();

        final boolean isMarkdown = (path != null && path.endsWith(".md"));
        final boolean isStorage = (path != null && (path.endsWith(".xml") || path.endsWith(".xhtml")));

        final Storage.Representation representation = (isStorage) ? Storage.Representation.STORAGE
                : Storage.Representation.WIKI;

        java.io.InputStream result = null;

        if ("classpath".equalsIgnoreCase(scheme)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            result = cl.getResourceAsStream(source);

            if (result == null) {
                // getLog().warn(String.format("resource [%s] doesn't exist in context
                // classloader", source));

                cl = Site.class.getClassLoader();

                final java.io.InputStream is = cl.getResourceAsStream(source);

                try {
                    result = (isMarkdown) ? processMarkdown(is, homePageTitle) : is;
                    if (result == null) {
                        final Exception ex = new Exception(String.format("page [%s] doesn't exist in classloader", source));
                        return callback.apply( Optional.of(ex), Tuple2.of(Optional.empty(), representation) );
                    }
                } catch (IOException e) {
                    final Exception ex = new Exception(String.format("error processing markdown for page [%s] ", source));
                    return callback.apply( Optional.of(ex), Tuple2.of(Optional.empty(), representation) );
                }


            }

        } else {

            try {

                java.net.URL url = uri.toURL();

                final java.io.InputStream is = url.openStream();

                result = (isMarkdown) ? processMarkdown(is, homePageTitle) : is;

            } catch (IOException e) {
                final Exception ex = new Exception(String.format("error opening/processing page [%s]!", source), e);
                return callback.apply( Optional.of(ex), Tuple2.of(Optional.empty(), representation) );
            }
        }

        return callback.apply( Optional.empty(), Tuple2.of(Optional.of(result), representation));
    }

    /**
     * class Source
     */
    @XmlType(namespace = Site.NAMESPACE)
    protected static class Source {

        protected transient final Site site;

        private java.net.URI uri;

        @XmlAttribute
        public final java.net.URI getUri() {
            if (uri != null && !uri.isAbsolute()) {
                return site.getBasedir().toUri().resolve(uri);
            }
            return uri;
        }

        public final void setUri(java.net.URI value) {
            if (null == value) {
                throw new IllegalArgumentException("uri is null");
            }
            // if (!value.isAbsolute()) {
            // throw new IllegalArgumentException("uri is not absolute!");
            // }
            this.uri = value;
        }

        String name;

        @XmlAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Source() {
            this.site = _SITE.peek();
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ": " + getName() + " - " + String.valueOf(getUri());
        }

        protected void validateSource() {
            if (null == uri) {
                throw new IllegalStateException("uri is null");
            }
        }
    }

    /**
     * class Attachment
     */
    @XmlType(name = "attachment", namespace = Site.NAMESPACE)
    public static class Attachment extends Source {
        public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
        public static final String DEFAULT_VERSION = "0";

        String contentType;

        @XmlAttribute
        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        String comment;

        @XmlAttribute
        public String getComment() {
            if (comment == null) {
                if (getName() == null) {
                    setComment("attached by maven-confluence-plugin");
                } else {
                    setComment(String.format("%s - attached by maven-confluence-plugin", getName()));
                }
            }
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        String version;

        @XmlAttribute
        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public boolean hasBeenUpdatedFrom(java.util.Date date) {
            if (date != null) {

                validateSource();

                final java.net.URI _uri = super.getUri();

                if (!_uri.isAbsolute() || "file".equals(_uri.getScheme())) {
                    java.io.File f = new java.io.File(_uri);

                    return f.lastModified() > date.getTime();
                }
            }

            return true;
        }

        public Attachment() {

            this.contentType = DEFAULT_CONTENT_TYPE;
            this.version = DEFAULT_VERSION;
        }
    }

    /**
     * class Page
     */
    @XmlType(name = "page", namespace = Site.NAMESPACE)
    public static class Page extends Source {

        java.util.List<Attachment> attachments;

        @Deprecated
        public File getSource() {
            validateSource();

            final java.net.URI _uri = super.getUri();

            if (!_uri.isAbsolute() && !"file".equals(_uri.getScheme())) {
                throw new IllegalArgumentException("uri not represent a file");
            }

            return new java.io.File(_uri);
        }

        private java.util.List<String> labels;

        @XmlElement(name = "label")
        public java.util.List<String> getLabels() {
            if (null == labels) {
                synchronized (this) {
                    labels = new java.util.ArrayList<String>();
                }
            }
            return labels;
        }

        public void setLabels(java.util.List<String> labels) {
            this.labels = labels;
        }

        private Page parent;

        @XmlTransient
        public final void setParent(Page p) {
            parent = p;
        }
        
        public final Page getParent() {
            return parent;
        }
        
        @XmlTransient
        public final java.util.List<String> getComputedLabels() {

            if (site != null) {

                java.util.List<String> _labels = site.getLabels();

                if (_labels != null && !_labels.isEmpty()) {

                    _labels = new java.util.ArrayList<String>(_labels);
                    _labels.addAll(getLabels());

                    return _labels;
                }

            }

            return getLabels();
        }

        java.util.List<Page> children;

        @XmlElement(name = "child")
        public java.util.List<Page> getChildren() {

            if (null == children) {
                synchronized (this) {
                    children = ChildListProxy.newInstance(this);
                    /* children = new java.util.ArrayList<Page>(); */
                }
            }
            return children;
        }

        @XmlElement(name = "attachment")
        public List<Attachment> getAttachments() {
            if (null == attachments) {
                synchronized (this) {
                    attachments = new java.util.ArrayList<Attachment>();
                }
            }
            return attachments;
        }

        public java.net.URI getUri(String ext) {

            if (getUri() == null) {
                if (getName() == null) {
                    throw new IllegalStateException("name is null");
                }

                setUri(site.getBasedir().toUri().resolve(getName().concat(ext)));

            }

            return getUri();
        }

        boolean ignoreVariables = false;

        @XmlAttribute(name = "ignore-variables")
        public Boolean isIgnoreVariables() {
            return ignoreVariables;
        }

        public void setIgnoreVariables(Boolean value) {
            this.ignoreVariables = Optional.ofNullable(value).orElse( false );
        }

        @XmlElement(name = "generated")
        protected List<Generated> generateds;

        /**
         * Gets the value of the generateds property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot.
         * Therefore any modification you make to the returned list will be present
         * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
         * for the generateds property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getGenerateds().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list {@link Generated }
         *
         *
         */
        public List<Page.Generated> getGenerateds() {
            if (generateds == null) {
                generateds = new ArrayList<Generated>();
            }
            return this.generateds;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Generated {

            @XmlAttribute(name = "ref")
            protected String ref;

            /**
             * Obtient la valeur de la propriété ref.
             *
             * @return possible object is {@link String }
             *
             */
            public String getRef() {
                return ref;
            }

            /**
             * Définit la valeur de la propriété ref.
             *
             * @param value
             *            allowed object is {@link String }
             *
             */
            public void setRef(String value) {
                this.ref = value;
            }

        }

    }
    
    private transient Optional<Path> _basedir;
    
    @XmlTransient
    public void setBasedir( Path basedir ) {
        
        this._basedir = Optional.ofNullable(basedir).map( (p) -> Files.isDirectory(p) ?
                Paths.get(p.toString()) :
                Paths.get(p.getParent().toString()));
           
    }
     
    public Path getBasedir() {
        return _basedir.orElseThrow( () -> new IllegalStateException("basedir is not set!"));
    }

    private java.util.List<String> labels;

    @XmlElement(name = "label")
    public java.util.List<String> getLabels() {
        if (null == labels) {
            synchronized (this) {
                labels = new java.util.ArrayList<String>();
            }
        }
        return labels;
    }

    public void setLabels(java.util.List<String> labels) {
        this.labels = labels;
    }

    Page home;

    @XmlElement(name = "home", required = true)
    public Page getHome() {
        return home;
    }

    public void setHome(Page home) {
        this.home = home;
    }

    
    private String getPrintableStringForResource( final Source source ) {
        return getPrintableStringForResource(source.getUri());
    }
    
    private String getPrintableStringForResource( final java.net.URI uri ) {
        
        try {
            Path p = Paths.get( uri );
            return getBasedir().relativize(p).toString();
            
        } catch (Exception e) {
            return uri.toString();
        }
        
    }

    private void printSource(PrintStream out, int level, char c, final Source source) {
        for (int i = 0; i < level; ++i) {
            System.out.print(c);
        }
        out.print(" ");
        out.println(getPrintableStringForResource(source));
        
        //out.println();
        //out.print( state.getBasedir() ); out.print( " - " ); out.print( source );
        //out.println();
    }

    private void printChildren(PrintStream out, int level, Page parent) {
        printSource(out, level, '-', parent);

        for (Attachment attach : parent.getAttachments()) {

            printSource(out, level + 1, '#', attach);

        }
        for (Page child : parent.getChildren()) {

            printChildren(out, level + 1, child);

        }
    }

    public void print(PrintStream out) {

        out.println("Site");

        if (!getLabels().isEmpty()) {
            out.println(" Labels");
            for (String label : getLabels()) {

                out.printf("  %s\n", label);

            }
        }

        printChildren(out, 0, getHome());

    }
}
