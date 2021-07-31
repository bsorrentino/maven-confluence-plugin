open module maven.confluence.core {

    requires lombok;
    //requires okhttp3;

    requires org.apache.commons.codec;
    //requires commons.httpclient;
    // requires commons.io;
    requires org.apache.commons.io;
    //requires commons.logging;

    //requires xmlrpc.client;
    //requires xmlrpc.common;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.dataformat.xml;

    requires java.logging;
    requires java.xml.bind;
    requires java.json;

    exports org.bsc.markdown;
    exports org.bsc.preprocessor;
}