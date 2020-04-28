/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.CurrentThreadExecutor;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.model.Attachment;
import org.bsc.confluence.rest.model.Page;
import org.bsc.confluence.xmlrpc.ConfluenceExportDecorator;
import org.bsc.ssl.SSLCertificateInfo;

import okhttp3.HttpUrl;

/**
 * @see https://docs.atlassian.com/confluence/REST/latest/
 * 
 * @author bosrrentino
 */
public class RESTConfluenceServiceImpl extends AbstractRESTConfluenceService implements ConfluenceService {
    
    final Credentials credentials;
    
    final java.net.URL endpoint ;

    
    /**
     * 
     * @param url
     * @param credentials
     * @param sslInfo 
     */
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

    private Attachment cast( Model.Attachment attachment ) {
        if( attachment == null ) {
            throw new IllegalArgumentException("attachment argument is null!");
        }
        if( !(attachment instanceof Attachment) ) {
            throw new IllegalArgumentException("page argument is not right type!");
        }
        return (Attachment)attachment;

    }
    
    public final JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final String title  ) {
          return Json.createObjectBuilder()
                  .add("type","page")
                  .add("title",title)
                  .add("space",Json.createObjectBuilder().add("key", spaceKey))
                  ;
      }

    public final JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final String parentPageId, final String title  ) {
        return jsonForCreatingPage( spaceKey, Long.valueOf(parentPageId), title);
    }

    public final JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final long parentPageId, final String title  ) {
          return jsonForCreatingPage( spaceKey, title )
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
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        
        return childrenPages(parentPageId).stream()
                .map( Page::new )
                .filter( page -> page.getTitle().equals( title ))
                .findFirst().orElse(null);
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {

        final String spaceKey = parentPage.getSpace();
        final String id = parentPage.getId();
        final JsonObjectBuilder input = jsonForCreatingPage(spaceKey, Integer.valueOf(id), title);

        return supplyAsync( () ->
            createPage( input.build() ).map( Page::new ).get()          
        , CurrentThreadExecutor.instance);
    }

    /**
     * 
     * @param pageId
     * @return
     * @throws Exception 
     */
    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {        
        return CompletableFuture.completedFuture(findPageById(pageId).map( Page::new ));
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle)  {
        return CompletableFuture.completedFuture(findPage(spaceKey, pageTitle).map( Page::new ));
    }
    
    
    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        
        return descendantPages(pageId).stream()
                .map( (page) -> new Page(page))
                .collect( Collectors.toList() );
        
    }


    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content )  {
        
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
        
        return supplyAsync(() ->
            updatePage(page.getId(),input).map( Page::new ).get()
        , CurrentThreadExecutor.instance);
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page)  {     
        return CompletableFuture.completedFuture(page);
    }

    @Override
    public CompletableFuture<Void> addLabelsByName(long id, String[] labels ) {
        return CompletableFuture.runAsync( () -> addLabels(String.valueOf(id), labels) );
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
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
        return supplyAsync( () ->
                getAttachment(pageId, name).stream()
                        .findFirst()
                        .map( result -> (Model.Attachment)new Attachment(result) ) );
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source)  {
        return supplyAsync( () ->
                addAttachment(page.getId(), cast(attachment), source).stream()
                        .findFirst()
                        .map( result -> (Model.Attachment)new Attachment(result) )
                        .get() );
    }
    
    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        
        return completedFuture(
                childrenPages(parentPage.getId()).stream()
                .map( page -> new Page(page))
                .filter( page -> page.getTitle().equals(title) )
                .map( page -> deletePageById(page.getId()) )
                .findFirst().orElse(false) );
        
    }

    
    @Override
    public CompletableFuture<Boolean> removePage(String pageId) {
        return CompletableFuture.completedFuture( deletePageById(pageId) );
    }

    @Override
    public void close() throws IOException {
    }

    

}
