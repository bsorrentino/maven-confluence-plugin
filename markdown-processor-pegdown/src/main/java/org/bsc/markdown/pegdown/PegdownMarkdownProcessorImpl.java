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
     * @param node
     */
    private void notImplementedYet(Node node) {

    }

    /**
     *
     * @param context
     * @param contents
     * @return
     * @throws IOException
     */
    public String processMarkdown( MarkdownParserContext context, String contents ) throws IOException {

        final PegDownProcessor p = new PegDownProcessor(PegdownConfluenceWikiVisitor.extensions());

        final RootNode root = p.parseMarkdown(contents.toCharArray());

        final PegdownConfluenceWikiVisitor ser = new PegdownConfluenceWikiVisitor( context, node -> {

            final int lc[] = PegdownConfluenceWikiVisitor.lineAndColFromNode( contents, node);
            throw new UnsupportedOperationException(format("Node [%s] not supported yet. line=[%d] col=[%d]",
                    node.getClass().getSimpleName(), lc[0], lc[1]));

        });

        root.accept(ser);

        return ser.toString();
    }

}
