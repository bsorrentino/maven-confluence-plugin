package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.util.regex.Pattern


class NoticeBlockTest {
    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    val pattern = Pattern.compile("^>\\s+[*][*]([Ww]arning|[Nn]ote|[Ii]nfo|[Tt]ip)[:][*][*]\\s*(.*)$")

    @Test
    fun `pattern match tip with title`() {

        val match = pattern.matcher("> **tip:** About you")

        assertNotNull( match )
        assertTrue( match.matches() )
        assertEquals( 2 ,match.groupCount())
        assertEquals( "tip" ,match.group(1))
        assertEquals( "About you" ,match.group(2))
    }
    @Test
    fun `pattern match tip without title`() {

        val match = pattern.matcher("> **tip:**")

        assertNotNull( match )
        assertTrue( match.matches() )
        assertEquals( 2 ,match.groupCount())
        assertEquals( "tip" ,match.group(1))
        assertEquals( "" ,match.group(2))

    }

    private fun parse():String? = parseResource( this.javaClass, "cheatsheet/noticeblock", this.site )

    @Test
    //@Ignore
    fun `parse notice blocks`()  = assertEquals( """
        h1. Notice Block Syntax
        h2. info
        {info:title=About me}
        {quote}
        tposidufsqdf qsfpqs dfopqsdijf q
        mldjkflqsdif sqj
        {quote}
        {info}
        
        h2. Note without title
        {note:title=}
        Contents of my note
        {note}
        
        h2. Tip with title
        {tip:title=About you}
        tposidufsqdf qsfpqs dfopqsdijf q
        mldjkflqsdif sqj
        {tip}
        
        h2. Warning with complex content
        {warning:title=About him}
        tposidufsqdf qsfpqs dfopqsdijf q
        mldjkflqsdif sqj
        * one
        * two
        have a *strong* and _pure_ feeling
        {warning}
    """.trimIndent(), parse() )

}