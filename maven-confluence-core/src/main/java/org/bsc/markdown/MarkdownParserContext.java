package org.bsc.markdown;

import org.bsc.confluence.model.Site;

import java.util.Optional;

import static java.util.Optional.empty;

/**
 *
 */
public interface MarkdownParserContext {

    /**
     * The Site Model Object
     *
     * @return site object. nullable
     */
    default Optional<Site> getSite() { return empty(); }

    /**
     * the current Page Model Object
     *
     * @return  Page Model Object
     */
    default Optional<Site.Page> getPage() { return empty(); }

    /**
     * the page prefix to apply
     *
     * @return page prefix to apply. nullable
     */
    default Optional<String> getPagePrefixToApply() { return empty(); }

    /**
     * indicates whether the prefix ${page.title} should be added or not
     *
     * @return use the prefix
     */
    default boolean isLinkPrefixEnabled() { return true; }

}

