/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import org.codehaus.swizzle.confluence.XMLRPCConfluenceServiceFactory;

/**
 *
 * @author softphone
 */
public class ConfluenceServiceFactory {

    public static ConfluenceService createInstance(String endPoint, ConfluenceProxy proxyInfo, String username, String password) throws Exception {
        
        return XMLRPCConfluenceServiceFactory.createInstanceDetectingVersion(endPoint, proxyInfo, username, password);
    }
    
}
