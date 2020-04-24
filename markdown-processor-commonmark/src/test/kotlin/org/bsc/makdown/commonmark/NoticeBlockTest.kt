package org.bsc.makdown.commonmark

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.regex.Pattern


class NoticeBlockTest {

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

    private fun parse():String? = parseResource( this.javaClass, "cheatsheet/noticeblock" )

    @Test
    //@Ignore
    fun `parse notice blocks`()  = assertEquals( parse(), """
        h1. Notice Block Syntax
        h2. info
        {info:title=About me}
        {quote}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        {quote}
        {info}
        
        h2. Note without title
        {note:title=}
        Contents of my note
        {note}
        
        h2. Tip with title
        {tip:title=About you}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        {tip}
        
        h2. Warning with complex content
        {warning:title=About him}
        tposidufsqdf qsfpqs dfopqsdijf qmldjkflqsdif sqj
        *  one
        *  two
        have a *strong* and _pure_ feeling
        {warning}
    """.trimIndent() )

}