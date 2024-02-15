package org.bsc.mojo.configuration;

import org.bsc.markdown.MarkdownProcessor;

/**
 *
 */
public class MarkdownProcessorInfo {

    public boolean isSkipHtml() {
        return MarkdownProcessor.shared.isSkipHtml();
    }

    /**
     * set skip html tags parsing
     * @param skipHtml
     */
    public void setSkipHtml(boolean skipHtml) {
        MarkdownProcessor.shared.setSkipHtml( skipHtml );
    }

    public String getName() {
        return MarkdownProcessor.shared.getName();
    }

    public void setName(String name) {

        MarkdownProcessor.shared.setName(name);
    }

    public MarkdownProcessorInfo() {
    }
}
