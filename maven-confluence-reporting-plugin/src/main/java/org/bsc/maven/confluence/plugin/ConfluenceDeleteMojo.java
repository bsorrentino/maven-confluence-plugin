/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.confluence.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Model.PageSummary;

import rx.functions.Action1;

/**
 *
 * Delete a confluence pageTitle 
 * 
 * @author bsorrentino
 * @since 3.4.0
 */
@Mojo( name="delete", threadSafe = true, requiresProject = false  )
public class ConfluenceDeleteMojo extends AbstractBaseConfluenceMojo {

    /**
     * title of pageTitle that will be deleted
     * 
     * @since 3.4.0
     */
    @Parameter(alias = "title", property = "confluence.page", defaultValue = "${project.build.finalName}")
    private String pageTitle;

    /**
     * perform recursive deletion 
     * 
     * @since 3.4.0
     */
    @Parameter(property = "recursive", defaultValue = "true")
    private boolean recursive;

    
    private void deletePage(ConfluenceService confluence) throws Exception {
        final Model.Page parentPage = loadParentPage(confluence);

        if( parentPage==null ) {
            getLog().warn(String.format("Parent page [%s] in [%s] not found!", parentPage.getTitle(), parentPage.getSpace()));                    
            return;
        }

        final Model.PageSummary root = confluence.findPageByTitle(parentPage.getId(),pageTitle);

        if( root==null ) {
            getLog().warn(String.format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(),pageTitle, parentPage.getSpace()));                    
            return;
        }

        if( recursive ) {
            final java.util.List<Model.PageSummary> descendents = confluence.getDescendents(root.getId());

            if( descendents==null || descendents.isEmpty() ) {
                getLog().warn(String.format("Page [%s]/[%s] in [%s] has not descendents!", parentPage.getTitle(),pageTitle, parentPage.getSpace()));                    
            }
            else {

                for( PageSummary descendent : descendents) {

                    getLog().info( String.format("Page [%s]/[%s]/[%s]  has been removed!", parentPage.getTitle(),pageTitle, descendent.getTitle()) );
                    confluence.removePage(descendent.getId());

                }
            }
        }

        confluence.removePage(root.getId());

        getLog().info(String.format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),pageTitle, parentPage.getSpace()));
        
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        super.loadUserInfoFromSettings();
        
        super.confluenceExecute( new Action1<ConfluenceService>() {

            @Override
            public void call(ConfluenceService confluence)   {
                
                try {
                    deletePage(confluence);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
        
            }
        });
        
    }
 
    
}
