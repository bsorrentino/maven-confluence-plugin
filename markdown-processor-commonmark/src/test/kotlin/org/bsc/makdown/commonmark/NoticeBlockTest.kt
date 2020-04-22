package org.bsc.makdown.commonmark

import org.apache.commons.io.IOUtils
import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownParserContext
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor
import org.commonmark.node.Block
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.regex.Pattern


class NoticeBlockTest {

    val pattern = Pattern.compile("^>\\s+[*][*]([Ww]arning|[Nn]ote|[Ii]nfo|[Tt]ip)[:][*][*]\\s*(.*)$")

    @Test
    fun `pattern match tip with title`() {

        val match = pattern.matcher("> **tip:** About you")

        assertNotNull( match )
        assertTrue( match.matches() )
        assertEquals( 2 ,match.groupCount())
        assertEquals( "tip" ,match.group(1))
        assertEquals( "About you" ,match.group(2))
    }
    @Test
    fun `pattern match tip without title`() {

        val match = pattern.matcher("> **tip:**")

        assertNotNull( match )
        assertTrue( match.matches() )
        assertEquals( 2 ,match.groupCount())
        assertEquals( "tip" ,match.group(1))
        assertEquals( "" ,match.group(2))

    }

    private fun parse():String? = try {
        this.javaClass.classLoader.getResourceAsStream( "cheatsheet/noticeblock.md").use {
            val root = CommonmarkConfluenceWikiVisitor.parser().parse(IOUtils.toString(it))

            val visitor = CommonmarkConfluenceWikiVisitor(object : MarkdownParserContext<Block?> {
                override fun getSite(): Optional<Site> {
                    return Optional.empty()
                }

                override fun getHomePageTitle(): Optional<String> {
                    return Optional.empty()
                }

                override fun isImagePrefixEnabled(): Boolean {
                    return false
                }

                override fun notImplementedYet(node: Block?) {
                    TODO("Not yet implemented")
                }
            })

            root.accept(visitor)

            return visitor.toString().trimEnd()
        }
    }
    catch( e:Exception) {
        //Assertions.fail()
        null;
    }

    @Test
    //@Ignore
    fun `parse notice blocks`()  = assertEquals( parse(), """
        h1. Notice Block Syntax
        h2. info
        {info:title=About me}
        {quote}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        {quote}
        {info}
        
        h2. Note without title
        {note:title=}
        Contents of my note
        {note}
        
        h2. Tip with title
        {tip:title=About you}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        {tip}
        
        h2. Warning with complex content
        {warning:title=About him}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        *  one
        *  two
        have a *strong* and _pure_ feeling
        {warning}
    """.trimIndent() )

}