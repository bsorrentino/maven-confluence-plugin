package org.bsc.markdown;

import java.io.IOException;

/**
 * Markdown Processor interface
 */
public interface MarkdownProcessor {

    /**
     * markdown processor identifier used to choose which procerror use at run-time
     *
     * @return identifier
     */
    String getName();

    /**
     *
     * @param context
     * @param content
     * @return
     * @throws IOException
     */
    String processMarkdown( MarkdownParserContext context, String content ) throws IOException;

    default String processMarkdown( String content ) throws IOException {
        return processMarkdown(new MarkdownParserContext() {}, content);
    }
}
