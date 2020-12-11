package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class MacroTest {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    private fun parse():String? = parseResource( this.javaClass, "macro", this.site )

    @Test
    //@Ignore
    fun `parse macros`()  = Assertions.assertEquals(
            """
            h1. This is table of content
            {toc} 
            {toc:minLevel=2} 
            {toc:type=flat|separator=pipe:minLevel=2}
            """.trimIndent(),
            parse() )
    
}