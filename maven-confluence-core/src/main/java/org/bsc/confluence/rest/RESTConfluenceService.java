/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.model.Attachment;
import org.bsc.confluence.rest.model.Page;
import org.bsc.confluence.xmlrpc.ConfluenceExportDecorator;
import org.bsc.ssl.SSLCertificateInfo;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.*;

/**
 * @see "https://docs.atlassian.com/confluence/REST/latest/"
 * 
 * @author bosrrentino
 */
public class RESTConfluenceService extends AbstractRESTConfluenceService implements ConfluenceService {

    enum ContentType { page, blogpost }

    final Credentials credentials;
    
    final java.net.URL endpoint ;

    static {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }
    /**
     * 
     * @param url
     * @param credentials
     * @param sslInfo 
     */
    public RESTConfluenceService(String url , Credentials credentials, SSLCertificateInfo sslInfo) {
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

    private Attachment cast( Model.Attachment attachment ) {
        if( attachment == null ) {
            throw new IllegalArgumentException("attachment argument is null!");
        }
        if( !(attachment instanceof Attachment) ) {
            throw new IllegalArgumentException("page argument is not right type!");
        }
        return (Attachment)attachment;

    }
    
    public final JsonObjectBuilder jsonForCreatingContent( ContentType type, final String spaceKey, final String title  ) {
          return Json.createObjectBuilder()
                  .add("type",type.name())
                  .add("title",title)
                  .add("space",Json.createObjectBuilder().add("key", spaceKey))
                  ;
      }

//    public final JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final String parentPageId, final String title  ) {
//        return jsonForCreatingPage( spaceKey, Long.valueOf(parentPageId), title);
//    }

    public final JsonObjectBuilder jsonForCreatingContent(ContentType type, final String spaceKey, final long parentPageId, final String title  ) {
          return jsonForCreatingContent( type, spaceKey, title )
                  .add("ancestors", Json.createArrayBuilder()
                                          .add(Json.createObjectBuilder().add("id", parentPageId )))
                  ;
      }

      public final JsonObjectBuilder jsonAddBody( JsonObjectBuilder builder, Storage storage  ) {
          return builder
                  .add("body", Json.createObjectBuilder()
                                  .add("storage", Json.createObjectBuilder()
                                                  .add("representation",storage.rapresentation.toString())
                                                  .add("value",storage.value)))
                  ;
      }

    /**
     *
     * @param spaceKey
     * @param title
     * @return
     */
      public final CompletableFuture<Model.Page> createPageByTitle( String spaceKey, String title ) {
              final JsonObjectBuilder input = jsonForCreatingContent( ContentType.page, spaceKey, title);

              return createPage( input.build() )
                      .thenApply( data -> data.map( Page::new ).get() );
      }

    /**
     * 
     * @return 
     */
    @Override
    protected HttpUrl.Builder urlBuilder() {
        
        int port = endpoint.getPort();
        port = (port > -1 ) ? port : endpoint.getDefaultPort();

        String path = endpoint.getPath();
        
        path = (path.startsWith("/")) ? path.substring(1) : path;
        
        return new HttpUrl.Builder()
                      .scheme(endpoint.getProtocol())
                      .host(endpoint.getHost())
                      .port(port)
                      .addPathSegments(path)
                      //.addPathSegments(ConfluenceService.Protocol.REST.path()) 
                    ; 
    }
    
    @Override
    public Credentials getCredentials() {
        return credentials;
    }
    
    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(Model.ID parentPageId, String title)  {

        return childrenPages(String.valueOf(parentPageId))
                .thenApply( children ->
                    children.stream()
                    .map( Page::new )
                    .filter( page -> page.getTitle().equals( title ))
                    .findFirst() );
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {

        final String spaceKey = parentPage.getSpace();
        final JsonObjectBuilder input = jsonForCreatingContent( ContentType.page, spaceKey, parentPage.getId().getValue(), title);

        return createPage( input.build() )
                    .thenApply( page -> page.map( Page::new ).get() );
    }

    /**
     * 
     * @param pageId
     * @return
     * @throws Exception 
     */
    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(Model.ID pageId) {
        return findPageById( pageId.toString() )
                    .thenApply( page -> page.map( Page::new ));
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle)  {
        return findPage(spaceKey, pageTitle)
                    .thenApply( page -> page.map( Page::new ));
    }
    
    
    @Override
    public CompletableFuture<List<Model.PageSummary>> getDescendents(Model.ID pageId)  {
        return descendantPages( pageId.getValue() )
                    .thenApply( descendant ->
                            descendant.stream()
                                .map( (page) -> new Page(page))
                                .collect( Collectors.toList() ));
    }


    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content )  {
        
        int previousVersion = page.getVersion();
        
        final JsonObject input = Json.createObjectBuilder()
                .add("version", Json.createObjectBuilder().add("number", ++previousVersion))
                .add("id",page.getId().getValue())
                .add("type","page")
                .add("title",page.getTitle())
                .add("space",Json.createObjectBuilder().add("key", page.getSpace()))
                .add("body", Json.createObjectBuilder()
                                .add("storage", Json.createObjectBuilder()
                                                .add("representation",content.rapresentation.toString())
                                                .add("value",content.value)))
                .build();

        final CompletableFuture<Model.Page> updatePage =
            updatePage(page.getId().toString(),input)
                    .thenApply( p -> p.map( Page::new ).get());

        return supplyAsync( () -> updatePage.join() );
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page)  {     
        return completedFuture(page);
    }

    @Override
    public CompletableFuture<Void> addLabelsByName(Model.ID id, String[] labels ) {
        return runAsync( () -> addLabels(id.toString(), labels) );
    }

    @Override
    public void exportPage(	String url, 
    							String spaceKey, 
    							String pageTitle, 
    							ExportFormat exfmt, 
    							File outputFile) throws Exception 
    {

		final ConfluenceExportDecorator exporter = 
				new ConfluenceExportDecorator( this, url );
		
		exporter.exportPage(spaceKey, 
		                    pageTitle, 
		                    exfmt, 
		                    outputFile);
    }

    @Override
    public Model.Attachment createAttachment() {    
        return new Attachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(Model.ID pageId, String name, String version) {
        return getAttachment(pageId.toString(), name)
                    .thenApply( attachments ->
                                attachments.stream()
                                    .findFirst()
                                    .map( result -> (Model.Attachment)new Attachment(result) ) );
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source)  {
        final CompletableFuture<Model.Attachment> addAttchment =
            addAttachment(page.getId().toString(), cast(attachment), source)
                    .thenApply( attachments ->
                            attachments.stream()
                                .findFirst()
                                .map( result -> (Model.Attachment)new Attachment(result) )
                                .get());

        return supplyAsync( () -> addAttchment.join() );

    }
    
    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        
        return childrenPages(parentPage.getId().toString())
                .thenCompose( children ->
                        children.stream()
                            .map( page -> new Page(page))
                            .filter( page -> page.getTitle().equals(title) )
                            .map( page -> deletePageById(page.getId().toString()) )
                            .findFirst()
                            .orElse( completedFuture(false) ));
        
    }

    
    @Override
    public CompletableFuture<Boolean> removePage(Model.ID pageId) {
        return deletePageById(pageId.toString());
    }

    @Override
    public void close() throws IOException {
    }

    

}
