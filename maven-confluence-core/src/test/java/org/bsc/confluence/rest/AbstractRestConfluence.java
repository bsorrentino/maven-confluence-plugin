/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import lombok.Value;
import lombok.val;
import lombok.var;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.ssl.SSLCertificateInfo;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@Value(staticConstructor="of")
class PageTuple2 {
    ConfluenceService.Model.Page page1;
    ConfluenceService.Model.Page page2;
}

/**
 *
 * @author bsorrentino
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractRestConfluence {
    //private static final String URL = "http://192.168.99.100:8090/rest/api";
    String confluenceUrl    = "http://localhost:8090/rest/api";
    String spaceKey         = "TEST";
    String parentPageTitle  = "Home";
    
    protected ConfluenceService service;
    protected final ConfluenceService.Credentials credentials = new ConfluenceService.Credentials("admin", "admin");
    protected final SSLCertificateInfo sslInfo = new SSLCertificateInfo();
    
    protected enum Pages {
        MyPage2,
        MyPage3
    }

    
    /**
     * @return the confluenceUrl
     */
    protected String getConfluenceUrl() {
        return confluenceUrl;
    }

    /**
     * @param confluenceUrl the confluenceUrl to set
     */
    protected void setConfluenceUrl(String confluenceUrl) {
        this.confluenceUrl = confluenceUrl;
    }

    /**
     * @return the spaceKey
     */
    protected String getSpaceKey() {
        return spaceKey;
    }

    /**
     * @param spaceKey the spaceKey to set
     */
    protected void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    /**
     * @return the homePage
     */
    protected String getParentPageTitle() {
        return parentPageTitle;
    }

    /**
     * @param homePage the homePage to set
     */
    protected void setgetParentPageTitle(String title) {
        this.parentPageTitle = title;
    }

    /**
     * @return the service
     */
    protected ConfluenceService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    protected void setService(ConfluenceService service) {
        this.service = service;
    }

    @Test @Ignore
    public void dummy() {}

    @Test 
    public void test101_getOrCreatePageAndStoreWiki() throws Exception  {

        val title           = Pages.MyPage2.name();

        val p = service.getOrCreatePage(spaceKey, getParentPageTitle(), title).get();

        int version = p.getVersion();
        assertThat( p, notNullValue());
        assertThat(p.getSpace(), equalTo(spaceKey));
        assertThat( version > 0, is(true));

        val content = new StringBuilder()
                            .append("h1.")
                            .append(" TEST ")
                            .append(System.currentTimeMillis()).append('\n')
                            .append("----").append('\n')
                            .append("*'wiki' \"wiki\"*")
                            .toString();
        
        val p1 = service.storePage(p, new Storage(content, Storage.Representation.WIKI)).get();

        assertThat( p1, notNullValue());
        assertThat( p1.getSpace(), equalTo(spaceKey));
        assertThat( p1.getVersion(), equalTo(version+1));
        
        
        final Optional<Model.Page> p11 = service.getPage( p1.getId() ).get();
        
        assertThat( p11.isPresent(), equalTo(true));
        assertThat( p11.get().getTitle(), equalTo(p1.getTitle()));

        final String[] labels = {"label"};
        service.addLabelsByName(p1.getId(), labels ).get();

        Model.Attachment result = 
            service.getAttachment(p1.getId(), "foto2.jpg", "")
            .thenApply( att -> {
                if( att.isPresent() ) {
                    return att.get();
                }
                
                 var a = service.createAttachment();
                
                 a.setFileName( "foto2.jpg");
                 a.setContentType("image/jpg");
                 a.setComment("test image");
                 
                 return a;
            })
            .thenCompose( att -> 
                service.addAttachment( p1, att, getClass().getClassLoader().getResourceAsStream("foto2.jpg")))
            .join()
            ;
      
        assertThat( result, IsNull.notNullValue());

        final Optional<Model.Attachment> att2 = 
                service.getAttachment(p1.getId(), result.getFileName(), "").join();

        assertThat( att2.isPresent(), IsEqual.equalTo(true));

        
    }
    
    @Test
    public void test102_getOrCreatePageAndStoreStorage() throws Exception  {

        val title           = Pages.MyPage3.name();

        final PageTuple2 result =
                service.getOrCreatePage(spaceKey, getParentPageTitle(), title)
                       .thenCompose(p -> {
    
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
                                    PageTuple2::of );
                })
                .join()
                ;

        int version = result.getPage1().getVersion();
        assertThat( result.getPage1(), notNullValue());
        assertThat( result.getPage1().getSpace(), equalTo(spaceKey));
        assertThat( version > 0, is(true));

        assertThat( result.getPage2(), notNullValue());
        assertThat( result.getPage2().getSpace(), equalTo(spaceKey));
        assertThat( result.getPage2().getVersion(), equalTo(version+1));
        
    }

    @Test //@Ignore
    public void test103_getDescendentsTest() throws Exception  {
    	
        service.getPage(spaceKey, getParentPageTitle()).thenAccept(page -> {
            
            assertThat( page.isPresent(), equalTo(true) );
           
            val descendents = service.getDescendents( page.get().getId() ).join();

            assertThat( descendents, notNullValue() );
            assertThat( descendents.isEmpty(), is(false) );

            for( Model.PageSummary p : descendents ) {
                System.out.printf( "Descend Page: [%s]\n", p.getTitle());
            }

        })
        .exceptionally( e -> {
            fail( e.getMessage() );
            return null;
        } )
        .get();

    
    }
    

}
