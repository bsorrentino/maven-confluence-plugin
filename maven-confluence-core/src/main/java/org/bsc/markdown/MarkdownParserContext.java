package org.bsc.markdown;

import org.bsc.confluence.model.Site;

import java.util.Optional;

/**
 *
 * @param <N> specific parser node
 */
public interface MarkdownParserContext<N> {

    /**
     * The site object
     *
     * @return site object. nullable
     */
    Optional<Site> getSite();

    /**
     * Strategy to publish 'not yet implemented' condition
     *
     * @param node
     */
    void notImplementedYet(N node);

    /**
     * The home page title
     *
     * @return home page title. nullable
     */
    Optional<String> getHomePageTitle();

    /**
     * indicates whether the prefix ${page.title} should be added or not
     *
     * @return use the prefix
     */
    boolean isImagePrefixEnabled();

}
