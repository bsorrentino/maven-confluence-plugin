/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.reporting

import org.bsc.confluence.ConfluenceHtmlUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 *
 * @author bsorrentino
 */
class ParseTextTest {

    @Test
    fun parsePluginDescription() {
        val text = """
if using a https url, configure if the plugin accepts every certificates or respects hostnameVerifierClass and trustManagerClass (if set). 
Below the Template 
<pre>
    <sslCertificate> 
        <ignore>true|false</ignore> // default true 
        <hostnameVerifierClass>FQN</hostnameVerifierClass> //default null 
        <trustManagerClass>FQN</trustManagerClass> // default null 
    </sslCertificate> 
</Pre>"""

        val result = ConfluenceHtmlUtils.replaceHTML(text)

        assertNotNull(result)

        val expected = """
if using a https url, configure if the plugin accepts every certificates or respects hostnameVerifierClass and trustManagerClass (if set). 
Below the Template 
{noformat}
    <sslCertificate> 
        <ignore>true|false</ignore> // default true 
        <hostnameVerifierClass>FQN</hostnameVerifierClass> //default null 
        <trustManagerClass>FQN</trustManagerClass> // default null 
    </sslCertificate> 
{noformat}"""

        assertEquals(expected, result)
    }
}