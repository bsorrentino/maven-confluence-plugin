package org.bsc.ssl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class YesTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
        // no-op
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
        // no-op
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
