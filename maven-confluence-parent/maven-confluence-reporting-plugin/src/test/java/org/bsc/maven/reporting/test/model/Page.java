/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.test.model;

import java.io.File;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author softphone
 */
public class Page {
    java.util.List<Page> children;
    java.util.List<Attachment> attachments;
    
    java.io.File file;

    @XmlAttribute
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    
    
    @XmlElement(name="child")
    public java.util.List<Page> getChildren() {
        
        if( null==children ) {
            synchronized(this){
                children = new java.util.ArrayList<Page>();
            }
        }
        return children;
    }

    @XmlElement(name="attachment")
    public List<Attachment> getAttachments() {
        if( null==attachments ) {
            synchronized(this){
                attachments = new java.util.ArrayList<Attachment>();
            }
        }
        return attachments;
    }
    
    
}
