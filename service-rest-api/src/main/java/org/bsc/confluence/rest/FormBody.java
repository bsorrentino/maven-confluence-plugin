package org.bsc.confluence.rest;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public final class FormBody {
    public static String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    static public class Builder {

        private final Map<String,String> fields = new HashMap<>();

        public Builder add( String name, String value ) {
            fields.put(name,value);
            return this;
        }

        public HttpRequest.BodyPublisher build() {
            return HttpRequest.BodyPublishers.ofString(
                    fields.entrySet().stream()
                            .map(kv -> format("%s=%s",
                                    URLEncoder.encode(kv.getKey(), StandardCharsets.UTF_8),
                                    URLEncoder.encode(kv.getValue(), StandardCharsets.UTF_8)))
                            .collect(Collectors.joining("&"))
            );
        }

    }
}
