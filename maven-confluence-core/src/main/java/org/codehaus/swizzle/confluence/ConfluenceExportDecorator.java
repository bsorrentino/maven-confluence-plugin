/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codehaus.swizzle.confluence;

import static org.apache.commons.httpclient.HttpStatus.SC_MOVED_TEMPORARILY;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ExportFormat;

/**
 *
 * @author bsorrentino
 */
public class ConfluenceExportDecorator {
   
    interface RedirectTask {
        void exec( String location ) throws Exception;
            
    }
    
    final ConfluenceService confluence;
    final String url;
    
    /**
     * 
     * @param confluence
     * @param url
     * @param username
     * @param password 
     */
    public ConfluenceExportDecorator(   ConfluenceService confluence, 
                                        String url ) 
    {
        assert url!= null;
        assert confluence!= null;
        
        if( confluence == null ) {
            throw new IllegalArgumentException("confluence is null!");
        }
        if( url == null ) {
            throw new IllegalArgumentException("url is null!");
        }
        
        this.confluence = confluence;
        this.url = url;
    }
    
    /**
     * 
     * @param space
     * @param pageTitle
     * @param format
     * @param outputFile
     * @throws Exception 
     */
    public final void exportPage( String space, String pageTitle, ExportFormat format, final java.io.File outputFile ) throws Exception {
        assert space!=null;
        assert pageTitle!=null;
        assert format!=null;
        assert outputFile!=null;
        
        if( space == null ) {
            throw new IllegalArgumentException("space is null!");
        }
        if( pageTitle == null ) {
            throw new IllegalArgumentException("pageTitle is null!");
        }
        if( format == null ) {
            throw new IllegalArgumentException("format is null!");
        }
        if( outputFile == null ) {
            throw new IllegalArgumentException("outputFile is null!");
        }
        if( outputFile.exists() && !outputFile.isFile() ) {
            throw new IllegalArgumentException("outputFile is not a file!");
        }
        
        final Model.Page page = confluence.getPage(space, pageTitle);

        final HttpClient client = new HttpClient();
        client.getParams().setCookiePolicy( CookiePolicy.BROWSER_COMPATIBILITY);

        login(  client, 
                String.format( "%s?pageId=%s", format.url, page.getId()), 
                new RedirectTask() {

                    @Override
                    public void exec(String location) throws Exception {                        
                        exportpdf(client, location, outputFile);
                    }
                }
            );
        
        
    }
    
    
    /**
     * 
     * @param client
     * @param redirectUrl
     * @throws Exception 
     */
    private void login( HttpClient client, 
                        String redirectUrl, 
                        RedirectTask task ) throws Exception  
    {
 
        PostMethod post = null;
        try {
            final String login = String.format("%s/%s", url, "login.action");
            
            post = new PostMethod(login);
            
            post.setDoAuthentication(true);
            post.addParameter("os_username", confluence.getCredentials().username);
            post.addParameter("os_password", confluence.getCredentials().password);
            post.addParameter("os_destination", redirectUrl);
            post.addParameter("login", "Log+In");
 
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK && 
                statusCode != SC_MOVED_TEMPORARILY) 
            {
                throw new HttpException( 
                        String.format("Execute ethod failed: [%s]", 
                                String.valueOf(post.getStatusLine())) );
            }

            String redirectLocation;
            Header locationHeader = post.getResponseHeader("Location");
            if (locationHeader != null) {

                redirectLocation = locationHeader.getValue();
                
                task.exec(redirectLocation);
                

            } else {

                throw new HttpException( 
                        String.format("no redirect to url found\n[%s]", 
                                new String(post.getResponseBody())) );
            }
        } finally {

            if (post != null) {
                post.releaseConnection();
            }
        }
        
    }
    
    /**
     * 
     * @param client
     * @param url
     * @throws Exception 
     */
    private void exportpdf( HttpClient client, 
                            String url, 
                            java.io.File outputFile ) throws IOException  
    {
        java.io.InputStream is = null;
        java.io.FileOutputStream fos = null;
        GetMethod get = null;
        try {

            get = new GetMethod(url);
            get.setRequestHeader("X-Atlassian-Token", "no-check");
            
            
            int statusCode = client.executeMethod(get);
            if (statusCode != HttpStatus.SC_OK) {
                throw new HttpException( 
                        String.format("Execute ethod failed: [%s]", 
                                String.valueOf(get.getStatusLine())) );
            }

            is = get.getResponseBodyAsStream();
            
            fos = new java.io.FileOutputStream(outputFile);

            IOUtils.copy(is, fos);
            
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);

            if (get != null) {
                get.releaseConnection();
            }
        }
        
    }
    
}
