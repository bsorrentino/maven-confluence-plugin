/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.java.Log;
import org.apache.commons.io.FilenameUtils;
import org.bsc.confluence.preprocessor.SiteProcessorService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
	
	@Log
	final class LogHolder {}
	
    interface Folder {

        Site createSiteFromFolder();
        
    }
    
    interface Model {
        
        Site createSiteFromModel(Map<String, Object> variables);
        
        /**
         * 
         * @param siteDescriptor
         * @param variables
         * @return
         * @throws Exception
         */
        default Site createFrom( java.io.File siteDescriptor, Map<String, Object> variables ) throws Exception {
            
            if (variables == null)
                throw new java.lang.IllegalArgumentException("variables is null!");
     
            final String ext = 
                    Optional.ofNullable(FilenameUtils.getExtension(siteDescriptor.getName()))
                    .map( v -> v.toLowerCase() )
                    .orElse("");
            
            // _createSite lambda function
            final Function<String,CompletableFuture<Site>> _createSite = ( String preprocessedDescriptor ) -> {
                
                final CompletableFuture<Site> result = new CompletableFuture<>();
                
                switch( ext ) {
                case "xml":
                {      
                    try {
                        final ObjectMapper mapper = new ObjectMapper(new XmlFactory());
                        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                        result.complete( mapper.readValue( preprocessedDescriptor, Site.class  ) );
                    }
                    catch( Exception e ) {
                        result.completeExceptionally(e);
                    }
                    
                    break;
                }
                case "yml":
                case "yaml":
                {
                    try {

                        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                        result.complete( mapper.readValue( preprocessedDescriptor, Site.class  ) );
                    }
                    catch( Exception e ) {
                        result.completeExceptionally(e);
                    }

                    break;
                }
                default:
                    result.completeExceptionally(new IllegalArgumentException( 
                            format("file extension [%s] not supported! Currently only '.xml' and '.yaml' are valid", ext)));
                }
                
                return result;
            }; // end lambda function
            
            final String content = new String(Files.readAllBytes(siteDescriptor.toPath()), StandardCharsets.UTF_8);
            
            final Optional<SiteProcessorService> siteProcessor = SiteProcessorService.getDefaultPreprocessorService();
            
            return siteProcessor.map( p -> p.process(content, variables)
                                                .thenCompose( _createSite )
                                                // uncomment if you want process source content ignoring preprocess exception
                                                //.exceptionally( e -> _createSite.apply(content).join() ) 
                                                )
            									.orElseGet( () ->{
            										LogHolder.log.fine( format("a Preprocessor service is not configurated") );
            										return _createSite.apply(content);
            									})
                    .get();
                                                
                   
    
        }
    }
}
