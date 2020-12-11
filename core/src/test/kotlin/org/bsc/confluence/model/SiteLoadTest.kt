package org.bsc.confluence.model

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class SiteLoadTest {

    private fun loadFromYAML(resource: String, c: ( site:Site ) -> Unit = {})  {
        val mapper = ObjectMapper(YAMLFactory())
        javaClass.classLoader.getResourceAsStream(resource).use { `is` ->
            val site = mapper.readValue(`is`, Site::class.java)
            assertNotNull(site)
            c(site)
        }
    }

    @Test
    fun testIssue182() = assertThrows(JsonMappingException::class.java) {
            loadFromYAML("site-issue182.yaml")
    }

    @Test
    fun testLoadFromYAML() {
        val tempDirectory = Files.createTempDirectory(null)

        loadFromYAML("site.yaml") { site ->

            val basedir = Paths.get(tempDirectory.toString())
            site.basedir = basedir

            assertNotNull(site.home)
            val uri = Paths.get(basedir.toString(), "encoding.confluence").toUri()
            assertEquals( uri, site.home.uri)
            val children = site.home.children
            assertNotNull(children)
            assertEquals(2, children.size)
            val attachments = site.home.attachments
            assertNotNull(attachments)
            assertEquals(1, attachments.size)
        }
    }


}