/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.confluence.plugin;

import static java.lang.String.format;

import java.util.Optional;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Model.PageSummary;

import lombok.val;

/**
 *
 * Delete a confluence pageTitle 
 * 
 * @author bsorrentino
 * @since 3.4.0
 */
@Mojo( name="delete", threadSafe = true, requiresProject = false  )
public class ConfluenceDeleteMojo extends AbstractBaseConfluenceSiteMojo {

    /**
     * perform recursive deletion 
     * 
     * @since 3.4.0
     */
    @Parameter(property = "recursive", defaultValue = "true")
    private boolean recursive;

    private Model.PageSummary getStartPage(ConfluenceService confluence, Model.Page parentPage) throws Exception {
        
        final String startPageTitle;
        if( isSiteDescriptorValid() ) {
            val site = createSiteFromModel();
            
            startPageTitle = site.getHome().getName();
        }
        else {
            startPageTitle = getPageTitle();
        }
             
        return confluence.findPageByTitle(parentPage.getId(), startPageTitle);
    }
    
    private void deletePage(ConfluenceService confluence) throws Exception {
        val parentPage = loadParentPage(confluence, Optional.empty());

        if( parentPage==null ) {
            getLog().warn("Parent page not found!");                    
            return;
        }

        val start = getStartPage( confluence, parentPage);
        
        if( start==null ) {
            getLog().warn(format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(),getPageTitle(), parentPage.getSpace()));                    
            return;
        }

        if( recursive ) {
            val descendents = confluence.getDescendents(start.getId());

            if( descendents==null || descendents.isEmpty() ) {
                getLog().warn(format("Page [%s]/[%s] in [%s] has not descendents!", parentPage.getTitle(),getPageTitle(), parentPage.getSpace()));                    
            }
            else {

                for( PageSummary descendent : descendents) {

                    getLog().info( format("Page [%s]/[%s]/[%s]  has been removed!", parentPage.getTitle(),getPageTitle(), descendent.getTitle()) );
                    confluence.removePage(descendent.getId());

                }
            }
        }

        confluence.removePage(start.getId());

        getLog().info(format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),getPageTitle(), parentPage.getSpace()));
        
    }
    
    void tryDeletePage( ConfluenceService confluence ) {
        try {
            deletePage(confluence);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }       
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        super.loadUserInfoFromSettings();
        
        super.confluenceExecute( this::tryDeletePage );
        
    }
 
    
}
