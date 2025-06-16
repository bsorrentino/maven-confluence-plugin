package org.bsc.makdown.commonmark

import org.bsc.markdown.commonmark.CommonmarkConfluenceWikiVisitor
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

class Issue301Test {

    @Test
    fun parseMermaidMacro() {
        val macro = """
<!-- {mermaiddiagram}
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
{mermaiddiagram } -->
""".trimIndent()

        //val m = isHTMLCommentPattern.matcher(macro)
        val m = CommonmarkConfluenceWikiVisitor.parseHTMLComment(macro)

        Assertions.assertTrue(m.matches())

        println( m.group(2) )
    }
}