/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.model;

import java.io.File;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author softphone
 */
@XmlRootElement
public class Site {

    public static class Attachment {

    }
    
    public static class Page {

        java.util.List<Page> children;
        java.util.List<Attachment> attachments;
        java.net.URI uri;
        String name;
        
        @XmlAttribute
        public final java.net.URI getUri() {
            return uri;
        }

        public final void setUri( java.net.URI value) {
            if( null == value ) throw new IllegalArgumentException("uri is null");
            if( !value.isAbsolute() ) throw new IllegalArgumentException("uri is absolute");
            this.uri = value;
        }

        public File getSource() {
            if( null == uri ) throw new IllegalStateException("uri is null");
            if( !"file".equals(uri.getScheme()) ) throw new IllegalArgumentException("uri not represent a file");
            
            return new java.io.File(uri);
        }

        @XmlAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    
        public java.net.URI getUri( MavenProject project, String ext) {
             
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

    
    Page home;

    public Page getHome() {
        return home;
    }

    public void setHome(Page home) {
        this.home = home;
    }
    
    
}
