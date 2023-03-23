package org.bsc.makdown.commonmark;

import org.apache.commons.io.IOUtils
import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownParserContext
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor
import org.junit.jupiter.api.fail
import java.nio.charset.Charset
import java.util.*

fun newMarkdownParserContext( site: Site, linkPrefixEnabled: Boolean ) = object :MarkdownParserContext {
        override fun isSkipHtml() = false

        override fun getSite() = Optional.of(site)

        override fun getPage(): Optional<Site.Page> = Optional.of(site.home)

        override fun isLinkPrefixEnabled() = linkPrefixEnabled

}

fun parseContent(site: Site, content: String, linkPrefixEnabled: Boolean = true): String {

        val context = newMarkdownParserContext( site, linkPrefixEnabled)

        return  CommonmarkConfluenceWikiVisitor.parser().parseMarkdown(context, content ).trimEnd()

}


fun parseResource(type: Class<*>, name: String, site: Site): String? =
        try {
                type.classLoader.getResourceAsStream("$name.md").use {
                        parseContent(site, IOUtils.toString(it, Charset.defaultCharset()))
                }
        } catch (e: Exception) {
                fail( e )
                null;
        }

