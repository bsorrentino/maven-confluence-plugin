/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.confluence.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.functional.P1;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ExportFormat;

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
    
    
    private void exportPage( ConfluenceService confluence ) throws Exception  {
        final ExportFormat exfmt = ExportFormat.valueOf( outputType.toUpperCase() );

        final Model.Page parentPage = loadParentPage(confluence);

        if( outputFile == null ) {

            outputFile = ( outputDirectory == null ) ? 
                                new java.io.File( String.format("%s.%s", pageTitle, exfmt.name().toLowerCase())) : 
                                new java.io.File( outputDirectory, String.format("%s.%s", pageTitle, exfmt.name().toLowerCase())) 
                    ;
        }

        FileUtils.forceMkdir( new java.io.File(outputFile.getParent()) );


        final String url = ConfluenceExportMojo.super.getEndPoint().replace("/rpc/xmlrpc", "");  // /rpc/xmlrpc

        confluence.exportPage(  url, 
                                parentPage.getSpace(), 
                                parentPage.getTitle(), 
                                exfmt, 
                                outputFile);
        
    }
    
    /**
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException 
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
       super.loadUserInfoFromSettings();
        
        super.confluenceExecute( new P1<ConfluenceService>() {

            @Override
            public void call(ConfluenceService confluence)  {
                
                try {
                    exportPage(confluence);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
  
        });
        
    }
    
}
