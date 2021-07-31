package org.bsc.makdown.commonmark;

import org.apache.commons.io.IOUtils
import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownParserContext
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor
import java.nio.charset.Charset
import java.util.*


fun parseContent(site: Site, content: String, linkPrefixEnabled: Boolean = true): String {

        val root = CommonmarkConfluenceWikiVisitor.parser().parse(content)

        val visitor = CommonmarkConfluenceWikiVisitor(object : MarkdownParserContext {

                override fun getSite() = Optional.of(site)

                override fun getPage(): Optional<Site.Page> = Optional.of(site.home)

                override fun isLinkPrefixEnabled() = linkPrefixEnabled

        })

        root.accept(visitor)

        return visitor.toString().trimEnd()

}

fun parseResource(type: Class<*>, name: String, site: Site): String? =
        try {
                type.classLoader.getResourceAsStream("$name.md").use {
                        parseContent(site, IOUtils.toString(it, Charset.defaultCharset()))
                }
        } catch (e: Exception) {
                //Assertions.fail()
                null;
        }

