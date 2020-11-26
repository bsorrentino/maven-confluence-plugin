package org.bsc.mojo.configuration;


import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ScrollVersionsInfo {

    private String version = null;
    private boolean skip = false;


    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getVersion() {
        Objects.requireNonNull( "version is not set!");
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Optional<String> optVersion() {
        return (skip) ? Optional.empty() : ofNullable(version);
    }
}
