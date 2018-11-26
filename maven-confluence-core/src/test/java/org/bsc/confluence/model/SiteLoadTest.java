package org.bsc.confluence.model;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class SiteLoadTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private void loadFromYAML( String resource, Consumer<Site> c ) throws Exception {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        
        try( InputStream is = getClass().getClassLoader().getResourceAsStream(resource) ) {
            Site site = mapper.readValue( is, Site.class  );
            
            assertThat( site, notNullValue());
            
            c.accept(site);
        }
    
    }
    
    @Test
    public void testIssue182() throws Exception {
    
        //thrown.expect(Exception.class);
        thrown.expect(JsonMappingException.class);
        //thrown.expectMessage("name [page 1 ] is not valid!");
        loadFromYAML( "site-issue182.yaml", site -> {
                        
        });

    }
    
    @Test
    public void testLoadFromYAML() throws Exception {
        
        loadFromYAML( "site.yaml", site -> {
            
            final Path basedir = Paths.get("/tmp");
            site.setBasedir( basedir );
            
            assertThat( site.getHome(), notNullValue());
            assertThat( site.getHome().getUri(), equalTo( 
                    Paths.get(basedir.toString(), "encoding.confluence").toUri()));
            
            final List<Site.Page> children = site.getHome().getChildren();
            
            assertThat( children, notNullValue());
            assertThat( children.size(), equalTo( 2 ) );

            final List<Site.Attachment> attachments = site.getHome().getAttachments();
            
            assertThat( attachments, notNullValue());
            assertThat( attachments.size(), equalTo( 1 ) );
            
        });
            
    }
}
