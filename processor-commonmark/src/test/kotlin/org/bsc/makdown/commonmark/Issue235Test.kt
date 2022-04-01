package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor.parseHTMLComment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Issue235Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun parseHtmlBlock() {

        val singleLineMatcher = parseHTMLComment("<!-- {xxxxxxx:yyyyyy}   -->")

        assertTrue( singleLineMatcher.matches() )
        assertEquals( 2, singleLineMatcher.groupCount() )
        assertEquals( "{xxxxxxx:yyyyyy}", singleLineMatcher.group(2).trimEnd() )

        val singleLineMatcherWithPrefixSpaces = parseHTMLComment("  <!-- {xxxxxxx:yyyyyy}   -->")

        assertTrue( singleLineMatcherWithPrefixSpaces.matches() )
        assertEquals( 2, singleLineMatcherWithPrefixSpaces.groupCount() )
        assertEquals( "  ", singleLineMatcherWithPrefixSpaces.group(1) )
        assertEquals( "{xxxxxxx:yyyyyy}", singleLineMatcherWithPrefixSpaces.group(2).trimEnd() )

        val multiLineMatcher = parseHTMLComment("""<!-- 
            
            {xxxxxxx:yyyyyy}   
            
            -->
            """.trimIndent())

        assertTrue( multiLineMatcher.matches() )
        assertEquals( 2, multiLineMatcher.groupCount() )
        assertEquals( "{xxxxxxx:yyyyyy}", multiLineMatcher.group(2).trimEnd() )

    }

    @Test
    fun `parse generic macros`() {
        val content = parseResource( this.javaClass, "issue235", this.site )

        assertEquals("""
            {html}
            <!-- 
            Hello world!
            -->
            {html}
            {macro} 
            {macro:scope} 
            {macros:scope|property=value} 
            {macros:scope|property1=value1, property2=value2}
        """.trimIndent(), content )
    }

    @Test
    fun `parse specific macros`() {
        val content =  parseResource( this.javaClass, "macro", this.site )
        assertEquals("""
            {toc}
            
            h1. This is table of content
            {toc:minLevel=2} 
            {toc:type=flat|separator=pipe:minLevel=2} 
            * Menu
            ** Menu item1
            ** Menu item2
            * Related pages
              {children:depth=1}
            """.trimIndent(), content )
    }
}