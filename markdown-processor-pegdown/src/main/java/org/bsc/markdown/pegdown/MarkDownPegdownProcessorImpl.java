package org.bsc.markdown.pegdown;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownProcessor;
import org.kohsuke.MetaInfServices;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@MetaInfServices(MarkdownProcessor.class)
public class MarkDownPegdownProcessorImpl implements MarkdownProcessor{


    /**
     *
     * @param site
     * @param child
     * @param page
     * @param contents
     * @param homePageTitle
     * @return
     * @throws IOException
     */
    public String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String contents,
            final String homePageTitle) throws IOException {

        final PegDownProcessor p = new PegDownProcessor(ConfluenceWikiVisitor.extensions());

        final RootNode root = p.parseMarkdown(contents.toCharArray());

        final ConfluenceWikiVisitor ser = new ConfluenceWikiVisitor() {


            @Override
            protected Optional<Site> getSite() {
                return Optional.of(site);
            }

            @Override
            protected void notImplementedYet(Node node) {

                final int lc[] = ConfluenceWikiVisitor.lineAndColFromNode(new String(contents), node);
                throw new UnsupportedOperationException(format("Node [%s] not supported yet. line=[%d] col=[%d]",
                        node.getClass().getSimpleName(), lc[0], lc[1]));
            }

            @Override
            protected Optional<String> getHomePageTitle() {
                return Optional.ofNullable(homePageTitle);
            }

            @Override
            protected boolean isImagePrefixEnabled() {
                if( child.isIgnoreVariables() ) return false;

                return page.map( p -> !p.getTitle().contains("[") ).orElse(true);
            }

        };

        root.accept(ser);

        return ser.toString();
    }

}
