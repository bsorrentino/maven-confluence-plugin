package org.bsc.markdown.pegdown;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;
import org.kohsuke.MetaInfServices;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@MetaInfServices(MarkdownProcessor.class)
public class PegdownMarkdownProcessorImpl implements MarkdownProcessor{

    @Override
    public String getName() {
        return "pegdown";
    }

    /**
     *
     * @param siteModel
     * @param pageModel
     * @param page
     * @param contents
     * @param homePageTitle
     * @return
     * @throws IOException
     */
    public String processMarkdown(
            final Site siteModel,
            final Site.Page pageModel,
            final Optional<ConfluenceService.Model.Page> page,
            final String contents,
            final Optional<String> prefixToApply) throws IOException {

        final PegDownProcessor p = new PegDownProcessor(PegdownConfluenceWikiVisitor.extensions());

        final RootNode root = p.parseMarkdown(contents.toCharArray());

        final PegdownConfluenceWikiVisitor ser = new PegdownConfluenceWikiVisitor(new MarkdownParserContext<Node>() {

            @Override
            public Optional<Site> getSite() {
                return Optional.of(siteModel);
            }

            @Override
            public Site.Page getPage() {
                return pageModel;
            }

            @Override
            public void notImplementedYet(Node node) {

                final int lc[] = PegdownConfluenceWikiVisitor.lineAndColFromNode(new String(contents), node);
                throw new UnsupportedOperationException(format("Node [%s] not supported yet. line=[%d] col=[%d]",
                        node.getClass().getSimpleName(), lc[0], lc[1]));
            }

            @Override
            public Optional<String> getPagePrefixToApply() {
                return prefixToApply;
            }

            @Override
            public boolean isLinkPrefixEnabled() {
                if( pageModel.isIgnoreVariables() ) return false;

                return page.map( p -> !p.getTitle().contains("[") ).orElse(true);
            }

        });

        root.accept(ser);

        return ser.toString();
    }

}
