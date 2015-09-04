package org.bsc.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class SSLFactories {
    public static SSLSocketFactory newInstance(final TrustManager trustManager) throws Exception {
        final SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { trustManager }, null);
        return ctx.getSocketFactory();
    }

    private SSLFactories() {
        // no-op
    }
}
