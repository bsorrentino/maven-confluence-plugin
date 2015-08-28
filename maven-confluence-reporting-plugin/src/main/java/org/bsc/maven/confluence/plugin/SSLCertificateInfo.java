/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.confluence.plugin;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import org.bsc.maven.confluence.plugin.ssl.SSLFactories;
import org.bsc.maven.confluence.plugin.ssl.YesHostnameVerifier;
import org.bsc.maven.confluence.plugin.ssl.YesTrustManager;
import static org.bsc.maven.confluence.plugin.AbstractBaseConfluenceMojo.newClass;

/**
 *
 * @author softphone
 */
public class SSLCertificateInfo {
        boolean ignore = true;
        String hostnameVerifierClass;
        String trustManagerClass;
        
        public void setup( AbstractBaseConfluenceMojo self ) {
            
            if ((ignore || hostnameVerifierClass != null || trustManagerClass != null)
                    && self.getEndPoint().startsWith("https://")) {
                try {
                    HttpsURLConnection.setDefaultSSLSocketFactory(
                            SSLFactories.newInstance(trustManagerClass != null ?
                                    newClass(trustManagerClass, TrustManager.class) : new YesTrustManager()));
                } catch (final Exception e) {
                    throw new IllegalStateException(e);
                }
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifierClass != null ?
                        newClass(hostnameVerifierClass, HostnameVerifier.class) : new YesHostnameVerifier());
            }
            
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("SSLCertificate")
                    .append('{')
                    .append("ignore:").append(ignore).append(',')
                    .append("hostnameVerifierClass:'").append(hostnameVerifierClass).append("',")
                    .append("trustManagerClass:'").append(trustManagerClass).append(',')
                    .append('}')
                    .toString();
        }
        
        
    
}
