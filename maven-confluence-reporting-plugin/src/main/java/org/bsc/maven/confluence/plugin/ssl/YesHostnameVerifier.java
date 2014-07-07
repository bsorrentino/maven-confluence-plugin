package org.bsc.maven.confluence.plugin.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class YesHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        return true;
    }
}

