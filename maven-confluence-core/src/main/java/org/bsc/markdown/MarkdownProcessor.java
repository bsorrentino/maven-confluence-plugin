package org.bsc.markdown;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.model.Site;

import java.io.IOException;
import java.util.Optional;

public interface MarkdownProcessor {

    java.io.InputStream processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final java.io.InputStream content,
            final String homePageTitle) throws IOException;
}
