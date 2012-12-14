/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.model;

import edu.emory.mathcs.backport.java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author softphone
 */
@XmlRootElement( name="site", namespace = "http://code.google.com/p/maven-confluence-plugin")
public class Site {

    /**
     * 
     */
    protected static final java.util.Stack<Site> _SITE = new java.util.Stack<Site>();
    
    /**
     * 
     * @param uri
     * @return
     * @throws Exception 
     */
    public static java.io.Reader processUri( java.net.URI uri ) throws /*ProcessUri*/Exception {
            String scheme = uri.getScheme();

            if (scheme == null) {
                throw new /*ProcessUri*/Exception("uri is invalid!");
            }

            String source = uri.getRawSchemeSpecificPart();

            java.io.Reader result = null;

            if ("classpath".equalsIgnoreCase(scheme)) {
                java.io.InputStream is = null;
                ClassLoader cl = Thread.currentThread().getContextClassLoader();

                is = cl.getResourceAsStream(source);

                if (is == null) {
                    //getLog().warn(String.format("resource [%s] doesn't exist in context classloader", source));

                    cl = Site.class.getClassLoader();

                    is = cl.getResourceAsStream(source);

                    if (is == null) {
                        throw new /*ProcessUri*/Exception(String.format("resource [%s] doesn't exist in classloader", source));
                    }

                }

                result = new java.io.InputStreamReader(is);

            } else {

                try {
                    java.net.URL url = uri.toURL();


                    result = new java.io.InputStreamReader(url.openStream());

                } catch (IOException e) {
                    throw new /*ProcessUri*/Exception(String.format("error opening url [%s]!", source), e);
                } catch (Exception e) {
                    throw new /*ProcessUri*/Exception(String.format("url [%s] is not valid!", source), e);
                }
            }

            return result;
    }

    /**
     * class Source
     */
    protected static class Source {
        
        private transient final Site site;
        
        java.net.URI uri;

        @XmlAttribute
        public final java.net.URI getUri() {
            return uri;
        }

        public final void setUri(java.net.URI value) {
            if (null == value) {
                throw new IllegalArgumentException("uri is null");
            }
            if (!value.isAbsolute()) {
                throw new IllegalArgumentException("uri is absolute");
            }
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
        
        @XmlTransient
        public java.util.List<String> getLabels() {
            return (site!=null) ? site.getLabels() : Collections.emptyList();
        }
        
        public Source() {
            this.site = _SITE.peek();
        }

        public java.io.Reader getContentAsStream() throws /*ProcessUri*/Exception {
            return Site.processUri( uri );
        }
        
        @Override
        public String toString() {
            return new StringBuilder()
                    .append( getClass().getSimpleName())
                    .append(": ")
                    .append( getName() )
                    .append( " - ")
                    .append( String.valueOf( getUri()))
                    .toString();
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

        public boolean hasBeenUpdatedFrom( java.util.Date date) {
            if( date != null ) {
                
                validateSource();

                if ("file".equals(uri.getScheme())) {
                    java.io.File f = new java.io.File(uri);
            
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
    public static class Page extends Source {

        
        java.util.List<Page> children;
        java.util.List<Attachment> attachments;
        String name;

        @Deprecated
        public File getSource() {
            validateSource();

            if (!"file".equals(uri.getScheme())) {
                throw new IllegalArgumentException("uri not represent a file");
            }

            return new java.io.File(uri);
        }

        @XmlElement(name = "child")
        public java.util.List<Page> getChildren() {

            if (null == children) {
                synchronized (this) {
                    children = new java.util.ArrayList<Page>();
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

        
        public java.net.URI getUri(MavenProject project, String ext) {

            if (getUri() == null) {
                if (project == null) {
                    throw new IllegalArgumentException("project is null");
                }
                if (getName() == null) {
                    throw new IllegalStateException("name is null");
                }

                final String path = String.format("src/site/confluence/%s.%s", getName(), ext);

                setUri(new java.io.File(project.getBasedir(), path).toURI());
            }

            return getUri();
        }
    }

    public Site() {  
        _SITE.push(this);
    }
    
    
    java.util.List<String> labels;

    @XmlElement(name="label")
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

    public Page getHome() {
        return home;
    }

    public void setHome(Page home) {
        this.home = home;
    }

   private void printSource( PrintStream out, int level, char c, final Source source ) {
       for( int i=0; i <level; ++i ) {
           System.out.print(c);
       }
       out.print( " " );
       out.println( source );
   } 

   private void printChildren( PrintStream out, int level, Page parent ) {
        printSource( out, level, '-', parent );
        
        for( Attachment attach : parent.getAttachments() ) {
            
            printSource( out, level+1, '#', attach );
                     
        }
        for( Page child : parent.getChildren() ) {
            
            printChildren( out, level+1, child );
                     
        }
   } 
    
    public void print( PrintStream out ) {

        out.println( "Site" );

        if( !getLabels().isEmpty() ) {
            out.println(" Labels");
            for( String label : getLabels() ) {

                out.printf( "  %s\n", label );

            }
        }
        
        printChildren( out, 0, getHome() );
        
    }
}
