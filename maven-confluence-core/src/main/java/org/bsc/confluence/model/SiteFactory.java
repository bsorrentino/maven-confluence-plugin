/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;
import org.bsc.confluence.preprocessor.Preprocessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
    
    public interface Folder {

        public Site createSiteFromFolder();
        
    }
    
    public interface Model {
        public Site createSiteFromModel(Map<String, Object> variables);
        
        default Site createFrom( java.io.File siteDescriptor, Map<String, Object> variables ) throws Exception {
            Objects.requireNonNull(siteDescriptor, "siteDescriptor is null!");
            String content = new String(Files.readAllBytes(siteDescriptor.toPath()), StandardCharsets.UTF_8);
            String preprocessedDescriptor = Preprocessor.INSTANCE.preprocess(content, variables);
    
            final String ext = 
                    Optional.ofNullable(FilenameUtils.getExtension(siteDescriptor.getName()))
                    .map( v -> v.toLowerCase() )
                    .orElse("");
            
            switch( ext ) {
            case "xml":
            {           
                final JAXBContext jc = JAXBContext.newInstance(Site.class);
                final Unmarshaller unmarshaller = jc.createUnmarshaller();  
                return (Site) unmarshaller.unmarshal( new StringReader(preprocessedDescriptor));
            }
            case "yml":
            case "yaml":
            {
                final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                return mapper.readValue( preprocessedDescriptor, Site.class  );
            }
            default:
                throw new IllegalArgumentException( 
                        format("file extension [%s] not supported! Currently only '.xml' and '.yaml' are valid", ext));
            }
        }
    }
}
