/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.test.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author softphone
 */
@XmlRootElement
public class Site {
    
    Page home;

    public Page getHome() {
        return home;
    }

    public void setHome(Page home) {
        this.home = home;
    }
    
    
}
