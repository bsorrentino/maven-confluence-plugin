package org.bsc.makdown.commonmark;

import org.bsc.confluence.model.Site
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URI
import java.nio.file.Paths

class Issue264Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))

        val home = Site.Home()
        home.uri = URI("./page.md")
        home.name = "page"

        setHome(home)

    }

    private fun parseContent():String {
        val content = """            
|Property                     | Default            | Description            |
|-----------------------------|--------------------|--------------------------|  
| A                           |                    | Desc01                 |
        """.trimIndent()
        return parseContent(this.site, content)
    }

    @Test //@Ignore
    fun parse() {
        val content = parseContent()

        assertEquals("""
||Property||Default||Description||
|A||Desc01|
        """.trimIndent(), content)
    }

}
