/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import java.net.MalformedURLException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author bsorrentino
 */
public class Confluence2 extends Confluence {

    public Confluence2(Confluence c) {
        super(c);
    }

    public Confluence2(String endpoint) throws MalformedURLException {
        super(endpoint);
    }

    @Override
    protected String getServicePrefix() {
        return "confluence2.";
    }
    
}
