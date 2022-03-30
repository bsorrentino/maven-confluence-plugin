package org.bsc.makdown.commonmark;

import org.bsc.confluence.model.Site
import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URI
import java.nio.file.Paths

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

        assertEquals( """String\[x\]""", CommonmarkConfluenceWikiVisitor.escapeMarkdownText( null,"""String\[x]""" ) )
        assertEquals( """String\[\]""", CommonmarkConfluenceWikiVisitor.escapeMarkdownText( null,"String[]" ) )
        assertEquals( """value = \{ \"Foo\", \"Bar\" \}""", CommonmarkConfluenceWikiVisitor.escapeMarkdownText( null, """value = { \"Foo\", \"Bar\" }""" ) )

    }
    @Test //@Ignore
    fun parse() {
        val content = parseResource()

        assertEquals("""
This check ensures that:

* It can check assignment of literals (e.g. {{@FooBar(lorem = "ipsum")}}, {{@FooBarArray(value = \{ "Foo", "Bar" \})}} )
* All {{@Transactional}} annotation usages are on method level and only on public methods
* Any method, belonging to a bean class and has an entity in its parameters or return type has {{@Transactional(propagation = Propagation.MANDATORY)}}
* An Aspect bean does not contain any {{@Transactional}} annotation

h2. Properties
||name||description||type||default values||since||
|patterns|classes excluded|[String\[\]|https://checkstyle.sourceforge.io/property_types.html#String.5B.5D]|{{[]}}|1.0|
        """.trimIndent(), content)
    }

}
