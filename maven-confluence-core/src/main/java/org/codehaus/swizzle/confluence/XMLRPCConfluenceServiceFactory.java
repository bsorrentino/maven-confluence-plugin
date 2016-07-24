/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Credentials;

/**
 *
 * @author bsorrentino
 */
public class XMLRPCConfluenceServiceFactory {

    protected XMLRPCConfluenceServiceFactory() {}
    
    /**
     * 
     * @param url
     * @param proxyInfo
     * @return
     * @throws MalformedURLException
     * @throws SwizzleException
     * @throws URISyntaxException 
     */
    public static <T extends ConfluenceService>  T createInstanceDetectingVersion( String url, Credentials credentials, ConfluenceProxy proxyInfo ) throws Exception {
        if( url == null ) {
            throw new IllegalArgumentException("url argument is null!");
        }
        if( credentials == null ) {
            throw new IllegalArgumentException("credentials argument is null!");
        }
        
        final Confluence c = new Confluence(url, proxyInfo);
        
        c.login(credentials.username, credentials.password);
        
        final ServerInfo info = c.getServerInfo();
        
        return (T) new XMLRPCConfluenceServiceImpl( (info.getMajorVersion() < 4) ? c : new Confluence2(c), credentials );
        
    }
}
