/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author bsorrentino
 */
public class SSLCertificateInfo {
        boolean ignore = false;
        String hostnameVerifierClass;
        String trustManagerClass;

        protected static <T> T newClass(final String clazz, final Class<T> type) {
            try {
                final Class<?> loadedClass = Thread.currentThread().getContextClassLoader().loadClass(clazz);
                //create an instance of loaded class i.e. with newInstance (just works for classes with non-arg const).
                final Object initClass = loadedClass.newInstance();
                return type.cast(initClass);
            } catch (final ClassNotFoundException e) {
                final String msg = String.format("Could not found Class with name %s", clazz);
                throw new IllegalStateException( msg, e);
            } catch (final InstantiationException e){
                final String msg = String.format("Could create Instance of Class with name %s. Class must be concrete.",clazz);
                throw new IllegalStateException(msg, e);
            }catch (final IllegalAccessException e){
                final String msg = String.format("Could create Instance of Class with name %s. Class must have a no-arg constructor.",clazz);
                throw new IllegalStateException(msg, e);
            }
        }

        public final boolean isIgnore() {
            return ignore;
        }
        
        public X509TrustManager getTrustManager() {

            final X509TrustManager trustManager = (trustManagerClass != null) ?
                                    newClass(trustManagerClass, X509TrustManager.class) : 
                                    new YesTrustManager();
            return trustManager;
            
        }
        public SSLSocketFactory getSSLSocketFactory()  {

            try {
                
                final SSLSocketFactory sslSocketFactory = SSLFactories.newInstance( getTrustManager() );
    
                return sslSocketFactory;
                
            } catch (Exception ex) {
               throw new IllegalStateException(ex);
            }
        }
        
        public HostnameVerifier getHostnameVerifier() {
            
            final HostnameVerifier hostnameVerifier = (hostnameVerifierClass != null ?
                        newClass(hostnameVerifierClass, HostnameVerifier.class) : new YesHostnameVerifier());
            
            return hostnameVerifier;
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
