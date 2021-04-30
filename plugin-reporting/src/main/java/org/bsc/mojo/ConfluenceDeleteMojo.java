/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.mojo;

import lombok.val;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model.PageSummary;
import org.bsc.confluence.DeployStateManager;
import org.bsc.mojo.configuration.DeployStateInfo;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 *
 * Delete a confluence pageTitle 
 * 
 * @author bsorrentino
 * @since 3.4.0
 */
@Mojo( name="delete", threadSafe = true, requiresProject = false  )
public class ConfluenceDeleteMojo extends AbstractBaseConfluenceSiteMojo implements DeployStateSupport {

    /**
     * perform recursive deletion 
     * 
     * @since 3.4.0
     */
    @Parameter(property = "recursive", defaultValue = "true")
    private boolean recursive;

    /**
     * <b>Experimental feature</b> - Store the last deployed state<br>
     * If declared, a local file will be generated that keeps the last update date
     * of all documents involved in publication.<br>
     * If such file is present the plugin will check the last update date of each
     * document, skipping it, if no update is detected.<br>
     * Example:
     * <pre>
     *   &lt;deployState>
     *     &lt;active> true|false &lt;/active> &lt;!-- default: true -->
     *     &lt;outdir> target dir &lt;/outdir> &lt;!-- default: project.build.directory -->
     *   &lt;/deployState>
     * </pre>
     *
     * @since 7.0
     */
    @Parameter
    protected DeployStateInfo deployState = new DeployStateInfo( false );

    /**
     * @since 7.0
     */
    private DeployStateManager _deployStateManager = null;

    @Override
    public MavenProject getProject() {
        return project;
    }

    @Override
    public final DeployStateInfo getDeployState() { return deployState; }

    /**
     * Lazy load
     * @return
     */
    @Override
    public final Optional<DeployStateManager> getDeployStateManager() {
        if (_deployStateManager ==null) {
            _deployStateManager = initDeployStateManager().orElse(null);
        }
        return ofNullable(_deployStateManager);
    }

    /**
     *
     * @param confluence
     * @return
     */
    private boolean deletePage(ConfluenceService confluence)  {
        Optional<String> optStartPageTitle = empty();

        if( isSiteDescriptorValid() ) {
            val site = createSiteFromModel(getSiteModelVariables());

            optStartPageTitle = ofNullable( site.getHome().getName());
        }

        if( !optStartPageTitle.isPresent() ) {
            optStartPageTitle = ofNullable(getPageTitle());
        }

        return optStartPageTitle.map( startPageTitle -> deletePage( confluence, startPageTitle ))
                .orElseGet( () -> {
                    getLog().warn( "page title has not been provided!" );
                    return false;
                });
    }

    private
    <T extends ConfluenceService.Model.PageSummary>
    CompletableFuture<Void>  deleteDescendents( ConfluenceService confluence, String startPageTitle, T parentPage)
    {
        if (!recursive) return completedFuture(null);

        return confluence.getDescendents(parentPage.getId())
            .thenAccept(descendents -> {
                if (descendents == null || descendents.isEmpty()) {
                    getLog().warn(format("Page [%s]/[%s] in [%s] has not descendents!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                    return;
                }

                for (PageSummary descendent : descendents) {

                    confluence.removePage(descendent.getId())
                            .exceptionally(ex -> {
                                getLog().warn(format("cannot remove descendent %s", descendent.getTitle()), ex);
                                return false;
                            })
                            .thenAccept( removed -> {
                                if( removed ) {
                                    getLog().info(format("Page [%s]/[%s]/[%s] in [%s] has been removed!",
                                            parentPage.getTitle(),
                                            startPageTitle,
                                            descendent.getTitle(),
                                            parentPage.getSpace()));
                                }
                                else {
                                    getLog().warn(format("Page [%s]/[%s]/[%s] in [%s] has not been removed!",
                                            parentPage.getTitle(),
                                            startPageTitle,
                                            descendent.getTitle(),
                                            parentPage.getSpace()));
                                }
                            })
                            .join();
                }
            });

    }

    /**
     *
     * @param confluence
     * @return
     */
    private boolean deletePage(ConfluenceService confluence, String startPageTitle)  {
        getLog().debug(  String.format( "start deleting from page [%s]", startPageTitle));

        final Function<Boolean,Boolean> clearDeployState = ( result ) -> {
            if( result ) getDeployStateManager().ifPresent(dpm -> dpm.clear().save() );
            return result;
        };

        return loadParentPage(confluence, empty())
            .thenCompose( parentPage ->
                confluence.getPageByTitle(parentPage.getId(), startPageTitle)
                    .thenCompose(start -> {

                        if (!start.isPresent()) {
                            getLog().warn(format("Page [%s]/[%s] in [%s] not found!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                            return completedFuture(false);
                        }
                        return deleteDescendents(confluence, startPageTitle, start.get())
                            .thenCompose(v ->
                                confluence.removePage(start.get().getId())
                                    .thenApply(success -> {
                                        if (success) {
                                            getLog().info(format("Page [%s]/[%s] in [%s] has been removed!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                                        } else {
                                            getLog().warn(format("Page [%s]/[%s] in [%s] has not been removed!", parentPage.getTitle(), startPageTitle, parentPage.getSpace()));
                                        }
                                        return success;
                                    })
                            );
                    })
            )
            .exceptionally(ex -> {
                getLog().warn(ex.getMessage());
                return false;
            })
            .thenApply( clearDeployState )
            .join();
    }

    @Override
    public void execute( ConfluenceService confluence ) throws Exception {

        deletePage(confluence);
        
    }
 
    
}
