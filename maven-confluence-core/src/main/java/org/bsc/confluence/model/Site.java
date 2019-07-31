/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import static java.lang.String.format;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import lombok.val;

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
        return Optional.ofNullable(spaceKey);
    }
    
    /**
     * @param spaceKey the spaceKey to set
     */
    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    /**
     * class Source
     */
    @XmlType(namespace = Site.NAMESPACE)
    protected static class Source {
        private static final Pattern lead_trail_spaces = Pattern.compile("^(.*)\\s+$|^\\s+(.*)");
        
        protected transient final Site site;

        public Source() {
            this.site = _SITE.peek();
        }

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

        public final Optional<String> optName() {
            return Optional.ofNullable(name);
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
    @XmlType(name = "page", namespace = Site.NAMESPACE)
    public static class Page extends Source {

        java.util.List<Attachment> attachments;

        @Deprecated
        public File getSource() {

            final java.net.URI _uri = super.getUri();
            if (null == _uri) {
                throw new IllegalStateException("uri is null");
            }

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

        /**
         * 
         * @param criteria
         * @return
         */
        public Optional<Page> findPage( Predicate<Page> criteria ) {
            if( criteria.test(this) ) return Optional.of(this); 
            
            for( Page child : getChildren() ) {
                
                val result = child.findPage(criteria);
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
        @XmlAttribute( name="parent-page")
        public String getParentPageTitle() {
            return parentPageTitle;
        }

        /**
         * 
         * @return
         */
        public final Optional<String> optParentPageTitle() {
            return Optional.ofNullable(parentPageTitle);
        }
        
        /**
         * @param parentPageTitle the parentPageTitle to set
         */
        public void setParentPageTitle(String parentPageTitle) {
            this.parentPageTitle = parentPageTitle;
        }
        
        private String parentPageId;

        /**
         * @return the parentPageTitle
         */
        @XmlAttribute( name="parent-page-id")
        public String getParentPageId() {
            return parentPageId;
        }

        /**
         * 
         * @return
         */
        public final Optional<String> optParentPageId() {
            return Optional.ofNullable(parentPageId);
        }
        
        /**
         * @param parentPageTitle the parentPageTitle to set
         */
        public void setParentPageId(String parentPageId) {
            this.parentPageId = parentPageId;
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

    Home home;

    @XmlElement(name = "home", required = true)
    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    
}
