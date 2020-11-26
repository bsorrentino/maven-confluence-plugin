package org.bsc.mojo.configuration;

import org.bsc.markdown.MarkdownProcessor;

/**
 *
 */
public class MarkdownProcessorInfo {

    public String getName() {

        return MarkdownProcessor.shared.getName();
    }

    public void setName(String name) {

        MarkdownProcessor.shared.setName(name);
    }

    public MarkdownProcessorInfo() {
    }
}
