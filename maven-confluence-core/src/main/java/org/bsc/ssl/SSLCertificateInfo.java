/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

/**
 *
 * @author softphone
 */
public class SSLCertificateInfo {
        boolean ignore = true;
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

        public void setup( String endpoint ) {

            if ((ignore || hostnameVerifierClass != null || trustManagerClass != null)
                    && endpoint.startsWith("https://")) {
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
