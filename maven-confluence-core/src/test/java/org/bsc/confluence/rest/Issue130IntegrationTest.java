/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.bsc.ssl.SSLFactories;
import org.bsc.ssl.YesHostnameVerifier;
import org.bsc.ssl.YesTrustManager;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author bsorrentino
 */
public class Issue130IntegrationTest extends AbstractRestConfluence {
    
    static {
        URL = "https://localhost:8443/rest/api";
    }
    
    @Before
    @Override
    public void initService() throws Exception {
      
        super.initService();
        
        
        try {
            // SSL Implementation
            final SSLSocketFactory sslSocketFactory = SSLFactories.newInstance( new YesTrustManager());
            Assert.assertThat(sslSocketFactory, IsNull.notNullValue());
            
            final X509TrustManager trustManager = new YesTrustManager();
            final HostnameVerifier hostnameVerifier = new YesHostnameVerifier();
            
            service.client
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    ;
            
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
 

}
