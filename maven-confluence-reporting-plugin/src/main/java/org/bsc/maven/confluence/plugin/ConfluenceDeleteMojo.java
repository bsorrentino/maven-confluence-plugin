/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.confluence.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import static org.bsc.maven.plugin.confluence.ConfluenceUtils.findPageByTitle;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;

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
    @Parameter(property = "confluence.page", required = true)
    private String pageTitle;

    /**
     * perform recursive deletion 
     * 
     * @since 3.4.0
     */
    @Parameter(property = "recursive", defaultValue = "true")
    private boolean recursive;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        super.confluenceExecute( new ConfluenceTask() {

            @Override
            public void execute(Confluence confluence) throws Exception {
                
                final Page parentPage = confluence.getPage(getSpaceKey(), getParentPageTitle());
                if( parentPage==null ) {
                    getLog().warn(String.format("Parent page [%s] in [%s] not found!", getParentPageTitle(), getSpaceKey()));                    
                    return;
                }

           
                final PageSummary root = ConfluenceUtils.findPageByTitle(confluence, parentPage.getId(),pageTitle);
                
                if( root==null ) {
                    getLog().warn(String.format("Page [%s]/[%s] in [%s] not found!", getParentPageTitle(),pageTitle, getSpaceKey()));                    
                    return;
                }
                
                
                final java.util.List<PageSummary> descendents = confluence.getDescendents(root.getId());

                if( descendents==null || descendents.isEmpty() ) {
                    getLog().warn(String.format("Page [%s]/[%s] in [%s] has not descendents!", getParentPageTitle(),pageTitle, getSpaceKey()));                    
                }
                else {
                    
                    for( PageSummary descendent : descendents) {
                        
                        getLog().info( String.format("Page [%s]/[%s]/[%s]  has been removed!", getParentPageTitle(),pageTitle, descendent.getTitle()) );
                        confluence.removePage(descendent.getId());
                        
                    }
                }
                
                confluence.removePage(root.getId());

                getLog().info(String.format("Page [%s]/[%s] in [%s] has been removed!", getParentPageTitle(),pageTitle, getSpaceKey()));
        
            }
        });
        
    }
 
    
}
