package org.bsc.markdown.commonmark;

import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;
import org.commonmark.node.Node;
import org.kohsuke.MetaInfServices;

import java.io.IOException;

@MetaInfServices(MarkdownProcessor.class)
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

        final Node node =  CommonmarkConfluenceWikiVisitor.parser().parse(content);

        final CommonmarkConfluenceWikiVisitor visitor = new CommonmarkConfluenceWikiVisitor( context );

        node.accept(visitor);

        return visitor.toString();
    }
}