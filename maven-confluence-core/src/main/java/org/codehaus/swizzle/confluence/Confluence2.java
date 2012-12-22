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
public class Confluence2 extends Confluence {

    protected static final String SERVICE_PREFIX_2 = "confluence2.";
   
    protected Confluence2(Confluence c) {
        super(c);
    }
/*
    protected Confluence2(String endpoint) throws MalformedURLException {
        super(endpoint);
    }
*/
    protected Confluence2(String endpoint, ProxyInfo proxyInfo) throws MalformedURLException {
        super(endpoint, proxyInfo);
    }
    
    

    @Override
    protected String getServicePrefix() {
        return SERVICE_PREFIX_2;
    }
    
}
