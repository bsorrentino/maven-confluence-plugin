package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Issue282Test {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun parse() {
        val content = parseResource( this.javaClass, "issue282", this.site )

        assertEquals("""
        test coverage of at least 70%. (Measures to show this for each PR will need to be investigated)
        """.trimIndent(), content )
    }

}