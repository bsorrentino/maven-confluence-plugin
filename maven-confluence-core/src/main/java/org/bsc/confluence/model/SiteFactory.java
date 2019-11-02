/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import static java.lang.String.format;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.bsc.confluence.preprocessor.SitePocessorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.apachecommons.CommonsLog;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
	
	@CommonsLog
	final class LogHolder {}
	
    public interface Folder {

        public Site createSiteFromFolder();
        
    }
    
    public interface Model {
        
        public Site createSiteFromModel(Map<String, Object> variables);
        
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
                        final JAXBContext jc = JAXBContext.newInstance(Site.class);
                        final Unmarshaller unmarshaller = jc.createUnmarshaller(); 
                        
                        result.complete( (Site)unmarshaller.unmarshal( new StringReader(preprocessedDescriptor)) );
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
            
            final Optional<SitePocessorService> siteProcessor = SitePocessorService.getDefaultPreprocessorService();
            
            return siteProcessor.map( p -> p.process(content, variables)
                                                .thenCompose( _createSite )
                                                // uncomment if you want process source content ignoring preprocess exception
                                                //.exceptionally( e -> _createSite.apply(content).join() ) 
                                                )
            									.orElseGet( () ->{
            										LogHolder.log.debug( format("a Preprocessor service is not configurated") );
            										return _createSite.apply(content);
            									})
                    .get();
                                                
                   
    
        }
    }
}
