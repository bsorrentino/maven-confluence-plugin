module maven.confluence.core {

    requires lombok;
    requires okhttp3;
    requires javax.json;

    requires org.apache.commons.codec;
    requires commons.httpclient;
    requires commons.io;
    requires commons.logging;

    requires xmlrpc.client;
    requires xmlrpc.common;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;

    requires java.logging;
    requires java.xml.bind;

    exports org.bsc.markdown;
}