/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import org.bsc.ssl.SSLFactories;
import org.bsc.ssl.YesHostnameVerifier;
import org.bsc.ssl.YesTrustManager;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author bsorrentino
 */
@Ignore
public class Issue130IntegrationTest extends RESTConfluenceServiceIntegrationTest {
    
    @Test @Ignore
    public void dummy() {}

    @Before
    @Override
    public void initService() throws Exception {

        setConfluenceUrl("https://localhost:8443/rest/api");
        super.initService();
        
        try {
            // SSL Implementation
            final SSLSocketFactory sslSocketFactory = SSLFactories.newInstance( new YesTrustManager());
            Assert.assertThat(sslSocketFactory, IsNull.notNullValue());
            
            final X509TrustManager trustManager = new YesTrustManager();
            final HostnameVerifier hostnameVerifier = new YesHostnameVerifier();

            restConfluenceService.client
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    ;
            
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
 

}
