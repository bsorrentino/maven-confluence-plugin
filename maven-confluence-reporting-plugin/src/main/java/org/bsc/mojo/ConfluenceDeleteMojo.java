/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.mojo;

import lombok.val;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model.PageSummary;

import java.util.Optional;

import static java.lang.String.format;

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

    private String getStartPageTitle() {
        final String result;
        if( isSiteDescriptorValid() ) {
            val site = createSiteFromModel(getSiteModelVariables());
            
            result = site.getHome().getName();
        }
        else {
            result = getPageTitle();
        }
        return result;
        
    }
    
    private void deletePage(ConfluenceService confluence) throws Exception {
        val parentPage = loadParentPage(confluence, Optional.empty());

        if( parentPage==null ) {
            getLog().warn("Parent page not found!");                    
            return;
        }

        final String startPageTitle = getStartPageTitle();
        
        getLog().debug(  String.format( "start deleting from page [%s]", startPageTitle));   
        
        val start = confluence.findPageByTitle(parentPage.getId(), startPageTitle);
        
        if( start==null ) {
            getLog().warn(format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));                    
            return;
        }

        if( recursive ) {
            val descendents = confluence.getDescendents(start.getId());

            if( descendents==null || descendents.isEmpty() ) {
                getLog().warn(format("Page [%s]/[%s] in [%s] has not descendents!", parentPage.getTitle(),startPageTitle, parentPage.getSpace()));
            }
            else {

                for( PageSummary descendent : descendents) {

                    getLog().info( format("Page [%s]/[%s]/[%s]  has been removed!", parentPage.getTitle(),startPageTitle, descendent.getTitle()) );
                    confluence.removePageAsync( descendent.getId() ).join();

                }
            }
        }

        confluence.removePageAsync(start.getId()).join();

        getLog().info(format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),startPageTitle, parentPage.getSpace()));
        
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
    	
    	if( getLog().isDebugEnabled())
    		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
        
        super.loadUserInfoFromSettings();
        
        super.confluenceExecute( this::tryDeletePage );
        
    }
 
    
}
