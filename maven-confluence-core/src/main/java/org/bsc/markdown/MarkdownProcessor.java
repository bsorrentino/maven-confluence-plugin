package org.bsc.markdown;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;

import java.io.IOException;
import java.util.Optional;

public interface MarkdownProcessor {

    /**
     * use alternative version below
     *
     * @param site
     * @param child
     * @param page
     * @param content
     * @param homePageTitle
     * @return
     * @throws IOException
     */
    @Deprecated
    default java.io.InputStream processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final java.io.InputStream content,
            final String homePageTitle) throws IOException {

        final char[] chars = IOUtils.toCharArray(content);

        final String result =  processMarkdown( site, child, page, String.valueOf(chars), homePageTitle);

        return new java.io.ByteArrayInputStream( result.getBytes() );
    }

    String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final String homePageTitle) throws IOException;
}
