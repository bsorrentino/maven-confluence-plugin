package org.bsc.confluence

import org.bsc.confluence.ConfluenceHtmlUtils.replaceHTML
import org.bsc.confluence.ConfluenceUtils.sanitizeLabel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths


class ConfluenceUtilsTest {
    @Test
    @Throws(Exception::class)
    fun decode() {
        val input = "<coDe>[I][RE]'.*moon[^dab]+'</CODE> : <EM>all</Em> <u>implemented</U> <s>specifications</S> <DEL>having</del> <Strong>the</Strong> <i>RE<I> '.*moon[^dab]+'<BR/>"
        val decoded = replaceHTML(input)
        assertEquals("{{\\[I\\]\\[RE\\]'.\\*moon\\[^dab\\]\\+'}} : _all_ +implemented+ -specifications- -having- *the* _RE_ '.\\*moon\\[^dab\\]\\+'\\\\", decoded)
    }

    @Test
    @Disabled
    @Throws(Exception::class)
    fun pathTest() {

        //File f = new File(".");
        val pp = Paths.get("http://test.xml")
        assertFalse(Files.exists(pp))
        val p1 = Paths.get("classpath:test.xml")
        assertFalse(Files.exists(p1))
    }

    @Test
    fun sanitationNotNeeded() = assertEquals( "label", sanitizeLabel("label"))

    @Test
    fun sanitation() = assertEquals( "labe-l", sanitizeLabel("labe:l"))

    @Test
    fun sanitationFull() = assertEquals( "-------------------------------", sanitizeLabel(": ; , . , ? & [ ] ( ) # ^ * ! @") )
}