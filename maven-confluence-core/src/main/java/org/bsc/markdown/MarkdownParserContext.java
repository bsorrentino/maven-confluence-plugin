package org.bsc.markdown;

import org.bsc.confluence.model.Site;

import java.util.Optional;

/**
 *
 * @param <N> specific parser node
 */
public interface MarkdownParserContext<N> {

    /**
     * The Site Model Object
     *
     * @return site object. nullable
     */
    Optional<Site> getSite();

    /**
     * the current Page Model Object
     *
     * @return
     */
    Site.Page getPage();

    /**
     * Strategy to publish 'not yet implemented' condition
     *
     * @param node
     */
    void notImplementedYet(N node);

    /**
     * the page prefix to apply
     *
     * @return page prefix to apply. nullable
     */
    Optional<String> getPagePrefixToApply();

    /**
     * indicates whether the prefix ${page.title} should be added or not
     *
     * @return use the prefix
     */
    boolean isLinkPrefixEnabled();

}
