package org.bsc.confluence.rest.model;

import javax.json.JsonObject;
import javax.json.JsonValue;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public interface IdHelper {

    static long getId( JsonObject o ) {
        final JsonValue value = o.get("id");
        switch(value.getValueType()) {
            case NUMBER:
                return ofNullable(o.getJsonNumber("id"))
                        .map( n -> n.longValue())
                        .orElseThrow( () -> new IllegalArgumentException( "id not found in object!"));
            case STRING:
                return ofNullable(o.getString("id"))
                        .map( s -> Long.valueOf(s))
                        .orElseThrow( () -> new IllegalArgumentException( "id not found in object!"));
            default:
                throw new IllegalArgumentException( format("wrong type for 'id' - %s", value.getValueType() ));
        }
    }

}
