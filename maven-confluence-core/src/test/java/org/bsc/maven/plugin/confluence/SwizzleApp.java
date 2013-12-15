/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.confluence;

import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;

/**
 *
 * @author bsorrentino
 */
public class SwizzleApp {

    public static final String PASSWORD = "admin";
    public static final String USERNAME = "admin";
    public static final String URL = "http://localhost:8080/confluence";


    static enum Format {

        PDF("spaces/flyingpdf/pdfpageexport.action"), DOC("exportword");

        private String url;

        private Format(String url) {
            this.url = url;
        }

    }

    public static void login( HttpClient client,  String url, String username, String password, String redirectUrl ) throws Exception {
        java.io.InputStream is = null;
        java.io.FileOutputStream fos = null;
 
        PostMethod post = null;
        try {
            final String login = String.format("%s/%s", url, "login.action");

            System.out.println(login);
            
            post = new PostMethod(login);
            
            post.setDoAuthentication(true);
            post.addParameter("os_username", username);
            post.addParameter("os_password", password);
            post.addParameter("os_destination", redirectUrl);
            post.addParameter("login", "Log+In");
 
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + post.getStatusLine());
            }

            for( Header h : post.getResponseHeaders() ) {
                 System.out.printf("header [%s]=[%s]\n", h.getName(), h.getValue() );
               
            }
            
            String redirectLocation;
            Header locationHeader = post.getResponseHeader("Location");
            if (locationHeader != null) {
                redirectLocation = locationHeader.getValue();
                System.out.printf("redirect to [%s]\n", redirectLocation );
                
                exportpdf(client, redirectLocation);

            } else {
                // The response is invalid and did not provide the new location for
                // the resource.  Report an error or possibly handle the response
                // like a 404 Not Found error.
                is = post.getResponseBodyAsStream();

                fos = new java.io.FileOutputStream("target/login.html");

                IOUtils.copy(is, fos);
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);

            if (post != null) {
                post.releaseConnection();
            }
        }
        
    }
    
    public static void exportpdf( HttpClient client, String url/*, String pageId*/ ) throws Exception {
        java.io.InputStream is = null;
        java.io.FileOutputStream fos = null;
        GetMethod get = null;
        try {
            //final String req = String.format("%s/%s?pageId=%s", url, Format.PDF.url, pageId);
            //System.out.println(req);

            get = new GetMethod(url);
            get.setRequestHeader("X-Atlassian-Token", "no-check");
            int statusCode = client.executeMethod(get);

            
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + get.getStatusLine());
            }

            for( Header h : get.getResponseHeaders() ) {
                System.out.printf("header [%s]=[%s]\n", h.getName(), h.getValue() );
                
            }
            
            String redirectLocation;
            Header locationHeader = get.getResponseHeader("Location");
            if (locationHeader != null) {
                redirectLocation = locationHeader.getValue();
                            System.out.printf("redirect to [%s]\n", redirectLocation );

            } else {
                // The response is invalid and did not provide the new location for
                // the resource.  Report an error or possibly handle the response
                // like a 404 Not Found error.
            }
            // Read the response body.
            //final byte[] responseBody = get.getResponseBody();
            is = get.getResponseBodyAsStream();
            
            fos = new java.io.FileOutputStream("target/out.pdf");

            IOUtils.copy(is, fos);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);

            if (get != null) {
                get.releaseConnection();
            }
        }
        
    }
    
    public static void main(String args[]) throws Exception {

        final String url = URL;
        final String username = USERNAME;
        final String password = PASSWORD;

        Confluence.ProxyInfo proxyInfo = null;

        Confluence confluence = null;

        confluence = ConfluenceFactory.createInstanceDetectingVersion(
                url.concat("/rpc/xmlrpc"), //args[0], 
                proxyInfo,
                username, //args[1], 
                password); //args[2]);

        Page page = confluence.getPage("CIRC", "Best Movies");

        HttpClient client = new HttpClient();
        /*
        client.getState().setCredentials(
                    new AuthScope("support.softphone.it", 80, "realm"),
                    new UsernamePasswordCredentials(username, password)
            );
            
        */    
       client.getParams().setCookiePolicy( CookiePolicy.BROWSER_COMPATIBILITY);
       client.getParams().setParameter(
    	            CredentialsProvider.PROVIDER, new CredentialsProvider() {

            @Override
            public Credentials getCredentials(AuthScheme as, String string, int i, boolean bln) throws CredentialsNotAvailableException {
                return new UsernamePasswordCredentials(username, password);
            }
                        
        }); 

        login( client, url, username, password, String.format( "%s?pageId=%s", Format.PDF.url, page.getId()));
        
    }
    
    public static void usingHttp() throws Exception {

        final String url = URL;
        final String username = USERNAME;
        final String password = PASSWORD;

        Confluence.ProxyInfo proxyInfo = null;

        Confluence confluence = confluence = ConfluenceFactory.createInstanceDetectingVersion(
                url.concat("/rpc/xmlrpc"), //args[0], 
                proxyInfo,
                username, //args[1], 
                password); //args[2]);

        Page page = confluence.getPage("CIRC", "Best Movies");

        java.io.InputStream is = null;
        java.io.FileOutputStream fos = null;
        try {
            final String req = String.format("%s/%s?pageId=%s", url, Format.PDF.url, page.getId());
            System.out.println(req);
            java.net.URL _url = new java.net.URL(req);

            HttpURLConnection urlConnection = (HttpURLConnection) _url.openConnection();
            //HttpURLConnection.setFollowRedirects(true);
            //urlConnection.setInstanceFollowRedirects(true);
            String userpass = username + ":" + password;

            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            urlConnection.addRequestProperty("Authorization", basicAuth);
            urlConnection.addRequestProperty("X-Atlassian-Token", "no-check");
            urlConnection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");

            urlConnection.setUseCaches(false);

            is = urlConnection.getInputStream();

            fos = new java.io.FileOutputStream("target/out.pdf");

            IOUtils.copy(is, fos);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
        }
    }

}
