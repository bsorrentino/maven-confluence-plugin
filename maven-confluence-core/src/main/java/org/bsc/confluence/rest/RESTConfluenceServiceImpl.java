/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.bsc.confluence.rest.model.Page;
import javax.json.JsonObjectBuilder;
import static java.lang.String.format;
import org.bsc.ssl.SSLCertificateInfo;
import rx.functions.Action1;

/**
 * @see https://docs.atlassian.com/confluence/REST/latest/
 * 
 * @author bosrrentino
 */
public class RESTConfluenceServiceImpl implements ConfluenceService {
    
    final Credentials credentials;
    final OkHttpClient.Builder client = new OkHttpClient.Builder();
    final java.net.URL endpoint ;
    
    public RESTConfluenceServiceImpl( String url , Credentials credentials, SSLCertificateInfo sslInfo) {
        if( credentials==null ) {
            throw new IllegalArgumentException("credentials argument is null!");
        } 
        if( url==null ) {
            throw new IllegalArgumentException("url argument is null!");
        } 
        if( sslInfo==null ) {
            throw new IllegalArgumentException("sslInfo argument is null!");
        } 
        
        try {
            this.endpoint = new java.net.URL(url);
            
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("url argument is not valid!", ex);
        }
        

        this.credentials = credentials;
        
        client.connectTimeout(10, TimeUnit.SECONDS);
        client.writeTimeout(10, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        
        if( !sslInfo.isIgnore() && "https".equals(this.endpoint.getProtocol()) ) {
                    
            client.hostnameVerifier(sslInfo.getHostnameVerifier())
                    .sslSocketFactory(sslInfo.getSSLSocketFactory(), sslInfo.getTrustManager())
                    
                 ;
        }
        
    }
    
    private HttpUrl.Builder urlBuilder() {
        
        int port = endpoint.getPort();
        port = (port > -1 ) ? port : endpoint.getDefaultPort();

        final String path = ConfluenceService.Protocol.XMLRPC.removeFrom(endpoint.getPath());

        return new HttpUrl.Builder()
                      .scheme(endpoint.getProtocol())
                      .host(endpoint.getHost())
                      .port(port)
                      .addPathSegments(path)
                      .addPathSegments(ConfluenceService.Protocol.REST.path()) 
                    ; 
    }
    
    private Observable<Response> fromRequest( final Request req ) {
        
         return rx.Observable.create( new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> t) {

                try {
                    final Response res = client.build().newCall(req).execute();

                    t.onNext(res);
                    t.onCompleted();                        
                                        
                } catch (IOException ex) {
                    
                    t.onError(ex);
                }
                
            }
        });
       
    }
    
    private Observable<Response> fromUrl( final HttpUrl url ) {
        final String credential = 
                okhttp3.Credentials.basic(credentials.username, credentials.password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )  
                .get()
                .build();
        
        return fromRequest(req);
    }

    private Observable<Response> rxfindContentById( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addQueryParameter("expand", "space,version,container")
                                    .build();
        
        return fromUrl( url );
    }

    private Observable<Response> rxfindContent( final String spaceKey, final String title ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addQueryParameter("spaceKey", spaceKey)
                                    .addQueryParameter("title", title)
                                    .addQueryParameter("expand", "space,version,container")
                                    .build();
        return fromUrl( url );
        
    }
    
    private final Func1<Response, Observable<JsonObject>> mapToArray = new Func1<Response, Observable<JsonObject>>() {
        @Override
        public Observable<JsonObject> call(Response res) {

            final ResponseBody body = res.body();

            try (Reader r = body.charStream()) {

                final JsonReader rdr = Json.createReader(r);

                final JsonObject root = rdr.readObject();

                // {"statusCode":404,"data":{"authorized":false,"valid":true,"errors":[],"successful":false,"notSuccessful":true},"message":"No space with key : TEST"}
                final JsonArray results = root.getJsonArray("results");

                //System.out.println( root.toString() );
                if (results == null || results.size() == 0) {
                    return Observable.empty();
                }
                            
                JsonObject array[] = new JsonObject[ results.size() ];
                for( int ii = 0 ; ii < results.size() ; ++ii )
                    array[ii] = results.getJsonObject(ii);
                
                return Observable.from( array );
 
            } catch (IOException ex) {

                return Observable.error(ex);
            }
        }
    };
   
    private  final Func1<Response, JsonObject> mapToObject =  new Func1<Response, JsonObject>() {
        @Override
        public JsonObject call(Response res) {
            final ResponseBody body = res.body();

            try (Reader r = body.charStream()) {

                final JsonReader rdr = Json.createReader(r);

                final JsonObject root = rdr.readObject();

                return root;

            } catch (IOException ex) {

                throw new Error(ex);
            }
        }

    };

    public Observable<Response> rxChildrenPageById( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addPathSegments("child/page")                
                                    .addQueryParameter("expand", "space,version,container")
                                    .build();
        return fromUrl( url );
        
    }

    /**
     * 
     * @param spaceKey
     * @param title
     * @return 
     */
    public Observable<JsonObject> rxfindPages( final String spaceKey, final String title ) {

        return rxfindContent(spaceKey, title)
                .flatMap( mapToArray );
        
    }
    
    /**
     * 
     * @param spaceKey
     * @param title
     * @return 
     */
    public Observable<JsonObject> rxfindPage( final String spaceKey, final String title ) {
        
        return rxfindContent(spaceKey, title)
                .flatMap( mapToArray )
                .take(1);
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public Observable<JsonObject> rxfindPageById( final String id ) {
        
        return rxfindContentById(id).map( mapToObject );
    }
    
    public JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final String title  ) {
        return Json.createObjectBuilder()
                .add("type","page")
                .add("title",title)
                .add("space",Json.createObjectBuilder().add("key", spaceKey))
                ;
    }

    public JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final int parentPageId, final String title  ) {
        return jsonForCreatingPage( spaceKey, title )
                .add("ancestors", Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder().add("id", parentPageId )))
                ;
    }
    public JsonObjectBuilder jsonAddBody( JsonObjectBuilder builder, Storage storage  ) {
        return builder
                .add("body", Json.createObjectBuilder()
                                .add("storage", Json.createObjectBuilder()
                                                .add("representation",storage.rapresentation.toString())
                                                .add("value",storage.value)))
                ;
    }

    
    public Observable<JsonObject> rxCreatePage( final JsonObject inputData ) {
        final String credential = 
                okhttp3.Credentials.basic(credentials.username, credentials.password);

        final MediaType storageFormat = MediaType.parse("application/json");
        
        final RequestBody inputBody = RequestBody.create(storageFormat, 
                inputData.toString());
        
        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( urlBuilder().addPathSegment("content") .build() )  
                .post(inputBody)
                .build();
        
        return rx.Observable.create( new Observable.OnSubscribe<JsonObject>() {
            @Override
            public void call(Subscriber<? super JsonObject> t) {

                try {
                    final Response res = client.build().newCall(req).execute();
                    
                    if( !res.isSuccessful() ) {
                        t.onError( new Exception( format("error creating page\n%s", res.toString()) ));
                        return;
                    }

                    final ResponseBody body = res.body();

                    try( Reader r = body.charStream()) {
            
                        final JsonReader rdr = Json.createReader(r);

                        final JsonObject root = rdr.readObject();
                        
                        t.onNext(root);
                        t.onCompleted();
                    }
                    
                                        
                } catch (IOException ex) {
                    
                    t.onError(ex);
                }
                
            }
        });        
    }
    
    public Observable<JsonObject> rxUpdatePage( final String pageId, final JsonObject inputData ) {
        final String credential = 
                okhttp3.Credentials.basic(credentials.username, credentials.password);

        final MediaType storageFormat = MediaType.parse("application/json");
        
        final RequestBody inputBody = RequestBody.create(storageFormat, 
                inputData.toString());
        
        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( urlBuilder()
                        .addPathSegment("content")
                        .addPathSegment(pageId)
                        .build() )  
                .put(inputBody)
                .build();
        
        return rx.Observable.create( new Observable.OnSubscribe<JsonObject>() {
            @Override
            public void call(Subscriber<? super JsonObject> t) {

                try {
                    final Response res = client.build().newCall(req).execute();
                    
                    if( !res.isSuccessful() ) {
                        t.onError( new Exception( format("error updating page\n%s", res.toString()) ));
                        return;
                    }

                    final ResponseBody body = res.body();

                    try( Reader r = body.charStream()) {
            
                        final JsonReader rdr = Json.createReader(r);

                        final JsonObject root = rdr.readObject();
                        
                        t.onNext(root);
                        t.onCompleted();
                    }
                    
                                        
                } catch (IOException ex) {
                    
                    t.onError(ex);
                }
                
            }
        });        
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
    
    @Override
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removePage(Model.Page parentPage, String title) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePage(String pageId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Page getOrCreatePage(final String spaceKey, final String parentPageTitle, final String title) throws Exception {
        final Observable error =  Observable.error(new Exception(format("parentPage [%s] doesn't exist!",parentPageTitle)));

        final JsonObject page = 
                (JsonObject) rxfindPage(spaceKey, parentPageTitle)
                .switchIfEmpty( error )
                .flatMap(new Func1<JsonObject, Observable<JsonObject>>() {
                      @Override
                      public Observable<JsonObject> call(JsonObject parent) {

                        final String id = parent.getString("id");
                        final JsonObjectBuilder input = jsonForCreatingPage(spaceKey, Integer.valueOf(id), title);

                        return rxfindPage(spaceKey,title)                                    
                                .switchIfEmpty( rxCreatePage( input.build() ));
                      }
                 })
                .toBlocking()
                .first()
                ;
        
        return new Page( page );
    }

    @Override
    public Model.Page getOrCreatePage(Model.Page parentPage, String title) throws Exception {

        final String spaceKey = parentPage.getSpace();
        final String id = parentPage.getId();
        final JsonObjectBuilder input = jsonForCreatingPage(spaceKey, Integer.valueOf(id), title);

        final JsonObject result =  rxfindPage(spaceKey,title)                                    
                .switchIfEmpty( rxCreatePage( input.build() ))
                .toBlocking().first();
        
        return new Page(result);
    }

    /**
     * 
     * @param pageId
     * @return
     * @throws Exception 
     */
    @Override
    public Model.Page getPage(String pageId) throws Exception {
        
        final JsonObject result = rxfindPageById(pageId)                                    
                                    .toBlocking()
                                    .first();
        
        return (result!=null) ? new Page(result) : null;
    }

    @Override
    public Model.Page getPage(String spaceKey, String pageTitle) throws Exception {
                
        final JsonObject result = rxfindPage(spaceKey, pageTitle)                                    
                     .toBlocking()
                     .first();
        return (result!=null) ? new Page(result) : null;
    }

    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Page storePage(Model.Page page, Storage content ) throws Exception {
        
        int previousVersion = page.getVersion();
        
        final JsonObject input = Json.createObjectBuilder()
                .add("version", Json.createObjectBuilder().add("number", ++previousVersion))
                .add("id",page.getId())
                .add("type","page")
                .add("title",page.getTitle())
                .add("space",Json.createObjectBuilder().add("key", page.getSpace()))
                .add("body", Json.createObjectBuilder()
                                .add("storage", Json.createObjectBuilder()
                                                .add("representation",content.rapresentation.toString())
                                                .add("value",content.value)))
                .build()
                ;        
        
        final JsonObject result = rxUpdatePage(page.getId(),input)
                                    .toBlocking()
                                    .first();
        
        return new Page(result);
    }

    @Override
    public Model.Page storePage(Model.Page page) throws Exception {
        
        return page;
    }

    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void call(Action1<ConfluenceService> task) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Attachment createAttachment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Attachment getAttachment(String pageId, String name, String version) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Attachment addAttchment(Model.Page page, Model.Attachment attachment, InputStream source) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
