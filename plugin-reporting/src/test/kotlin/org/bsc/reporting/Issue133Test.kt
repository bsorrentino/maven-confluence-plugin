/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.reporting

import org.bsc.confluence.rest.RESTConfluenceService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

/**
 *
 * @author bsorrentino
 */
class Issue133Test {
    @Test
    @Disabled
    fun dummy() {
    }

    @Test
    fun htttpUrlBuilderWithoutPort() {
        assertThrows(URISyntaxException::class.java) {
            val endpoint = URL("http://localhost/confluence")
            assertEquals(-1, endpoint.port)

            URI(
                endpoint.protocol,
                null,  // user info,
                endpoint.host,
                endpoint.port,
                "rest/api",
                null,  // query
                null // fragment
            );

        }
    }

    @Test
    @Throws(MalformedURLException::class)
    fun htttpUrlBuilderWithoutPortSafe() {
        val endpoint = URL("http://localhost/confluence")
        assertEquals(-1, endpoint.port)
        var port = endpoint.port
        port = if (port > -1) port else endpoint.defaultPort
        var path = endpoint.path
        path = if (path.startsWith("/")) path.substring(1) else path

        val uri = URI(
            endpoint.protocol,
            null,  // user info,
            endpoint.host,
            port,
            null,
            null,  // query
            null // fragment
        );

        assertEquals("confluence", path)

        val url = RESTConfluenceService.buildUrl( uri, listOf( path ), mapOf() );

        assertEquals(endpoint, URL(url.toString()))
    }

    @Test
    @Throws(MalformedURLException::class, URISyntaxException::class)
    fun htttpUrlBuilderWithoutPortSafe2() {
        val endpoint = URL("http://localhost/")
        assertEquals(-1, endpoint.port)
        var port = endpoint.port
        port = if (port > -1) port else endpoint.defaultPort
        var path = endpoint.path
        path = if (path.startsWith("/")) path.substring(1) else path

        val uri = URI(
            endpoint.protocol,
            null,  // user info,
            endpoint.host,
            port,
            path,
            null,  // query
            null // fragment
        );

        assertTrue(path.isEmpty())

        val url = RESTConfluenceService.buildUrl( uri, listOf( path ), mapOf() );

        assertEquals(endpoint, URL(url.toString()))
    }
}