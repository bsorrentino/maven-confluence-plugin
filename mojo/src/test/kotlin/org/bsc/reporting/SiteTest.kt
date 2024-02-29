/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.reporting;

import org.bsc.confluence.model.Site
import org.junit.Assert
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import javax.xml.bind.JAXBContext

/**
 *
 * @author bsorrentino
 */
class SiteTest {
    
    
    /**
     * Must be ignored because i got
     * 
     * java.lang.NoClassDefFoundError: org/bsc/maven/reporting/model/Site
     * at org.bsc.reporting.SiteTest.jaxbTest(SiteTest.java:30)
     * 
     * @throws Exception 
     */
    @Test
    @Disabled
    fun jaxbTest() {

        val basepath = Paths.get("src/site/confluence/home.confluence")

        val relativeURI = java.net.URI("template.confluence")

        print("uri=${basepath.toUri().resolve(relativeURI)}")
        
        val jc = JAXBContext.newInstance(Site::class.java)

        val unmarshaller = jc.createUnmarshaller()

        val result = unmarshaller.unmarshal( this::class.java.classLoader.getResourceAsStream("site.xml"));

        assertNotNull(result)
        assertTrue( result is Site)

        val site = result as Site
        
        //site.setBasedir( basepath )

        assertNotNull(site.home.uri)
        assertEquals("home", site.home.name)
        Assert.assertNotNull(site.home)
        assertNotNull(site.home.attachments)
        assertFalse(site.home.attachments.isEmpty())
        assertNotNull(site.home.children)
        assertFalse(site.home.children.isEmpty())
        assertFalse(site.labels.isEmpty());

        site.labels.forEach {
            print( "label=$it\n")
        }
    }

}
