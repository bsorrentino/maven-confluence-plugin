package org.bsc.makdown.commonmark;

import org.bsc.confluence.model.Site
import org.bsc.markdown.MarkdownVisitorHelper
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URI
import java.nio.file.Paths
import java.util.regex.Pattern

class Issue226Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))

        val home = Site.Home()
        home.uri = URI("./page.md")
        home.name = "page"

        setHome(home)

    }

    private fun parseResource():String? = parseResource(this.javaClass, "issue226", this.site)

    @Test
    fun `escape markdown text`() {
        val input = """String\[x]"""

        val leftS = Regex( """^(\\)(\[)""" )
        val rightS = Regex("""(^\\])|]""")
        val result = input.replace( leftS,"\\\\$1" ).replace(rightS, "\\\\]")

        assertEquals( """String\[x\]""", result  )

        assertEquals( """String\[x\]""", MarkdownVisitorHelper.escapeMarkdownText( """String\[x]""" ) )
        assertEquals( """String\[\]""", MarkdownVisitorHelper.escapeMarkdownText( "String[]" ) )

    }
    @Test
    fun parse() {
        val content = parseResource()

        assertEquals("""
h2. Properties
||name||description||type||default values||since||
|packagePatterns|regular expression|[String\[\]|https://checkstyle.sourceforge.io/property_types.html#String.5B.5D]|{{[]}}|1.2.0|
|annotationNames|full qualified class name or simple class name of an annotation|[String\[\]|https://checkstyle.sourceforge.io/property_types.html#String.5B.5D]|{{[]}}|1.0.0|
        """.trimIndent(), content)
    }

}
