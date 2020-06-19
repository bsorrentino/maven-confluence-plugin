package org.bsc.mojo.configuration;


import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ScrollVersionsInfo {

    private String version = null;

    public String getVersion() {
        Objects.requireNonNull( "version is not set!");
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Optional<String> optVersion() {
        return ofNullable(version);
    }
}
