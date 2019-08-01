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

    
    private void deletePage(ConfluenceService confluence) throws Exception {
        val parentPage = loadParentPage(confluence, Optional.empty());

        if( parentPage==null ) {
            getLog().warn("Parent page not found!");                    
            return;
        }

        val root = confluence.findPageByTitle(parentPage.getId(),getPageTitle());

        if( root==null ) {
            getLog().warn(format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(),getPageTitle(), parentPage.getSpace()));                    
            return;
        }

        if( recursive ) {
            val descendents = confluence.getDescendents(root.getId());

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

        confluence.removePage(root.getId());

        getLog().info(format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),getPageTitle(), parentPage.getSpace()));
        
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        super.loadUserInfoFromSettings();
        
        super.confluenceExecute( (ConfluenceService confluence)  -> {
                
                try {
                    deletePage(confluence);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
        
        });
        
    }
 
    
}
