/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.bsc.confluence.ConfluenceService.Credentials;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.functional.Tuple2;
import org.bsc.ssl.SSLCertificateInfo;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author bsorrentino
 */
public class AbstractRestConfluence {
    //private static final String URL = "http://192.168.99.100:8090/rest/api";
    protected static String URL = "http://localhost:8090/rest/api";
    
    RESTConfluenceServiceImpl service;
    
    @Before
    public void initService() throws Exception {
      
        final Credentials credentials = new Credentials("admin", "admin");
        final SSLCertificateInfo sslInfo = new SSLCertificateInfo();
        
        service = new RESTConfluenceServiceImpl(URL, credentials, sslInfo );
    }
    
    @Test @Ignore
    public void dummy() {}

    @Test
    public void getOrCreatePageAndStoreWiki() throws Exception  {

        final String spaceKey = "TEST";
        final String parentPageTitle = "Home";
        final String title = "MyPage2";


        final Model.Page p = service.getOrCreatePage(spaceKey, parentPageTitle, title).get();

        int version = p.getVersion();
        Assert.assertThat( p, IsNull.notNullValue());
        Assert.assertThat( p.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( version > 0, Is.is(true));

        final String content = new StringBuilder()
                                        .append("h1.")
                                        .append(" TEST ")
                                        .append(System.currentTimeMillis()).append('\n')
                                        .append("----").append('\n')
                                        .append("*'wiki' \"wiki\"*")
                                        .toString();
        
        final Model.Page p1 = service.storePage(p, new Storage(content, Storage.Representation.WIKI)).get();

        Assert.assertThat( p1, IsNull.notNullValue());
        Assert.assertThat( p1.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( p1.getVersion(), IsEqual.equalTo(version+1));
        
        
        final Optional<Model.Page> p11 = service.getPage( p1.getId() ).get();
        
        Assert.assertThat( p11.isPresent(), IsEqual.equalTo(true));
        Assert.assertThat( p11.get().getTitle(), IsEqual.equalTo(p1.getTitle()));
        
        final boolean addLabelResult = service.addLabelByName("label", Integer.parseInt(p1.getId()) );
        
        Assert.assertThat( addLabelResult, Is.is(true));
        
        Model.Attachment result = 
            service.getAttachment(p1.getId(), "foto2.jpg", "")
            .thenApply( att -> {
                if( att.isPresent() ) return att.get();
                
                 Model.Attachment a = service.createAttachment();
                
                 a.setFileName( "foto2.jpg");
                 a.setContentType("image/jpg");
                 a.setComment("test image");
                 
                 return a;
            })
            .thenCompose( att -> 
                service.addAttachment( p1, att, getClass().getClassLoader().getResourceAsStream("foto2.jpg")))
            .join()
            ;
      
        Assert.assertThat( result, IsNull.notNullValue());

        final Optional<Model.Attachment> att2 = 
                service.getAttachment(p1.getId(), result.getFileName(), "").join();

        Assert.assertThat( att2.isPresent(), IsEqual.equalTo(true));

        
    }
    
    @Test
    public void getOrCreatePageAndStoreStorage() throws Exception  {

        final String spaceKey = "TEST";
        final String parentPageTitle = "Home";
        final String title = "MyPage3";

        final Tuple2<Model.Page,Model.Page> result =
            service.getPage(spaceKey, parentPageTitle)
                .thenApply( p -> p.orElseThrow( () -> new RuntimeException("parent page not found!")) )
                .thenCompose( p -> service.createPage( p, title ))
                .thenCompose( p -> {
    
                    final String content = new StringBuilder()
                            .append("<h1>")
                            .append("TEST ")
                            .append(System.currentTimeMillis())
                            .append("</h1>")
                            .append("<hr/>")
                            .append("<b>'storage' \"storage\"</b>")
                            .toString();
                    return CompletableFuture.completedFuture(p)
                            .thenCombine( 
                                    service.storePage(p, new Storage(content, Storage.Representation.STORAGE)), 
                                    Tuple2::of );
                })
                .get()
                ;
        
        Model.Page p = result.getValue1();
        Model.Page p1 = result.getValue2();
        
        int version = p.getVersion();
        Assert.assertThat( p, IsNull.notNullValue());
        Assert.assertThat( p.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( version > 0, Is.is(true));

        Assert.assertThat( p1, IsNull.notNullValue());
        Assert.assertThat( p1.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( p1.getVersion(), IsEqual.equalTo(version+1));
        
    }

    @Test //@Ignore
    public void getDescendentsTest()   {
    	
        final String spaceKey	= "TEST";
        final String title 		= "Home";
        
        service.getPage(spaceKey, title).thenAccept( page -> {
            
            Assert.assertThat( page.isPresent(), IsEqual.equalTo(true) );
           
            try {
                java.util.List<Model.PageSummary> descendents = service.getDescendents( page.get().getId() );
                
                Assert.assertThat( descendents, IsNull.notNullValue() );
                
                for( Model.PageSummary p : descendents ) {
                    System.out.printf( "Descend Page: [%s]\n", p.getTitle());
                }
                
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            
        })
        .exceptionally( e -> {           
            Assert.fail( e.getMessage() );
            return null;
        } );

    
    }
    

}
