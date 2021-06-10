package org.bsc.makdown.commonmark

import org.apache.commons.io.IOUtils
import org.bsc.confluence.ConfluenceService
import org.bsc.confluence.model.Site
import org.bsc.confluence.model.SiteFactory
import org.bsc.confluence.model.SiteProcessor
import org.bsc.markdown.MarkdownProcessor
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.io.IOException
import java.nio.file.Paths
import java.util.*
import java.util.Optional.ofNullable
import java.util.regex.Pattern

/**
 *
 * @author bsorrentino
 */
class SiteTest : SiteFactory.Model {
    internal data class TestPage(private val title: String, private val space: String ) : ConfluenceService.Model.Page {

        override fun getId() = ConfluenceService.Model.ID.of(1001)

        override fun getTitle(): String = title

        override fun getSpace(): String  = space

        override fun getParentId() = ConfluenceService.Model.ID.of(1000)

        override fun getVersion(): Int = 0
    }

    private fun splitStrippingOutNewlines(content: String): List<String> {
        return content.split("\n+".toRegex()).filter { line -> line.isNotEmpty() }
    }

    override fun createSiteFromModel(variables: Map<String, Any>): Site {
        val path = Paths.get("src", "test", "resources", "site.yaml")
        return try {
            createFrom(path.toFile(), variables)
        } catch (e: Exception) {
            throw RuntimeException(String.format("error reading site descriptor at [%s]", path), e)
        }
    }

    var site: Site? = null

    @Before
    fun loadSite() {
        site = createSiteFromModel(emptyMap())
        site!!.basedir = Paths.get("src", "test", "resources")
    }

    @Test
    fun validateName() {
        val p = Pattern.compile("^(.*)\\s+$|^\\s+(.*)")
        Assertions.assertFalse(p.matcher("name").matches())
        Assertions.assertTrue(p.matcher("name ").matches())
        Assertions.assertTrue(p.matcher("name  ").matches())
        Assertions.assertTrue(p.matcher(" name").matches())
        Assertions.assertTrue(p.matcher("  name").matches())
        Assertions.assertTrue(p.matcher(" name ").matches())
        Assertions.assertTrue(p.matcher(" name  ").matches())
    }

    @Test
    @Throws(IOException::class)
    fun shouldSupportRefLink() {
        val parentPageTitle = Optional.of("Test")
        val stream = javaClass.classLoader.getResourceAsStream("withRefLink.md")
        Assertions.assertNotNull(stream)
        val content = SiteProcessor.processMarkdown(site, site!!.home, Optional.empty(), IOUtils.toString(stream), parentPageTitle)
        Assertions.assertNotNull(content)
        val converted = content.split("\n+".toRegex()).toTypedArray()
        var i = 1
        Assertions.assertEquals(String.format("* relative link to another page [SECOND PAGE|%s - page 2].", parentPageTitle.get()), converted[i++])
        Assertions.assertEquals("* This one is [inline|http://google.com|Google].", converted[i++])
        Assertions.assertEquals("* This one is [inline *wo* title|http://google.com].", converted[i++])
        Assertions.assertEquals("* This is my [google|http://google.com] link defined after.", converted[i++])
        Assertions.assertEquals("* This is my [more complex google|http://google.com|Other google] link defined after.", converted[i++])
        Assertions.assertEquals("* This is my [relative|relativepage] link defined after.", converted[i++])
        Assertions.assertEquals("* This is my [rel|relativeagain] link defined after.", converted[i++])
    }

    @Test
    @Throws(IOException::class)
    fun shouldSupportImgRefLink() {
        val page: ConfluenceService.Model.Page = TestPage("\${page.title}", "spaceKey")
        val parentPageTitle = Optional.of("Test IMG")
        val stream = javaClass.classLoader.getResourceAsStream("withImgRefLink.md")
        Assertions.assertNotNull(stream)
        val content = SiteProcessor.processMarkdown(site, site!!.home, Optional.of(page), IOUtils.toString(stream), parentPageTitle)
        Assertions.assertNotNull(content)
        val converted = content.split("\n+".toRegex()).toTypedArray()
        var i = 1
        Assertions.assertEquals("* add an absolute !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon! with title.", converted[i++])
        Assertions.assertEquals("* add a relative !\${page.title}^conf-icon-64.png|conf-icon! with title.", converted[i++])
        Assertions.assertEquals("* add a relative !\${page.title}^conf-icon-64.png|conf-icon! without title.", converted[i++])
        Assertions.assertEquals("* add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y! with title.", converted[i++])
        Assertions.assertEquals("* add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y1! without title.", converted[i++])
        Assertions.assertEquals("* add a ref img !\${page.title}^conf-icon-64.png|conf-icon-y2! relative.", converted[i++])
        Assertions.assertEquals("* add a ref img !\${page.title}^conf-icon-64.png|conf-icon-none! relative with default refname.", converted[i++])
    }

    @Test
    @Throws(IOException::class)
    fun shouldSupportSimpleNode() {
        val parentPageTitle = Optional.of("Test")
        val stream = javaClass.classLoader.getResourceAsStream("simpleNodes.md")
        Assertions.assertNotNull(stream)
        val converted = SiteProcessor.processMarkdown(site, site!!.home, Optional.empty(), IOUtils.toString(stream), parentPageTitle)
        Assertions.assertNotNull(converted)
        Assertions.assertLinesMatch(listOf(
                "h1. Horizontal rules",
                "----", "1",
                "----", "2",
                "----", "3",
                "----", "4",
                "----",
                "h1. Apostrophes",
                "'I'm with single quotes'",
                "\"you're with double quotes\"",
                "I'll talk to him 'bout borrowin' one of his models.",
                "h1. Ellipsis", "Three dots {{...}} will be converted to ...",
                "h1. Emdash", "Use 3 dashes {{---}} for an em-dash --- .",
                "# Range (endash)", "\"it's all in chapters 12--14\"",
                "h1. Line break", "Forcing a line-break\\\\",
                "Next line in the list",
                "h1. Nbsp",
                "      This will appear with six space characters in front of it." ), splitStrippingOutNewlines(converted))
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setMarkdownPorcessorProvider() {
            MarkdownProcessor.shared.name = "commonmark"
        }
    }
}