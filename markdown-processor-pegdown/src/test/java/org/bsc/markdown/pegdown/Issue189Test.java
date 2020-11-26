package org.bsc.markdown.pegdown;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 
 * @author bsorrentino
 *
 */
public class Issue189Test extends PegdownParse {

    @Override
    protected char[] loadResource() throws IOException {
        return new StringBuilder()
            .append( "![alt text](./images/img.png \"title\")" ).append("\n\n")
            .append( "![](./images/img.png \"title\")" ).append("\n\n")
            .append( "![](./images/img.png \"\")" ).append("\n\n")
            .append( "![](./images/img.png)" ).append("\n\n")
            .append( "![alt text](${pageTitle}^img.png \"title\")" ).append("\n\n")
            .append( "![alt text](img.png \"title\")" ).append("\n\n")
            .append( "![thumbnail](meal.png \"Meal\")" ).append("\n\n")
            .append( "![conf-icon](http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png \"My conf-icon\")" ).append("\n\n")
            .append( "![cb-integration-components.png](cb-integration-components.png)").append("\n\n")
            .toString()
            .toCharArray()
        ;
        
    }
    
    @Test
    public void parse() throws Exception {
        final String [] lines = serializeToString( createPage("Page1", "./Page1.md") ).split( "\n" );
        
        int i=0;
        assertEquals( "!${page.title}^img.png|alt text!", lines[i++] );
        assertEquals( "!${page.title}^img.png|!", lines[i++] );
        assertEquals( "!${page.title}^img.png|!", lines[i++] );
        assertEquals( "!${page.title}^img.png|!", lines[i++] );
        assertEquals( "!${pageTitle}^img.png|alt text!", lines[i++] );
        assertEquals( "!${page.title}^img.png|alt text!", lines[i++] );
        assertEquals( "!${page.title}^meal.png|thumbnail!", lines[i++] );
        assertEquals( "!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon!", lines[i++] );
        assertEquals( "!${page.title}^cb-integration-components.png|cb-integration-components.png!", lines[i++] );
    }

    /**
     * 
     * @param uri
     * @return
     */
    private CompletableFuture<String> getFileName( String uri ) {
        
        final CompletableFuture<String> result = new CompletableFuture<>();
        try {
            final java.net.URI uriObject = java.net.URI.create(uri);
            
            if( "classpath".equalsIgnoreCase(uriObject.getScheme()) ) {
                result.completeExceptionally( 
                        new IllegalArgumentException( "'classpath' scheme is not supported!"));    
                return result;
            }
            final Path path = Paths.get(uriObject.getPath());
            
            result.complete(path.getFileName().toString());
        
        } catch( Throwable e) {
            
            result.completeExceptionally(e);
        }
        return result;
    }
    
    @Test
    public void processUrl() {
        final Pattern patternUrl = Pattern.compile("(?:(\\$\\{.+\\})\\^)?(.+)");
        {
        final String url = "${page.title}^image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertEquals( "${page.title}", m.group(1) );
        assertEquals( "image-name.png", m.group(2) );
        }
        
        {
        final String url = "${page.title}^./images/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertEquals( "${page.title}", m.group(1) );
        assertEquals( "./images/image-name.png", m.group(2) );
        
        getFileName(m.group(2))
        .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
        .thenAccept( fileName -> assertEquals( "image-name.png", fileName))
        .join()
        ;

        }
        
        {
        final String url = "image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertEquals( "image-name.png", fileName))
            .join()
            ;
        }
        
        {
        final String url = "./images/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "./images/image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertEquals( "image-name.png", fileName))
            .join()
            ;
        }
        
        {
        final String url = "file:///image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "file:///image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertEquals( "image-name.png", fileName))
            .join()
            ;
        }
        
        {
        final String url = "http://localhost:8080/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "http://localhost:8080/image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertEquals( "image-name.png", fileName))
            .join()
            ;
        }
        
        {
        final String url = "classpath:/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "classpath:/image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { return null ;} )
            .thenAccept( fileName -> assertNull( fileName ) )
            .join()
            ;
        }
        
        {
        final String url = "/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertTrue( m.matches() );
        assertEquals( 2, m.groupCount() );
        assertNull( m.group(1) );
        assertEquals( "/image-name.png", m.group(2) );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenApply( fileName -> "${page.title}^".concat(fileName) )
            .thenAccept( fileName -> assertEquals( "${page.title}^image-name.png", fileName ))
            .join()
            ;
        }
        
    }
    
}
