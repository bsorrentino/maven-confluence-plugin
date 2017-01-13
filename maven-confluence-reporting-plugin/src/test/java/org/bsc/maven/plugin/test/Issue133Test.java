/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import okhttp3.HttpUrl;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author softphone
 */
public class Issue133Test {
 
    @Test @Ignore
    public void dummy() {}
    
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void htttpUrlBuilderWithoutPort() throws MalformedURLException {
        
        final java.net.URL endpoint = new java.net.URL("http://localhost/confluence");
        
        Assert.assertThat( endpoint.getPort(), Is.is(-1));
        
        final HttpUrl.Builder builder = new HttpUrl.Builder()
               .scheme(endpoint.getProtocol())
               .host(endpoint.getHost())
               .port(endpoint.getPort())
               .addPathSegments("rest/api")
             ; 
  
    }

    @Test
    public void htttpUrlBuilderWithoutPortSafe() throws MalformedURLException {
        
        final java.net.URL endpoint = new java.net.URL("http://localhost/confluence");
        
        Assert.assertThat( endpoint.getPort(), Is.is(-1));
        
        int port = endpoint.getPort();
        port = (port > -1 ) ? port : endpoint.getDefaultPort();

        String path = endpoint.getPath();
        path = (path.startsWith("/")) ? path.substring(1) : path;

        final HttpUrl.Builder builder = new HttpUrl.Builder()
                      .scheme(endpoint.getProtocol())
                      .host(endpoint.getHost())
                      .port( port )
                      ;
        Assert.assertThat(path, IsEqual.equalTo("confluence"));

        final HttpUrl url = 
                builder
                        .addPathSegments(path)
                        //.addPathSegments("rest/api")                        
                        .build();

        Assert.assertThat( url.url(), IsEqual.equalTo(endpoint)); 
  
    }
    
    @Test
    public void htttpUrlBuilderWithoutPortSafe2() throws MalformedURLException, URISyntaxException {
        
        final java.net.URL endpoint = new java.net.URL("http://localhost/");
        
        Assert.assertThat( endpoint.getPort(), Is.is(-1));
        
        int port = endpoint.getPort();
        port = (port > -1 ) ? port : endpoint.getDefaultPort();

        String path = endpoint.getPath();
        path = (path.startsWith("/")) ? path.substring(1) : path;
        
        final HttpUrl.Builder builder = new HttpUrl.Builder()
                      .scheme(endpoint.getProtocol())
                      .host(endpoint.getHost())
                      .port( port )
                      ;
        
        Assert.assertThat( path, IsEqual.equalTo(""));
        
        final HttpUrl url = 
                builder
                        .addPathSegments(path)
                        //.addPathSegments("rest/api")                        
                        .build();

        Assert.assertThat( url.url(), IsEqual.equalTo(endpoint));
                    ; 
  
    }
}
