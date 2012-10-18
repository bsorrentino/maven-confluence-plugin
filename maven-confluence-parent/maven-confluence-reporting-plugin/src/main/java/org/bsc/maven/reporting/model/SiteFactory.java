/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.model;

/**
 *
 * @author softphone
 */
public interface SiteFactory {
    
    public Site createFromFolder();
    
    public Site createFromModel();
}
