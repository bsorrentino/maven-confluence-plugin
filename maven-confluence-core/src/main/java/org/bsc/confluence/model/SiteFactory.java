/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
    
    public Site createFromFolder();
    
    public Site createFromModel();
}
