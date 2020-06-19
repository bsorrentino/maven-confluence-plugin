/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maven.plugin.confluence

import org.bsc.confluence.ConfluenceHtmlUtils.replaceHtmlList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

/**
 *
 * @author bsorrentino
 */
class Issue99Test {
    @Disabled
    @Test
    fun dummy() {
    }

    fun tokenizeUsingRegex() {
        val line = "\"hello world\" Alexandros Alex \"I Am\" Something"
        val pattern = Pattern.compile("\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b")
        val matcher = pattern.matcher(line)
        var i = 0
        while (matcher.find()) {
            println(matcher.group(0))
            ++i
        }
        assertEquals(5, i)
    }

    @Test
    fun tokenizeHtmlUsingRegex() {
        val line = """
            this is an example of comment
            <ol>
            <li>item1</li>
            <li>item2</li>
            <li>item3</li>
            </ol>
            another example
            <ul>
            <li>item1</li>
            <li>item2</li>
            <li>item3</li>
            </ul>
            end example
        """.trimIndent()

        val expect = """
                this is an example of comment
                
                # item1
                # item2
                # item3
                
                another example
                
                * item1
                * item2
                * item3
                
                end example
            """.trimIndent()
        assertEquals( expect, replaceHtmlList(line) )
    }

    @Test
    fun tokenizeHtmlUsingRegex1() {
        val line = """
            <ul>
            <li><code>&#42;&#42;/&#42;.?ar</code></li>
            <li><code>&#42;&#42;/&#42;.dll</code></li>
            </ul>
        """.trimIndent()

        val expect = """
            
            * <code>&#42;&#42;/&#42;.?ar</code>
            * <code>&#42;&#42;/&#42;.dll</code>
            
        """.trimIndent()
        assertEquals( expect, replaceHtmlList(line) )
    }

    @Test
    fun tokenizeHtmlUsingRegex2() {
        val line = """
            <b>this is an example of bold</b>
        """.trimIndent()

        //final String result = line.replaceAll("<[Bb]>|</[Bb]>", "*");
        val result = line.replace("</?[Bb]>".toRegex(), "*")

        val expect = """
            *this is an example of bold*
        """.trimIndent()
        assertEquals( expect, result )
    }

    @Test
    fun tokenizeHtmlUsingRegex3() {
        val input = "this is an example of comment"
        assertEquals(input, replaceHtmlList(input))
    }
}