/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;

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
     * @param login
     * @param password
     * @return
     * @throws MalformedURLException
     * @throws SwizzleException
     * @throws URISyntaxException 
     */
    public static XMLRPCConfluenceServiceImpl createInstanceDetectingVersion( String url, ConfluenceProxy proxyInfo, String login, String password ) throws MalformedURLException, SwizzleException, URISyntaxException {
        
        final Confluence c = new Confluence(url, proxyInfo);
        
        c.login(login, password);
        
        final ServerInfo info = c.getServerInfo();
        
        return new XMLRPCConfluenceServiceImpl( (info.getMajorVersion() < 4) ? c : new Confluence2(c) );
        
    }
}
