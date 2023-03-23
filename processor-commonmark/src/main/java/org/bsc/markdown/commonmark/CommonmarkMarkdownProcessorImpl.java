package org.bsc.markdown.commonmark;

import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;
import org.commonmark.node.Node;

import java.io.IOException;

/**
 *
 */
public class CommonmarkMarkdownProcessorImpl implements MarkdownProcessor {

    @Override
    public String getName() {
        return "commonmark";
    }

    /**
     * @param content
     * @return
     */
    @Override
    public String processMarkdown( MarkdownParserContext context, String content ) throws IOException {
        return CommonmarkConfluenceWikiVisitor.parser().parseMarkdown( context, content );
    }
}