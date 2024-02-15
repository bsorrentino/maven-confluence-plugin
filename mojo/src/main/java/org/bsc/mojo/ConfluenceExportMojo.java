/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.xmlrpc.ConfluenceExportDecorator;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * Export a confluence page either in PDF or DOC 
 * 
 * @author bsorrentino
 * 
 * @since 3.4.3
 */
@Mojo( name="export", threadSafe = true, requiresProject = false  )
public class ConfluenceExportMojo extends AbstractBaseConfluenceMojo {

    /**
     * title of pageTitle that will be deleted
     * 
     * @since 3.4.3
     */
    @Parameter(alias = "title", property = "confluence.page", defaultValue = "${project.build.finalName}")
    private String pageTitle;
    
    /**
     * 
     * type of output - either 'pdf' or 'doc'
     * 
     * @since 3.4.3
     */
    @Parameter(alias = "outputType", property = "outputType", defaultValue = "pdf")
    private String outputType;
    
    /**
     * 
     * output file - if missing it will be ${project.build.directory}/${title}.${outputType}
     * 
     * @since 3.4.3
     */
    @Parameter(alias = "outputFile")
    private java.io.File outputFile;

    
    @Parameter(property="project.build.directory", readonly = true)
    private java.io.File outputDirectory;
    
    
    private CompletableFuture<Void> exportPage(ConfluenceService confluence, Model.Page parentPage )   {

        final var result = new CompletableFuture<Void>();

        final var exfmt = ExportFormat.valueOf( outputType.toUpperCase() );

        outputFile = ofNullable(outputFile).orElseGet( () -> {

            final var fileName = format("%s.%s", pageTitle, exfmt.name().toLowerCase());

            final java.io.File file =
                    ofNullable(outputDirectory)
                        .map( dir -> new java.io.File( dir, fileName))
                        .orElseGet( () -> new java.io.File( fileName));

            return file;
        });

        try {
            FileUtils.forceMkdir( new java.io.File(outputFile.getParent()) );

            final var url = ConfluenceService.Protocol.XMLRPC.removeFrom(ConfluenceExportMojo.super.getEndPoint());

            final var exporter =
                    new ConfluenceExportDecorator( confluence, url );

            exporter.exportPage(parentPage.getSpace(),
                    pageTitle,
                    exfmt,
                    outputFile);

            result.complete(null);


        } catch (IOException e) {
            result.completeExceptionally(e);
        }

        return result;

    }

    /**
     *
     * @param confluence
     * @throws Exception
     */
    @Override
    public void execute( ConfluenceService confluence) throws Exception {

        loadParentPage(confluence, empty())
                .thenCompose( parentPage -> exportPage( confluence, parentPage ))
                .join();
    }
    
}
