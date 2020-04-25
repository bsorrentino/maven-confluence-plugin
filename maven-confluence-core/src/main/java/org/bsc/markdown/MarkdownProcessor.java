package org.bsc.markdown;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;

import java.io.IOException;
import java.util.Optional;

/**
 * Markdown Processor interface
 */
public interface MarkdownProcessor {

    /**
     * markdown processor identifier used to choose which procerror use at run-time
     *
     * @return identifier
     */
    String getName();

    /**
     *
     * @param siteModel - Site model instance
     * @param pageModel - current processing Page Model instance
     * @param page - current processing page instance. Valid only if we are updating content of existent page
     * @param content - content to process
     * @param pagePrefixToApply - prefix to apply. Valid only if 'childrenTitlesPrefixed' parameter is true
     * @return processed (i.e. translated) content
     * @throws IOException
     */
    String processMarkdown(
            final Site siteModel,
            final Site.Page pageModel,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final Optional<String> pagePrefixToApply) throws IOException;
}
