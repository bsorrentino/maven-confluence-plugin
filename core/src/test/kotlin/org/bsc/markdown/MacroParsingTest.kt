package org.bsc.markdown

import org.bsc.markdown.MarkdownVisitorHelper.isConfluenceMacroOrVariable
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue

class MacroParsingTest {

    @Test
    fun parse() {

        val macros = arrayOf(
            " {toc} ",
            "{toc:style=disc|indent=20px}",
            "{toc:type=flat|separator=pipe|maxLevel=3}",
            "{toc:outline=true|indent=0px|minLevel=2}",
            "{children}",
            "{children:all=true}",
            "{children:depth=x}",
            "{children:depth=x|style=h3}",
            "{children:excerpt=true}",
            "{children:page=Another Page}",
            " {children:page=/}",
            "{children:page=SPACEKEY:}  ",
            "{children:page=SPACEKEY:Page+Title}",
            " {children:first=x}",
            "{children:sort=<mode>|reverse=false}",
            "{blog-posts:max=5}",
            "{blog-posts:max=5|content=excerpts}",
            "{blog-posts:max=5|content=titles}",
            "{blog-posts:time=7d|spaces=@all}",
            "{blog-posts:max=15|time=14d|content=excerpts}",
            "{blog-posts:labels=confluence,atlassian}",
            "{blog-posts:labels=+atlassian,+confluence,+content}",
            "{navmap:mylabel}",
            "{navmap:mylabel|wrapAfter=3|cellWidth=110|cellHeight=20|theme=mytheme}",
            "{toc-zone:separator=brackets|location=top}  ",
            "{toc-zone}",
            "\${project.summary}",
            "\${plugin.goals}",
            "\${project.team}",
            "\${project.scmManager}",
        )

        for( literal in macros )  {
            println( "evaluate macro $literal")
            assertTrue( isConfluenceMacroOrVariable(literal) )
        }

    }

}