package org.bsc.markdown;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.kohsuke.MetaInfServices;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@MetaInfServices(MarkdownProcessor.class)
public class MarkDownPegDownProcessorImpl implements MarkdownProcessor{


    /**
     *
     * @param content
     * @return
     */
    @Override
    public java.io.InputStream processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final java.io.InputStream content,
            final String homePageTitle) throws IOException {

        final char[] contents = IOUtils.toCharArray(content);

        final PegDownProcessor p = new PegDownProcessor(ToConfluenceSerializer.extensions());

        final RootNode root = p.parseMarkdown(contents);

        final ToConfluenceSerializer ser = new ToConfluenceSerializer() {


            @Override
            protected Optional<Site> getSite() {
                return Optional.of(site);
            }

            @Override
            protected void notImplementedYet(Node node) {

                final int lc[] = ToConfluenceSerializer.lineAndColFromNode(new String(contents), node);
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

        return new java.io.ByteArrayInputStream(ser.toString().getBytes());
    }

}
