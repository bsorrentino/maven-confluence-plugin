/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import java.util.List;
import javax.json.JsonObject;
import org.bsc.confluence.ConfluenceService.Credentials;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import org.hamcrest.core.IsNull;
import rx.functions.Func1;
import org.bsc.confluence.ConfluenceService.Model;
import rx.functions.Action1;
import javax.json.JsonObjectBuilder;
import static java.lang.String.format;
import okhttp3.Response;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.ssl.SSLCertificateInfo;
import org.hamcrest.core.Is;
import org.junit.Ignore;

/**
 *
 * @author bsorrentino
 */
public class AbstractRestConfluence {
    //private static final String URL = "http://192.168.99.100:8090/rest/api";
    protected static String URL = "http://localhost:8090/";
    
    RESTConfluenceServiceImpl service;
    
    @Before
    public void initService() throws Exception {
      
        final Credentials credentials = new Credentials("admin", "admin");
        final SSLCertificateInfo sslInfo = new SSLCertificateInfo();
        
        service = new RESTConfluenceServiceImpl(URL, credentials, sslInfo );
    }
    
    @Test @Ignore
    public void dummy() {}
    
    @Test @Ignore
    public void getOrCreatePage() throws Exception  {
        
        final String spaceKey = "TEST";
        final String parentPageTitle = "Home";
        final String title = "test-storage";
        
        {
            TestSubscriber<JsonObject> test = new TestSubscriber<>();

            service.rxfindPage(spaceKey,title).subscribe(test);

            test.assertCompleted();
            test.assertValueCount(1);
        }
        
        
        {

            final TestSubscriber<List<JsonObject>> test = new TestSubscriber<>();

            Observable.concat( service.rxfindPage(spaceKey, parentPageTitle), service.rxfindPage(spaceKey,title) )
            .buffer(2)
            .subscribe(test);

            test.assertCompleted();
            test.assertValueCount(1);
            Assert.assertThat( test.getOnNextEvents().get(0).size(), IsEqual.equalTo(2) );
        }
        
        {
            final String parentPageTitle0 = "NOTEXISTS";
            
            final Exception ex = new Exception(format("parentPage [%s] doesn't exist!",parentPageTitle0));
            final Observable error =  Observable.error(ex);
            
            TestSubscriber<JsonObject> test = new TestSubscriber<>();
            
            service.rxfindPage(spaceKey, parentPageTitle0)
                    .switchIfEmpty( error )
                    .subscribe(test)
                    ;
            
            test.assertError(ex);
    
        }
        
        {
            final String title0 = "MyPage";
            
            final Observable error =  Observable.error(new Exception(format("parentPage [%s] doesn't exist!",parentPageTitle)));
            
            TestSubscriber<JsonObject> test = new TestSubscriber<>();
            
            service.rxfindPage(spaceKey, parentPageTitle)
                    .switchIfEmpty( error )
                    .doOnNext( new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject t) {
                            System.out.printf( "Parent Id: [%s]\n", t.getString("id"));
                        }                        
                    })
                    .flatMap(new Func1<JsonObject, Observable<JsonObject>>() {
                          @Override
                          public Observable<JsonObject> call(JsonObject parent) {
                            
                            final String id = parent.getString("id");
                            final JsonObjectBuilder input = service.jsonForCreatingPage(spaceKey, Integer.valueOf(id), title0);
                            
                            System.out.printf( "input\n%s\n", input.toString());
                            
                            return service.rxfindPage(spaceKey,title0)                                    
                                    .switchIfEmpty( service.rxCreatePage( input.build() ));
                          }
                     })
                    .subscribe(test)
                    ;
            
            test.assertCompleted();
            test.assertValueCount(1);
            Assert.assertThat( test.getOnNextEvents().get(0), IsNull.notNullValue() );
            
            System.out.printf( "JsonObject\n%s", test.getOnNextEvents().get(0) );

    
        }
        
    }
    
    @Test
    public void getOrCreatePageAndStoreWiki() throws Exception  {

        final String spaceKey = "TEST";
        final String parentPageTitle = "Home";
        final String title = "MyPage2";


        final Model.Page p = service.getOrCreatePage(spaceKey, parentPageTitle, title);

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
        
        final Model.Page p1 = service.storePage(p, new Storage(content, Storage.Representation.WIKI));

        Assert.assertThat( p1, IsNull.notNullValue());
        Assert.assertThat( p1.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( p1.getVersion(), IsEqual.equalTo(version+1));
        
        
        final Model.Page p11 = service.getPage( p1.getId() );
        
        Assert.assertThat( p11, IsNull.notNullValue());
        Assert.assertThat( p11.getTitle(), IsEqual.equalTo(p1.getTitle()));
        
        
    }
    
    @Test
    public void getOrCreatePageAndStoreStorage() throws Exception  {

        final String spaceKey = "TEST";
        final String parentPageTitle = "Home";
        final String title = "MyPage3";


        final Model.Page p = service.getOrCreatePage(spaceKey, parentPageTitle, title);

        int version = p.getVersion();
        Assert.assertThat( p, IsNull.notNullValue());
        Assert.assertThat( p.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( version > 0, Is.is(true));

        final String content = new StringBuilder()
                                        .append("<h1>")
                                        .append("TEST ")
                                        .append(System.currentTimeMillis())
                                        .append("</h1>")
                                        .append("<hr/>")
                                        .append("<b>'storage' \"storage\"</b>")
                                        .toString();
        final Model.Page p1 = service.storePage(p, new Storage(content, Storage.Representation.STORAGE));

        Assert.assertThat( p1, IsNull.notNullValue());
        Assert.assertThat( p1.getSpace(), IsEqual.equalTo("TEST"));
        Assert.assertThat( p1.getVersion(), IsEqual.equalTo(version+1));
        
    }


}
