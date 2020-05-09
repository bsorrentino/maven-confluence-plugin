/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.reporting

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.HttpUrl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.MalformedURLException
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
        assertThrows(IllegalArgumentException::class.java) {
            val endpoint = URL("http://localhost/confluence")
            Assertions.assertEquals(-1, endpoint.port)
            /*final HttpUrl.Builder builder = */HttpUrl.Builder()
                .scheme(endpoint.protocol)
                .host(endpoint.host)
                .port(endpoint.port)
                .addPathSegments("rest/api")
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
        val builder = HttpUrl.Builder()
                .scheme(endpoint.protocol)
                .host(endpoint.host)
                .port(port)
        assertEquals("confluence", path)
        val url = builder
                .addPathSegments(path) //.addPathSegments("rest/api")
                .build()
        assertEquals(endpoint, url.url())
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
        val builder = HttpUrl.Builder()
                .scheme(endpoint.protocol)
                .host(endpoint.host)
                .port(port)
        assertTrue(path.isEmpty())
        val url = builder
                .addPathSegments(path) //.addPathSegments("rest/api")
                .build()
        assertEquals(endpoint, url.url())
    }
}