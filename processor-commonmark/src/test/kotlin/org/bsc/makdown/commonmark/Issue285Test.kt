package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownVisitorHelper.isConfluenceMacroOrVariable
import org.bsc.markdown.MarkdownVisitorHelper.parseConfluenceMacro
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor.parseHTMLComment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Issue285Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun `parse excerpt macros`() {

        val excerptMacro = """
            <!-- {excerpt:title=MyExcerpt}
            This is the content that I want to use as my excerpt.
            {excerpt} -->
        """.trimIndent()

        val multiLineMatcher = parseHTMLComment(excerptMacro)

        assertTrue( multiLineMatcher.matches() )
        assertEquals( 2, multiLineMatcher.groupCount() )

        assertEquals(  "",  multiLineMatcher.group(1).trimEnd() )

        val content = multiLineMatcher.group(2).trimEnd()
        assertEquals( """
            {excerpt:title=MyExcerpt}
            This is the content that I want to use as my excerpt.
            {excerpt}
            """.trimIndent(), content )
        assertTrue( isConfluenceMacroOrVariable( content ), "$content\n It is not recognized as confluence macro!" )

    }

    @Test
    fun `parse excerpt macros with markdown content`() {

        val excerptMacro = """
            <!-- {excerpt:title=MyExcerpt}
            # Title
            This is the content that I **want** to use as my excerpt.
            {excerpt} -->
        """.trimIndent()

        val multiLineMatcher = parseHTMLComment(excerptMacro)

        assertTrue( multiLineMatcher.matches() )
        assertEquals( 2, multiLineMatcher.groupCount() )

        assertEquals(  "",  multiLineMatcher.group(1).trimEnd() )

        val content = multiLineMatcher.group(2).trimEnd()

        assertEquals( """
            {excerpt:title=MyExcerpt}
            # Title
            This is the content that I **want** to use as my excerpt.
            {excerpt}
            """.trimIndent(), content )
        assertTrue( isConfluenceMacroOrVariable( content ), "$content\n It is not recognized as confluence macro!" )

        val m = parseConfluenceMacro(content)

        assertTrue( m.matches() )
        assertEquals( 3, m.groupCount())
        assertEquals( """
            
            # Title
            This is the content that I **want** to use as my excerpt.
            
            """.trimIndent(), m.group(2))


        val mdContent = parseContent( site, m.group(2) )

        assertEquals( """
            h1. Title
            This is the content that I *want* to use as my excerpt.
            """.trimIndent(), mdContent )

        assertEquals( """
            {excerpt:title=MyExcerpt}
            h1. Title
            This is the content that I *want* to use as my excerpt.
            {excerpt}
            """.trimIndent(), "${m.group(1)}\n$mdContent\n${m.group(3)}" )

    }
    @Test
    fun `parse document with excerpt macros having markdown content`() {

        val mdContent = """
            <!-- {excerpt:title=MyExcerpt}
            # Title
            This is the content that I **want** to use as my excerpt.
            {excerpt} -->
            
            # New Title
            this is a paragraph                      
        """.trimIndent()


        val wikiContent = parseContent( site, mdContent )

        assertEquals( """
            {excerpt:title=MyExcerpt}
            h1. Title
            This is the content that I *want* to use as my excerpt.
            
            
            {excerpt}
            
            h1. New Title
            this is a paragraph
            """.trimIndent(), wikiContent )

    }

    @Test
    fun `parse document with excerpt macros without markdown content`() {

        val mdContent = """
            <!-- {excerpt:title=MyExcerpt}
            h1. Title
            This is the content that I _want_ to use as my excerpt.
            {excerpt} -->
            
            # New Title
            this is a paragraph                     
        """.trimIndent()


        val wikiContent = parseContent( site, mdContent )

        assertEquals( """
            {excerpt:title=MyExcerpt}
            h1. Title
            This is the content that I _want_ to use as my excerpt.
            
            
            {excerpt}
  
            h1. New Title
            this is a paragraph
            """.trimIndent(), wikiContent )

    }

}