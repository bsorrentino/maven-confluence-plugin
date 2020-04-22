package org.bsc.markdown;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class MarkdownProcessorInfo {
    private String name ;

    public Optional<String> optName() {
        return ofNullable(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MarkdownProcessorInfo() {
        this(null);
    }

    public MarkdownProcessorInfo(String name) {
        this.name = name;
    }
}
