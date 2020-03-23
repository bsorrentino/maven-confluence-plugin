package org.bsc.markdown.commonmark;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownProcessor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.util.Optional;

@MetaInfServices(MarkdownProcessor.class)
public class MarkdownCommonmarkProcessorImpl implements MarkdownProcessor {

    /**
     * @param content
     * @return
     */
    @Override
    public String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final String homePageTitle) throws IOException {

        final Parser parser = Parser.builder().build();
        final Node node = parser.parse(content);
        final ConfluenceWikiVisitor visitor = new ConfluenceWikiVisitor();
        node.accept(visitor);


        return visitor.toString();
    }
}