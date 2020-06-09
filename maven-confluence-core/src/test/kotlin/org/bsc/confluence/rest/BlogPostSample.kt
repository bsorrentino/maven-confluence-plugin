package org.bsc.confluence.rest

import org.bsc.confluence.ConfluenceService
import org.bsc.confluence.rest.model.Page
import org.bsc.ssl.SSLCertificateInfo

fun main(args : Array<String>) {

    val confluenceUrl = "http://localhost:8090/rest/api"
    val credentials  = ConfluenceService.Credentials("admin", "admin")
    val sslInfo = SSLCertificateInfo()

    val spaceKey = "TEST"
    val title = "BLOG1"

   RESTConfluenceService(confluenceUrl, credentials, sslInfo).apply {

        val builder = jsonForCreatingContent( RESTConfluenceService.ContentType.blogpost, spaceKey, title);

        createPage(builder.build())
                .thenApply { page -> page.map { delegate -> Page(delegate) } }
                .join()

    }

}

