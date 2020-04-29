package org.bsc.confluence.rest.scrollversions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

public interface ScrollVersions {

    interface Model {

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Version {
            String id;
            String name;
            String precedingVersionId;
            boolean archived;
            boolean runtimeAccessible;
            String color;
        }
    }
}
