/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import java.net.MalformedURLException;

/**
 *
 * @author bsorrentino
 */
public class ConfluenceFactory {

    protected ConfluenceFactory() {}
    
    
    public static Confluence createInstanceVersion3x( String url, Confluence.ProxyInfo proxyInfo ) throws MalformedURLException {
        
        return new Confluence( url, proxyInfo );
        
    }

    public static Confluence createInstanceVersion4x( String url, Confluence.ProxyInfo proxyInfo ) throws MalformedURLException {
        
        return new Confluence2( url, proxyInfo );
        
    }

    public static Confluence createInstanceDetectingVersion( String url, Confluence.ProxyInfo proxyInfo, String login, String password ) throws MalformedURLException, SwizzleException {
        
        Confluence c = new Confluence(url, proxyInfo);
        c.login(login, password);
        
        ServerInfo info = c.getServerInfo();
        
        return ( info.getMajorVersion() < 4  ) ? c : new Confluence2( c );
        
    }
}
