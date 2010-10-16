/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.plugin.confluence;

import org.junit.Ignore;
import org.codehaus.swizzle.confluence.Attachment;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
/**
 *
 * @author softphone
 */
@Ignore
public class SwizzleTest {

    Confluence confluence = null;

    @Before
    public void connect() throws Exception  {
        
        confluence = new Confluence( "http://localhost:1990/confluence" );
        
        confluence.login("admin", "admin");

    }

    @After
    public void disconnect() throws Exception {

        if( confluence != null ) {
            confluence.logout();
            confluence = null;
        }
    }

    @Test
    public void addAttachment() throws Exception {

        Page page = ConfluenceUtils.getOrCreatePage(confluence, "ds", "Home", "Tutorial");

        Attachment a = new Attachment(new java.util.HashMap());
        a.setComment("test");
        a.setFileName( "foto2.jpg");
        a.setContentType( "image/jpg" );


        java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("foto2.jpg");

        ConfluenceUtils.addAttchment(confluence, page, a, is);

    }

    @Test
    public void findAttachment() throws Exception {
        Page page = ConfluenceUtils.getOrCreatePage(confluence, "ds", "Home", "Tutorial");

        Attachment a = confluence.getAttachment( page.getId(), "foto2.jpg", "0");

        assertThat( a, notNullValue() );

        System.out.printf( " created=[%tc] creator=[%s] size=[%s]\n", a.getCreated(), a.getCreator(), a.getFileSize());
    }
}
