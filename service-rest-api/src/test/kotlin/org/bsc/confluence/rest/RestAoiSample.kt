package org.bsc.confluence.rest

import org.bsc.confluence.ConfluenceService.*
import org.bsc.ssl.SSLCertificateInfo

fun main() {

    val confluenceUrl = "http://localhost:8090"
    val credentials  = Credentials("admin", "admin")
    val sslInfo = SSLCertificateInfo()

    // Get Page by Id
    RESTConfluenceService( Protocol.REST.addTo(confluenceUrl), credentials, sslInfo).apply {

        val page = this.getPage( Model.ID.of("101390799")).join()

        page.ifPresent {
            print( it.title )
        }
    }

}
