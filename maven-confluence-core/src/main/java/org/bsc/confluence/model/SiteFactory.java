/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import static java.lang.String.format;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 * @author bsorrentino
 */
public interface SiteFactory {
    
    public interface Folder {

        public Site createSiteFromFolder();
        
    }
    
    public interface Model {
        public Site createSiteFromModel();
        
        default Site createFrom( java.io.File siteDescriptor ) throws Exception {
            Objects.requireNonNull(siteDescriptor, "siteDescriptor is null!");
    
            final String ext = 
                    Optional.ofNullable(FilenameUtils.getExtension(siteDescriptor.getName()))
                    .map( v -> v.toLowerCase() )
                    .orElse("");
            
            switch( ext ) {
            case "xml":
            {           
                final JAXBContext jc = JAXBContext.newInstance(Site.class);
                final Unmarshaller unmarshaller = jc.createUnmarshaller();  
                return (Site) unmarshaller.unmarshal( siteDescriptor );
            }
            case "yml":
            case "yaml":
            {
                final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                return mapper.readValue( siteDescriptor, Site.class  );
            }
            default:
                throw new IllegalArgumentException( 
                        format("file extension [%s] not supported! Currently only '.xml' and '.yaml' are valid", ext));
            }
        }
    }
}
