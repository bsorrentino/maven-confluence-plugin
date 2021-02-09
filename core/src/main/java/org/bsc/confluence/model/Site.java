/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import javax.xml.bind.annotation.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 *
 * @author bsorrentino
 */
@SuppressWarnings("JavadocReference")
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

    private String spaceKey;
    
    @XmlAttribute(name="space-key")
    public final String getSpaceKey() {
       return spaceKey; 
    }

    /**
     * 
     * @return
     */
    public final Optional<String> optSpaceKey() {
        return ofNullable(spaceKey);
    }
    
    /**
     * @param spaceKey the spaceKey to set
     */
    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    /**
     * Attribute basedir
     */
    private transient Optional<Path> _basedir = Optional.empty();
    /**
     *
     * @param basedir
     */
    @XmlTransient
    public void setBasedir( Path basedir ) {
        
        this._basedir = ofNullable(basedir).map( (p) -> Files.isDirectory(p) ?
                Paths.get(p.toString()) :
                Paths.get(p.getParent().toString()));
           
    }
    /**
     *
      * @return
     */
    public Path getBasedir() {
        return _basedir.orElseThrow( () -> new IllegalStateException("basedir is not set!"));
    }
    /**
     * default document extension
     *
     * Attribute defaultExt
     */
    private transient Optional<String> _defaultFileExt = Optional.empty();
    /**
     *
     * @param basedir
     */
    @XmlTransient
    public void setDefaultFileExt( String fileExt ) {
        this._defaultFileExt = ofNullable(fileExt);
    }
    /**
     *
     * @return
     */
    public Optional<String> optDefaultFileExt() {
        return _defaultFileExt;
    }

    @XmlAttribute(name="label")
    public java.util.List<String> labels = new ArrayList<>();

    //@XmlElement(name = "label")
    public java.util.List<String> getLabels() {
        return labels;
    }

    /**
     * need for XmlMapper
     * @see https://stackoverflow.com/a/40839029/521197
     *
     * @param label
     */
    public void setLabel( String label) {
        this.labels.add(label);
    }

    Home home;

    @XmlElement(name = "home", required = true)
    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // TYPE(s)
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * class Source
     */
    @XmlType(namespace = Site.NAMESPACE)
    public static class Source {
        private static final Pattern lead_trail_spaces = Pattern.compile("^(.*)\\s+$|^\\s+(.*)");

        protected transient final Site site;

        public Source() {
            this.site = _SITE.peek();
        }

        /**
         * attribute URI
         */
        private java.net.URI uri;
        /**
         *
         * @return
         */
        @XmlAttribute
        public java.net.URI getUri() {
            if (uri != null && !uri.isAbsolute()) {
                return site.getBasedir().toUri().resolve(uri);
            }
            return uri;
        }
        /**
         *
         * @param value
         */
        public final void setUri(java.net.URI value) {
            if (null == value) {
                throw new IllegalArgumentException("uri is null");
            }
            this.uri = value;
        }

        public java.net.URI getRelativeUri() {
            return site.getBasedir().toUri().relativize(getUri());
        }

        String name;

        @XmlAttribute
        public String getName() {
            return name;
        }

        public final Optional<String> optName() {
            return ofNullable(name);
        }

        public void setName(String name) {
            if( lead_trail_spaces.matcher(name).matches() )
                throw new IllegalArgumentException(format("name [%s] is not valid!", name));
            this.name = name;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ": " + getName() + " - " + String.valueOf(getUri());
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

        public Attachment() {

            this.contentType = DEFAULT_CONTENT_TYPE;
            this.version = DEFAULT_VERSION;
        }

    }

    /**
     * class Page
     */
    @SuppressWarnings("JavadocReference")
    @XmlType(name = "page", namespace = Site.NAMESPACE)
    public static class Page extends Source {

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

        @XmlAttribute(name="label")
        public java.util.List<String> labels = new ArrayList<>();

        //@XmlElement(name = "label")
        public java.util.List<String> getLabels() {
            return labels;
        }

        /**
         * need for XmlMapper
         * @see https://stackoverflow.com/a/40839029/521197
         *
         * @param label
         */
        public void setLabel( String label) {
            this.labels.add(label);
        }

        @XmlAttribute(name="child")
        public java.util.List<Page> children = ChildListProxy.newInstance(this);

        //@XmlElement(name = "child")
        public java.util.List<Page> getChildren() {
            return children;
        }

        /**
         * need for XmlMapper
         *
         * @see https://stackoverflow.com/a/40839029/521197
         * @param child
         */
        public void setChild( Page child ) {
            children.add( child );
        }

        @XmlAttribute(name="attachment")
        public java.util.List<Attachment> attachments = new ArrayList<>();

        //@XmlElement(name = "attachment")
        public List<Attachment> getAttachments() {
            return attachments;
        }

        /**
         * need for XmlMapper
         *
         * @see https://stackoverflow.com/a/40839029/521197
         * @param attachment
         */
        public void setAttachment( Attachment attachment ) {
            attachments.add( attachment );
        }

        @Override
        public java.net.URI getUri() {

            return ofNullable(super.getUri()).orElseGet( () ->  {
                if (getName() == null) throw new IllegalStateException("name is null");
                final String pageName = site.optDefaultFileExt().map( ext -> getName().concat(ext) ).orElse( getName() );
                final java.net.URI pageUri = site.getBasedir().toUri().resolve( pageName );
                setUri( pageUri );
                return pageUri;
            });
        }

        boolean ignoreVariables = false;

        public Boolean isIgnoreVariables() {
            return ignoreVariables;
        }

        /**
         * this attribute name containing dash is not supported in yaml format
         */
        @XmlAttribute(name = "ignore-variables")
        @Deprecated
        public void setIgnoreVariablesDeprecated(Boolean value) {
            this.ignoreVariables = ofNullable(value).orElse( false );
        }

        @XmlAttribute(name = "ignoreVariables")
        public void setIgnoreVariables(Boolean value) {
            this.ignoreVariables = ofNullable(value).orElse( false );
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

        /**
         *
         * @param criteria
         * @return
         */
        public Optional<Page> findPage( Predicate<Page> criteria ) {
            if( criteria.test(this) ) return Optional.of(this);

            for( Page child : getChildren() ) {

                final Optional<Page> result = child.findPage(criteria);
                if( result.isPresent() ) {
                    return result;
                }
            }

            return Optional.empty();
        }
    }

    @XmlType(name = "home", namespace = Site.NAMESPACE)
    public static class Home extends Page {
        private String parentPageTitle;

        /**
         * @return the parentPageTitle
         */
        public String getParentPageTitle() {
            return parentPageTitle;
        }

        /**
         * this attribute name containing dash is not supported in yaml format
         *
         * @param parentPageTitle the parentPageTitle to set
         */
        @Deprecated
        @XmlAttribute( name="parent-page")
        public void setParentPageTitleDeperecated(String parentPageTitle) {
            this.parentPageTitle = parentPageTitle;
        }

        /**
         * @param parentPageTitle the parentPageTitle to set
         */
        @XmlAttribute( name="parentPage")
        public void setParentPageTitle(String parentPageTitle) {
            this.parentPageTitle = parentPageTitle;
        }

        /**
         *
         * @return
         */
        public final Optional<String> optParentPageTitle() {
            return ofNullable(parentPageTitle);
        }


        private String parentPageId;

        /**
         * @return the parentPageTitle
         */
        public String getParentPageId() {
            return parentPageId;
        }
        /**
         * this attribute name containing dash is not supported in yaml format

         * @param parentPageId the parentPageTitle to set
         */
        @XmlAttribute( name="parent-page-id")
        public void setParentPageIdDeprecated(String parentPageId) {
            this.parentPageId = parentPageId;
        }
        /**
         * @param parentPageId the parentPageTitle to set
         */
        @XmlAttribute( name="parentPageId")
        public void setParentPageId(String parentPageId) {
            this.parentPageId = parentPageId;
        }

        /**
         *
         * @return
         */
        public final Optional<String> optParentPageId() {
            return ofNullable(parentPageId);
        }




    }

}
