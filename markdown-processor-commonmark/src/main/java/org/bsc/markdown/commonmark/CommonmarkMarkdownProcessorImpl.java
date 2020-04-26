package org.bsc.markdown.commonmark;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;
import org.commonmark.node.Block;
import org.commonmark.node.Node;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

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
    public String processMarkdown(
            final Site siteModel,
            final Site.Page pageModel,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final Optional<String> pagePrefixToApply) throws IOException {

        final Node node =  CommonmarkConfluenceWikiVisitor.parser().parse(content);

        final CommonmarkConfluenceWikiVisitor visitor = new CommonmarkConfluenceWikiVisitor(new MarkdownParserContext<Block>() {

            @Override
            public Optional<Site> getSite() {
                return ofNullable(siteModel);
            }

            @Override
            public Site.Page getPage() {
                return pageModel;
            }

            @Override
            public void notImplementedYet(Block node) {
                throw new UnsupportedOperationException(format("Node [%s] not supported yet.", node.getClass().getSimpleName() ));
            }

            @Override
            public Optional<String> getPagePrefixToApply() {
                return pagePrefixToApply;
            }

            @Override
            public boolean isLinkPrefixEnabled() {
                if( pageModel.isIgnoreVariables() ) return false;

                return page.map( p -> !p.getTitle().contains("[") ).orElse(true);
            }

        });

        node.accept(visitor);

        return visitor.toString();
    }
}