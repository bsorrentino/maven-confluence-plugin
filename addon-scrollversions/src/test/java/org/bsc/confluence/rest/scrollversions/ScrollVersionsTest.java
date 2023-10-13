package org.bsc.confluence.rest.scrollversions;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final var confluenceUrl       = "http://localhost:8090/rest/api";     
        final var scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");
        
        assertEquals( "http://localhost:8090/rest/scroll-versions/1.0", scrollVersionsUrl );
        }
        
        {
        final var confluenceUrl       = "http://localhost:8090/rest/api/";     
        final var scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");

        assertEquals( "http://localhost:8090/rest/scroll-versions/1.0", scrollVersionsUrl );
        }
        
        {
        final var confluenceUrl       = "http://localhost:8090/rest/api/xxx/rest/api";     
        final var scrollVersionsUrl   = confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0");

            assertEquals( "http://localhost:8090/rest/api/xxx/rest/scroll-versions/1.0", scrollVersionsUrl );
        }
        
    }

    @Test
    public void decodeScrollVersionsTitle() {
        final var p = Pattern.compile( "^[\\.](.+)\\sv(.+)$");

        final var m = p.matcher(".Parent - Issue#194 valpha");

        assertTrue( m.matches() );
        assertEquals( "Parent - Issue#194", m.group(1));
        assertEquals( "alpha", m.group(2));

    }
}
