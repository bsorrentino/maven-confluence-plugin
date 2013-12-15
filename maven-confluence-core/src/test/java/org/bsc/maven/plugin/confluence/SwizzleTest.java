/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.plugin.confluence;

import org.codehaus.swizzle.confluence.ServerInfo;
import org.codehaus.swizzle.confluence.Confluence2;
import org.junit.Ignore;
import org.codehaus.swizzle.confluence.Attachment;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;
import org.junit.After;
import org.junit.Assert;
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

    public static final String PASSWORD = "admin";
    public static final String USER = "admin";
    public static final String URL = "http://localhost:8090/";

    Confluence confluence = null;

    @Before
    public void connect() throws Exception  {
        
        Confluence.ProxyInfo proxyInfo = null;
        
        confluence = ConfluenceFactory.createInstanceDetectingVersion( URL, proxyInfo, USER, PASSWORD);
        

    }
    @After
    public void disconnect() throws Exception {

        if( confluence != null ) {
            confluence.logout();
            confluence = null;
        }
    }

    @Test @Ignore 
    public void fakeTest() {}
    
    @Test
    public void showInfo() throws Exception {
        
        ServerInfo  si = confluence.getServerInfo();
        
        System.out.printf( "majorVersion=[%s]\n", si.getMajorVersion());
        
    }
    
    @Test 
    @Ignore
    public void addAttachment() throws Exception {

        Page page = ConfluenceUtils.getOrCreatePage(confluence, "ds", "Tutorial", "test");
        
        Attachment a = new Attachment(new java.util.HashMap());
        a.setComment("test");
        a.setFileName( "foto2.jpg");
        a.setContentType( "image/jpg" );


        java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("foto2.jpg");

        page = confluence.storePage(page);
        
        Assert.assertThat( page, notNullValue() );
        Assert.assertThat( page.getId(), notNullValue() );
        
        ConfluenceUtils.addAttchment(confluence, page, a, is);

    }

    @Test 
    @Ignore
    public void findAttachment() throws Exception {
        Page page = ConfluenceUtils.getOrCreatePage(confluence, "ds", "Tutorial", "test");

        Attachment a = confluence.getAttachment( page.getId(), "foto2.jpg", "0");

        assertThat( a, notNullValue() );

        System.out.printf( " created=[%tc] creator=[%s] size=[%s]\n", a.getCreated(), a.getCreator(), a.getFileSize());
    }
}
