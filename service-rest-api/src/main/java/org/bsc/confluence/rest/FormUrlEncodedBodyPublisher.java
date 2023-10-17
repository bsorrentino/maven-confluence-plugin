package org.bsc.confluence.rest;

import java.net.http.HttpRequest;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class FormUrlEncodedBodyPublisher implements HttpRequest.BodyPublisher {

    private final byte[] data;

    public final String getContentType() {
        return "application/x-www-form-urlencoded";
    }
    public FormUrlEncodedBodyPublisher(Map<String, String> fields) {

        data = fields.entrySet().stream()
                .map(kv -> format("%s=%s",
                        URLEncoder.encode(kv.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(kv.getValue(), StandardCharsets.UTF_8)))
                .collect(Collectors.joining("&"))
                .getBytes(StandardCharsets.UTF_8);;

    }

    @Override
    public long contentLength() {
        return data.length;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        subscriber.onNext(ByteBuffer.wrap(data));
        subscriber.onComplete();
    }
}
