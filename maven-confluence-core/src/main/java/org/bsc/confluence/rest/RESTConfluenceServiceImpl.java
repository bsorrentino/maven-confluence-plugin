/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import static java.lang.String.format;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.model.Attachment;
import org.bsc.confluence.rest.model.Page;
import org.bsc.ssl.SSLCertificateInfo;
import org.codehaus.swizzle.confluence.ConfluenceExportDecorator;

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

    public final JsonObjectBuilder jsonForCreatingPage( final String spaceKey, final int parentPageId, final String title  ) {
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
        
        return rxChildrenPages(parentPageId).stream()
                .map( Page::new )
                .filter( page -> page.getTitle().equals( title ))
                .findFirst().get();
    }

    @Override
    public Model.Page getOrCreatePage(final String spaceKey, final String parentPageTitle, final String title) throws Exception {
        final JsonObject page = rxfindPage(spaceKey, parentPageTitle)
                                    .orElseThrow( () -> new Exception(format("parentPage [%s] doesn't exist!",parentPageTitle)) );
 
        return  Stream.of(page)
                .map( Page::new )
                .map( parent -> {

                    final String id = parent.getId();
                    final JsonObjectBuilder input = jsonForCreatingPage(spaceKey, Integer.valueOf(id), title);

                    return rxfindPage(spaceKey,title).map( Page::new )
                            .orElseGet( () -> rxCreatePage( input.build() ).map( Page::new ).get() );                                  
                 })
                .findFirst().get();
                
                
    }

    @Override
    public Model.Page getOrCreatePage(Model.Page parentPage, String title) throws Exception {

        final String spaceKey = parentPage.getSpace();
        final String id = parentPage.getId();
        final JsonObjectBuilder input = jsonForCreatingPage(spaceKey, Integer.valueOf(id), title);

        return rxfindPage(spaceKey,title).map( Page::new )
                .orElseGet( () -> rxCreatePage( input.build() ).map( Page::new ).get() ); 
    }

    /**
     * 
     * @param pageId
     * @return
     * @throws Exception 
     */
    @Override
    public Model.Page getPage(String pageId) throws Exception {
        
        final JsonObject result =  rxfindPageById(pageId).orElseThrow( () -> new Exception(format( "page [%s] not found!", pageId)));
    
        return new Page(result);
    }

    @Override
    public Model.Page getPage(String spaceKey, String pageTitle) throws Exception {
               
        final JsonObject result = rxfindPage(spaceKey, pageTitle).orElseThrow( () -> new Exception(format( "page [%s] not found!", pageTitle)));
        
        return new Page(result);
    }
    
    
    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        
        return rxDescendantPages(pageId).stream()
                .map( (page) -> new Page(page))
                .collect( Collectors.toList() );
        
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
        
        
        return rxUpdatePage(page.getId(),input).map( Page::new ).orElseGet(null);
    }

    @Override
    public Model.Page storePage(Model.Page page) throws Exception {     
        return page;
    }

    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
 
        rxAddLabels(String.valueOf(id), label);
        return true;
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
    public void call(java.util.function.Consumer<ConfluenceService> task) throws Exception {
    		if (task == null)
				throw new java.lang.IllegalArgumentException("task is null!");

    		task.accept(this);
    }

    @Override
    public Model.Attachment createAttachment() {    
        return new Attachment();
    }

    @Override
    public Model.Attachment getAttachment(String pageId, String name, String version) throws Exception {
       
        return rxAttachment(pageId, name).stream()
            .map( result -> new Attachment(result) )
            .findFirst().get();
    }

    @Override
    public Model.Attachment addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) throws Exception {

        return rxAddAttachment(page.getId(), cast(attachment), source).stream()
                .map( result -> new Attachment(result) )
                .findFirst().get();

    }
    
    @Override
    public boolean removePage(Model.Page parentPage, String title) throws Exception {
        
        return rxChildrenPages(parentPage.getId()).stream()
                .map( page -> new Page(page))
                .filter( page -> page.getTitle().equals(title) )
                .map( page -> rxDeletePageById(page.getId()) )
                .findFirst().orElse(false);
        
    }

    @Override
    public void removePage(String pageId) throws Exception {
        
        rxDeletePageById(pageId);
        
    }

    

}
