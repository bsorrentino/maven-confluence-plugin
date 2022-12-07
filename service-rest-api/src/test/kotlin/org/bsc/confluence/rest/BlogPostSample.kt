package org.bsc.confluence.rest

import org.bsc.confluence.ConfluenceService
import org.bsc.confluence.ConfluenceService.Protocol
import org.bsc.confluence.ConfluenceService.Storage
import org.bsc.confluence.ConfluenceService.Storage.Representation
import org.bsc.ssl.SSLCertificateInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {

    val confluenceUrl = "http://localhost:8090"
    val credentials  = ConfluenceService.Credentials("admin", "admin")
    val sslInfo = SSLCertificateInfo()

    val spaceKey = "TEST"


   RESTConfluenceService( Protocol.REST.addTo(confluenceUrl), credentials, sslInfo).apply {

        val content = """
            h1. BLOG POST TEST
            ----
        """.trimIndent()

        val title = "BLOG REST - ${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}"

        var blogpost = createBlogpost( spaceKey, title, Storage.of( content, Representation.WIKI), 0)

        blogpost =  addBlogpost( blogpost ).join()

        print( "blogpost.id=${blogpost.id}\n")

    }

}
