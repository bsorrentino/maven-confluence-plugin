package org.bsc.markdown;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;

import java.io.IOException;
import java.util.Optional;

public interface MarkdownProcessor {

    /**
     * markdown processor identifier used to choose which procerror use at run-time
     *
     * @return identifier
     */
    String getName();

    /**
     *
     * @param site
     * @param child
     * @param page
     * @param content
     * @param homePageTitle
     * @return
     * @throws IOException
     */
    String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final String homePageTitle) throws IOException;
}
