package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor.parseHTMLComment
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Paths
import java.util.regex.Pattern

class Issue235Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun parseHtmlBlock() {

        val singleLineMatcher = parseHTMLComment("<!-- {xxxxxxx:yyyyyy}   -->")

        assertTrue( singleLineMatcher.matches() )
        assertEquals( 1, singleLineMatcher.groupCount() )
        assertEquals( "{xxxxxxx:yyyyyy}", singleLineMatcher.group(1).trimEnd() )

        val multiLineMatcher = parseHTMLComment("""<!-- 
            
            {xxxxxxx:yyyyyy}   
            
            -->
            """.trimIndent())

        assertTrue( multiLineMatcher.matches() )
        assertEquals( 1, multiLineMatcher.groupCount() )
        assertEquals( "{xxxxxxx:yyyyyy}", multiLineMatcher.group(1).trimEnd() )

    }

    @Test
    fun parseMacros() {
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

}