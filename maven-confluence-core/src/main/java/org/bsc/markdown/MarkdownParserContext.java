package org.bsc.markdown;

import org.bsc.confluence.model.Site;

import java.util.Optional;

public interface MarkdownParserContext<N> {

    Optional<Site> getSite();

    void notImplementedYet(N node);

    Optional<String> getHomePageTitle();

    boolean isImagePrefixEnabled();

}
