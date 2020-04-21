package org.bsc.markdown.commonmark;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;
import org.bsc.markdown.commonmark.ConfluenceWikiVisitor;
import org.commonmark.node.Block;
import org.commonmark.node.Node;
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

        final Node node =  ConfluenceWikiVisitor.parser().parse(content);

        final ConfluenceWikiVisitor visitor = new ConfluenceWikiVisitor(new MarkdownParserContext<Block>() {

            @Override
            public Optional<Site> getSite() {
                return Optional.empty();
            }

            @Override
            public void notImplementedYet(Block node) {

            }

            @Override
            public Optional<String> getHomePageTitle() {
                return Optional.empty();
            }

            @Override
            public boolean isImagePrefixEnabled() {
                return false;
            }
        });

        node.accept(visitor);

        return visitor.toString();
    }
}