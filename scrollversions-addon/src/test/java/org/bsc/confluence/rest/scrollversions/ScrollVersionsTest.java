package org.bsc.confluence.rest.scrollversions;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;
import lombok.val;

import java.util.regex.Pattern;

/**
 * 
 * @author bsorrentino
 *
 */
public class ScrollVersionsTest {

    @Test
    public void fromConfluenceToScrollVersionsUrl() {
        final String regex = "/rest/api(/?)$";
        
        {
        val confluenceUrl       = "http://localhost:8090/rest/api";     
        val scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");
        
        assertThat( scrollVersionsUrl, equalTo("http://localhost:8090/rest/scroll-versions/1.0"));
        }
        
        {
        val confluenceUrl       = "http://localhost:8090/rest/api/";     
        val scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");
        
        assertThat( scrollVersionsUrl, equalTo("http://localhost:8090/rest/scroll-versions/1.0"));
        }
        
        {
        val confluenceUrl       = "http://localhost:8090/rest/api/xxx/rest/api";     
        val scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");

        assertThat( scrollVersionsUrl, equalTo("http://localhost:8090/rest/api/xxx/rest/scroll-versions/1.0"));
        }
        
    }

    @Test
    public void decodeScrollVersionsTitle() {
        val p = Pattern.compile( "^[\\.](.+)\\sv(.+)$");

        val m = p.matcher(".Parent - Issue#194 valpha");

        assertTrue( m.matches() );
        assertEquals( "Parent - Issue#194", m.group(1));
        assertEquals( "alpha", m.group(2));

    }
}
