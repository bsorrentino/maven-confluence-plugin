/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import org.bsc.confluence.ExportFormat;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.ssl.SSLCertificateInfo;

/**
 *
 * @author bsorrentino
 */
public class SwizzleApp {

    @Parameter(names="-h", description="confluence url", required = true)
    private String url;
    @Parameter( names = "-u", description ="confluence username", required = true)
    private String username ;
    @Parameter( names="-p", description = "confluence password", password = true, required = true)
    private String password ;
    @Parameter( names="--space", description = "confluence site", required = true)
    private String space ;
    @Parameter( names="--page", description = "confluence page")
    private String page = "Home";
 
    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String args[]) throws Exception {

        final SwizzleApp app = new SwizzleApp();
        
        for( String arg : args ) {
            System.out.println( arg );
        }
        
        new JCommander(app,args);
               
        ConfluenceProxy proxyInfo = null;

        final ConfluenceService.Credentials credentials = new ConfluenceService.Credentials(app.username,app.password);
        final SSLCertificateInfo sslInfo = new SSLCertificateInfo();
        
        final XMLRPCConfluenceServiceImpl confluence = 
            XMLRPCConfluenceServiceImpl.createInstanceDetectingVersion(
                    app.url.concat("/rpc/xmlrpc"),
                    credentials,
                    proxyInfo, sslInfo);

        ConfluenceExportDecorator exporter = 
                new ConfluenceExportDecorator(  confluence.connection, 
                                                app.url, 
                                                app.username, 
                                                app.password);
        exporter.exportPage(app.space, app.page, ExportFormat.DOC, new java.io.File("target", app.page.concat(".pdf")));
      
    }
    
    /**
     * 
     * @throws Exception 
     */
    private void usingHttp( ) throws Exception {

        ConfluenceProxy proxyInfo = null;

        final ConfluenceService.Credentials credentials = 
                new ConfluenceService.Credentials(username /*args[1]*/,password/*args[2]*/);

        final SSLCertificateInfo sslInfo = new SSLCertificateInfo();

        final XMLRPCConfluenceServiceImpl confluence = 
            XMLRPCConfluenceServiceImpl.createInstanceDetectingVersion(
                        url.concat("/rpc/xmlrpc"), //args[0],
                        credentials,
                        proxyInfo, sslInfo); 

        Model.Page page = confluence.getPage("CIRC", "Best Movies");

        java.io.InputStream is = null;
        java.io.FileOutputStream fos = null;
        try {
            final String req = String.format("%s/%s?pageId=%s", url, ExportFormat.PDF.url, page.getId());
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
