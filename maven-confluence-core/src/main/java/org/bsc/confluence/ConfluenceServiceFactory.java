/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import org.bsc.confluence.ConfluenceService.Credentials;
import org.codehaus.swizzle.confluence.XMLRPCConfluenceServiceFactory;

/**
 *
 * @author softphone
 */
public class ConfluenceServiceFactory {

    public static ConfluenceService createInstance(String endPoint, Credentials credentials, ConfluenceProxy proxyInfo) throws Exception {
        
        return XMLRPCConfluenceServiceFactory.createInstanceDetectingVersion(endPoint, credentials, proxyInfo);
    }
    
}
