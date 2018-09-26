package org.bsc.confluence.model;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class SiteLoadTest {

    
    @Test
    public void loadFromYAML() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            
        try( InputStream is = getClass().getClassLoader().getResourceAsStream("site.yaml") ) {
            Site site = mapper.readValue( is, Site.class  );
            
            Path basedir = Paths.get("/tmp");
            site.setBasedir( basedir );
            assertThat( site, notNullValue());
            
            assertThat( site.getHome(), notNullValue());
            assertThat( site.getHome().getUri(), IsEqual.equalTo( 
                    Paths.get(basedir.toString(), "encoding.confluence").toUri()));
            
            final List<Site.Page> children = site.getHome().getChildren();
            
            assertThat( children, notNullValue());
            assertThat( children.size(), IsEqual.equalTo( 2 ) );

            final List<Site.Attachment> attachments = site.getHome().getAttachments();
            
            assertThat( attachments, notNullValue());
            assertThat( attachments.size(), IsEqual.equalTo( 1 ) );
        }

    }
}
