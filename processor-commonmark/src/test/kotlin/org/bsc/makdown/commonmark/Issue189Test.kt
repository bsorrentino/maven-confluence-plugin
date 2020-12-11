package org.bsc.makdown.commonmark

import org.bsc.confluence.model.Site
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNull
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.io.IOException
import java.net.URI
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

/**
 *
 * @author bsorrentino
 */
class Issue189Test  {

    var site = Site().apply {
        basedir = Paths.get(System.getProperty("user.dir"))
    }

    @Test
    fun parse() {
        val content = parseResource( this.javaClass, "issue189", this.site )
        val pageTitle = "\${page.title}"

        Assertions.assertEquals("""
        !${pageTitle}^img.png|alt text!
        
        !${pageTitle}^img.png|!
        
        !${pageTitle}^img.png|!
        
        !${pageTitle}^img.png|!
        
        !${'$'}{pageTitle}^img.png|alt text!
        
        !${pageTitle}^img.png|alt text!
        
        !${pageTitle}^meal.png|thumbnail!
        
        !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon!
        
        !${pageTitle}^cb-integration-components.png|cb-integration-components.png!
        """.trimIndent(), content )
    }

    /**
     *
     * @param uri
     * @return
     */
    private fun getFileName(uri: String): CompletableFuture<String?> {
        val result = CompletableFuture<String?>()
        try {
            val uriObject = URI.create(uri)
            if ("classpath".equals(uriObject.scheme, ignoreCase = true)) {
                result.completeExceptionally(
                        IllegalArgumentException("'classpath' scheme is not supported!"))
                return result
            }
            val path = Paths.get(uriObject.path)
            result.complete(path.fileName.toString())
        } catch (e: Throwable) {
            result.completeExceptionally(e)
        }
        return result
    }

    @Test
    fun processUrl() {
        val patternUrl = Pattern.compile("(?:(\\$\\{.+\\})\\^)?(.+)")
        run {
            val url = "\${page.title}^image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsEqual.equalTo("\${page.title}"))
            Assert.assertThat(m.group(2), IsEqual.equalTo("image-name.png"))
        }
        run {
            val url = "\${page.title}^./images/image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsEqual.equalTo("\${page.title}"))
            Assert.assertThat(m.group(2), IsEqual.equalTo("./images/image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsEqual.equalTo("image-name.png")) }
                    .join()
        }
        run {
            val url = "image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsEqual.equalTo("image-name.png")) }
                    .join()
        }
        run {
            val url = "./images/image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("./images/image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsEqual.equalTo("image-name.png")) }
                    .join()
        }
        run {
            val url = "file:///image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("file:///image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsEqual.equalTo("image-name.png")) }
                    .join()
        }
        run {
            val url = "http://localhost:8080/image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("http://localhost:8080/image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsEqual.equalTo("image-name.png")) }
                    .join()
        }
        run {
            val url = "classpath:/image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("classpath:/image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable? -> null }
                    .thenAccept { fileName: String? -> Assert.assertThat(fileName, IsNull.nullValue()) }
                    .join()
        }
        run {
            val url = "/image-name.png"
            val m = patternUrl.matcher(url)
            Assert.assertThat(m.matches(), IsEqual.equalTo(true))
            Assert.assertThat(m.groupCount(), IsEqual.equalTo(2))
            Assert.assertThat(m.group(1), IsNull.nullValue())
            Assert.assertThat(m.group(2), IsEqual.equalTo("/image-name.png"))
            getFileName(m.group(2))
                    .exceptionally { ex: Throwable ->
                        Assert.fail(ex.message)
                        null
                    }
                    .thenApply { fileName: String? -> "\${page.title}^$fileName" }
                    .thenAccept { fileName: String -> Assert.assertThat(fileName, IsEqual.equalTo("\${page.title}^image-name.png")) }
                    .join()
        }
    }
}