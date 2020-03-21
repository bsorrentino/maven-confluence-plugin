package org.bsc.markdown;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bsc.markdown.PegdownParse;
import org.junit.Test;


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
    public void parse() throws IOException {
        final String [] lines = serializeToString().split( "\n" );
        
        int i=0;
        assertThat( lines[i++], equalTo("!${page.title}^img.png|alt text!"));
        assertThat( lines[i++], equalTo("!${page.title}^img.png|!"));
        assertThat( lines[i++], equalTo("!${page.title}^img.png|!"));
        assertThat( lines[i++], equalTo("!${page.title}^img.png|!"));
        assertThat( lines[i++], equalTo("!${pageTitle}^img.png|alt text!"));
        assertThat( lines[i++], equalTo("!${page.title}^img.png|alt text!"));
        assertThat( lines[i++], equalTo("!${page.title}^meal.png|thumbnail!"));
        assertThat( lines[i++], equalTo("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon!"));
        assertThat( lines[i++], equalTo("!${page.title}^cb-integration-components.png|cb-integration-components.png!"));
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
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), equalTo("${page.title}") );
        assertThat( m.group(2), equalTo("image-name.png") );
        }
        
        {
        final String url = "${page.title}^./images/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), equalTo("${page.title}") );
        assertThat( m.group(2), equalTo("./images/image-name.png") );
        
        getFileName(m.group(2))
        .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
        .thenAccept( fileName -> assertThat( fileName, equalTo("image-name.png")))
        .join()
        ;

        }
        
        {
        final String url = "image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertThat( fileName, equalTo("image-name.png")))
            .join()
            ;
        }
        
        {
        final String url = "./images/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("./images/image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertThat( fileName, equalTo("image-name.png")))
            .join()
            ;
        }
        
        {
        final String url = "file:///image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("file:///image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertThat( fileName, equalTo("image-name.png")))
            .join()
            ;
        }
        
        {
        final String url = "http://localhost:8080/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("http://localhost:8080/image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenAccept( fileName -> assertThat( fileName, equalTo("image-name.png")))
            .join()
            ;
        }
        
        {
        final String url = "classpath:/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("classpath:/image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { return null ;} )
            .thenAccept( fileName -> assertThat( fileName, nullValue()) )
            .join()
            ;
        }
        
        {
        final String url = "/image-name.png";
        final Matcher m = patternUrl.matcher(url);
        
        assertThat( m.matches(), equalTo(true) );
        assertThat( m.groupCount(), equalTo(2) );
        assertThat( m.group(1), nullValue() );
        assertThat( m.group(2), equalTo("/image-name.png") );
        
        getFileName(m.group(2))
            .exceptionally( ex -> { fail( ex.getMessage()); return null ;} )
            .thenApply( fileName -> "${page.title}^".concat(fileName) )
            .thenAccept( fileName -> assertThat( fileName, equalTo("${page.title}^image-name.png")))
            .join()
            ;
        }
        
    }
    
}
