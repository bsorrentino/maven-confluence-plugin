package org.bsc.markdown.flexmark;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;
import org.bsc.markdown.MarkdownProcessor;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.util.Optional;

@MetaInfServices(MarkdownProcessor.class)
public class FlexmarkMarkDownProcessorImpl implements MarkdownProcessor {

    @Override
    public String getName() {
        return "flexmark";
    }

    /**
     *
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


        final Parser parser = Parser.builder(FlexmarkConfluenceWikiVisitor.OPTIONS()).build();

        final Document doc = parser.parse( content );

        throw new UnsupportedOperationException("Flexmark parser is not implememnted yet!");

    }
}
