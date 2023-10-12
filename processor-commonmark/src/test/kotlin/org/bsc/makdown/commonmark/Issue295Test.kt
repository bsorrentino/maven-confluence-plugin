package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownVisitorHelper.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Issue295Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun `escape text between curly braces`() {

        assertEquals( "\\{0,128}", escapeMarkdownText( "{0,128}", EscapeTextEnum.LeftCurlyBrace ))
        assertEquals( "\\{0,128\\}", escapeMarkdownText( "{0,128}",
                EscapeTextEnum.LeftCurlyBrace, EscapeTextEnum.RightCurlyBrace ))
        assertEquals( "\\[0,128]", escapeMarkdownText( "[0,128]", EscapeTextEnum.LeftSquareBrace ))
        assertEquals( "\\[0,128\\]", escapeMarkdownText( "[0,128]",
            EscapeTextEnum.LeftSquareBrace, EscapeTextEnum.RightSquareBrace ))
    }

    @Test
    fun parse() {
        val content = parseResource( this.javaClass, "issue295", this.site )

        assertEquals("""
        ||Property||Default||Required||Description||Example||
        |someEntry|{{\{0,128}}}|false|value that is being misinterpreted as a macro|-|
        """.trimIndent(), content )
    }

}