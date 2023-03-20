package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownVisitorHelper.isConfluenceMacro
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

        val content = multiLineMatcher.group(2).trimEnd()
        assertEquals( """
            {excerpt:title=MyExcerpt}
            This is the content that I want to use as my excerpt.
            {excerpt}
            """.trimIndent(), content )
        assertTrue( isConfluenceMacro( content ), "$content\n It is not recognized as confluence macro!" )

    }

}