/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.confluence.xmlrpc;

import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.xmlrpc.model.Attachment;
import org.bsc.confluence.xmlrpc.model.ServerInfo;
import org.bsc.ssl.SSLCertificateInfo;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.junit.*;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
/**
 *
 * @author bsorrentino
 */
//@Ignore
public class XMLRPCIntegrationTest {

    public static final String PASSWORD = "admin";
    public static final String USER = "admin";
    public static final String URL = "http://localhost:8090/";

    XMLRPCConfluenceService confluence = null;

    @Before
    public void connect() throws Exception  {
        
        ConfluenceProxy proxyInfo = null;
        
        final ConfluenceService.Credentials credentials = 
                new ConfluenceService.Credentials(USER, PASSWORD );
        
        final SSLCertificateInfo sslInfo = new SSLCertificateInfo();

        confluence = 
            XMLRPCConfluenceService.createInstanceDetectingVersion(URL, credentials,proxyInfo, sslInfo);
        

    }
    @After
    public void disconnect() throws Exception {

        if( confluence != null ) {
            confluence.logout();
            confluence = null;
        }
    }
    
    @Test @Ignore 
    public void dummy() {}
    
    @Test
    public void showInfo() throws Exception {
        
        ServerInfo si = confluence.connection.getServerInfo();
        
        System.out.printf( "majorVersion=[%s]\n", si.getMajorVersion());
        
    }
       
    @Test @Ignore
    public void addAttachment() throws Exception {

        Model.Page page = confluence.getOrCreatePage("ds", "Tutorial", "test").get();
        
        Attachment a = new Attachment(new HashMap<>());
        a.setComment("test");
        a.setFileName( "foto2.jpg");
        a.setContentType( "image/jpg" );


        java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("foto2.jpg");

        page = confluence.storePage(page).get();
        
        Assert.assertThat( page, notNullValue() );
        Assert.assertThat( page.getId(), notNullValue() );
        
        confluence.addAttachment(page, a, is);

    }

    @Test @Ignore
    public void findAttachment() throws Exception {
        
        Model.Page page = confluence.getOrCreatePage("ds", "Tutorial", "test").get();           
        Optional<Model.Attachment> a = confluence.getAttachment( page.getId(), "foto2.jpg", "0").join();

        assertThat( a.isPresent(), IsEqual.equalTo(true) );
        assertThat( a.get(), IsInstanceOf.instanceOf(Attachment.class) );

        System.out.printf( " created=[%tc] creator=[%s] size=[%s]\n", 
                a.get().getCreated(), 
                ((Attachment)a.get()).getCreator(), 
                ((Attachment)a.get()).getFileSize());
    }
}
