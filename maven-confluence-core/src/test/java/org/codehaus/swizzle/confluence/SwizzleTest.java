/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codehaus.swizzle.confluence;

import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
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

    XMLRPCConfluenceServiceImpl confluence = null;

    @Before
    public void connect() throws Exception  {
        
        ConfluenceProxy proxyInfo = null;
        
        final ConfluenceService.Credentials credentials = 
                new ConfluenceService.Credentials(USER, PASSWORD );

        confluence = 
            XMLRPCConfluenceServiceImpl.createInstanceDetectingVersion(URL, credentials,proxyInfo);
        

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
        
        ServerInfo  si = confluence.connection.getServerInfo();
        
        System.out.printf( "majorVersion=[%s]\n", si.getMajorVersion());
        
    }
       
    @Test 
    @Ignore
    public void addAttachment() throws Exception {

        Model.Page page = confluence.getOrCreatePage("ds", "Tutorial", "test");
        
        Attachment a = new Attachment(new java.util.HashMap());
        a.setComment("test");
        a.setFileName( "foto2.jpg");
        a.setContentType( "image/jpg" );


        java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("foto2.jpg");

        page = confluence.storePage(page);
        
        Assert.assertThat( page, notNullValue() );
        Assert.assertThat( page.getId(), notNullValue() );
        
        confluence.addAttchment(page, a, is);

    }

    @Test 
    @Ignore
    public void findAttachment() throws Exception {
        Model.Page page = confluence.getOrCreatePage("ds", "Tutorial", "test");

        Model.Attachment a = confluence.getAttachment( page.getId(), "foto2.jpg", "0");

        assertThat( a, notNullValue() );
        assertThat( a, IsInstanceOf.instanceOf(Attachment.class) );

        System.out.printf( " created=[%tc] creator=[%s] size=[%s]\n", a.getCreated(), ((Attachment)a).getCreator(), ((Attachment)a).getFileSize());
    }
}
