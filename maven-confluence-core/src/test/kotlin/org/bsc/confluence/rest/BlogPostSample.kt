package org.bsc.confluence.rest

import org.bsc.confluence.ConfluenceService
import org.bsc.confluence.ConfluenceService.Storage
import org.bsc.confluence.ConfluenceService.Storage.Representation
import org.bsc.ssl.SSLCertificateInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args : Array<String>) {

    val confluenceUrl = "http://localhost:8090/rest/api"
    val credentials  = ConfluenceService.Credentials("admin", "admin")
    val sslInfo = SSLCertificateInfo()

    val spaceKey = "TEST"


   RESTConfluenceService(confluenceUrl, credentials, sslInfo).apply {

        val content = """
            h1. BLOG POST TEST
            ----
        """.trimIndent()

        val title = "BLOG - ${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}"

        var blogpost = createBlogpost( spaceKey, title, Storage.of( content, Representation.WIKI))

        blogpost =  addBlogpost( blogpost ).join()

        print( "blogpost.id=${blogpost.id}")
       
    }

}

