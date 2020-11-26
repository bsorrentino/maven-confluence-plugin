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
import static java.util.concurrent.CompletableFuture.completedFuture;

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

    /**
     *
     * @param confluence
     * @return
     */
    private boolean deletePage(ConfluenceService confluence)  {

        return loadParentPage(confluence, Optional.empty())
                .thenCompose( parentPage -> {

                    final String startPageTitle = getStartPageTitle();

                    getLog().debug(  String.format( "start deleting from page [%s]", startPageTitle));

                    return confluence.getPageByTitle(parentPage.getId(), startPageTitle)
                            .thenCompose( ( start ) -> {

                                if (!start.isPresent()) {
                                    getLog().warn(format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                                    return completedFuture(false);
                                }

                                if (recursive) {

                                    confluence.getDescendents(start.get().getId()).thenAccept(descendents -> {
                                        if (descendents == null || descendents.isEmpty()) {
                                            getLog().warn(format("Page [%s]/[%s] in [%s] has not descendents!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                                        }
                                        else {
                                            for (PageSummary descendent : descendents) {
                                                final boolean removed =
                                                        confluence.removePage(descendent.getId())
                                                        .exceptionally(ex -> {
                                                            getLog().warn(format("cannot remove descendent %s", descendent.getTitle()), ex);
                                                            return false;

                                                        })
                                                        .join();
                                                if( removed ) {
                                                    getLog().info(format("Page [%s]/[%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),startPageTitle, descendent.getTitle(), parentPage.getSpace()));
                                                }
                                                else {
                                                    getLog().warn(format("Page [%s]/[%s]/[%s] in [%s] has not been removed!", parentPage.getTitle(),startPageTitle, descendent.getTitle(), parentPage.getSpace()));
                                                }
                                            }
                                        }

                                    }).join();

                                }

                                return confluence.removePage(start.get().getId())
                                        .thenApply( success -> {
                                            if( success ) {
                                                getLog().info(format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(),startPageTitle, parentPage.getSpace()));
                                            }
                                            else {
                                                getLog().warn(format("Page [%s]/[%s] in [%s] has not been removed!", parentPage.getTitle(),startPageTitle, parentPage.getSpace()));
                                            }
                                            return success;
                                        });
                            });
        }).exceptionally( ex  -> {
            getLog().warn( ex.getMessage() );
            return false;
        })
        .join();

    }

    @Override
    public void execute( ConfluenceService confluence ) throws Exception {

        deletePage(confluence);
        
    }
 
    
}
