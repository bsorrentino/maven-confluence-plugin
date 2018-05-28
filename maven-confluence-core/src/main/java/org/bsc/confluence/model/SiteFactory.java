/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import java.util.Objects;

import org.bsc.confluence.DeployStateManager;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
    
    public Site createFromFolder( final DeployStateManager dsm );
    
    public Site createFromModel( final DeployStateManager dsm );
}
