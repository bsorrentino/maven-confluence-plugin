/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.xmlrpc;

import lombok.val;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.ssl.SSLCertificateInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.confluence.ConfluenceUtils.sanitizeLabel;

/**
 *
 * @author bsorrentino
 */
public class XMLRPCConfluenceServiceImpl implements ConfluenceService {


    public final Confluence connection;
    public final Credentials credentials;

    /**
     *
     * @param url
     * @param credentials
     * @param proxyInfo
     * @param sslInfo
     * @return
     * @throws Exception
     */
    public static XMLRPCConfluenceServiceImpl createInstanceDetectingVersion( String url, Credentials credentials, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo ) throws Exception {
        if( url == null ) {
            throw new IllegalArgumentException("url argument is null!");
        }
        if( credentials == null ) {
            throw new IllegalArgumentException("credentials argument is null!");
        }
        if( sslInfo == null ) {
            throw new IllegalArgumentException("sslInfo argument is null!");
        }

        if (!sslInfo.isIgnore() && url.startsWith("https")) {
            
            HttpsURLConnection.setDefaultSSLSocketFactory( sslInfo.getSSLSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier( sslInfo.getHostnameVerifier() );
        }
        
        final Confluence c = new Confluence(url, proxyInfo);
        
        c.login(credentials.username, credentials.password);
        
        final ServerInfo info = c.getServerInfo();
        
        return new XMLRPCConfluenceServiceImpl( (info.getMajorVersion() < 4) ? c : new Confluence2(c), credentials );
        
    }
    
    /**
     * 
     * @param confluence 
     */
    protected XMLRPCConfluenceServiceImpl(Confluence confluence, Credentials credentials ) {
        if( confluence==null ) {
            throw new IllegalArgumentException("confluence argument is null!");
        } 
        if( credentials==null ) {
            throw new IllegalArgumentException("credentials argument is null!");
        } 
        this.connection = confluence;
        this.credentials = credentials;
    }

    @Override
    public void close() throws IOException {
        logout();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
 
    
    /**
     * 
     * @param parentPageId
     * @param title
     * @return
     * @throws Exception 
     */
    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(long parentPageId, String title)  {
//        if( parentPageId == null ) {
//            throw new IllegalArgumentException("parentPageId argument is null!");
//        }
        if( title == null ) {
            throw new IllegalArgumentException("title argument is null!");
        }

        final CompletableFuture<Optional<? extends Model.PageSummary>> result = new CompletableFuture<>();

        try {
            final List<PageSummary> children = connection.getChildren(String.valueOf(parentPageId));

            final Optional<Model.PageSummary> value = children.stream()
                    .filter( pageSummary -> title.equals(pageSummary.getTitle()) )
                    .map( pageSummary -> (Model.PageSummary)pageSummary )
                    .findFirst()
                    ;

            result.complete( value );

        } catch (Exception e) {
           result.completeExceptionally(e);
        }


        return result;
    }

    private Page cast( Model.Page page ) {
        if( page == null ) {
            throw new IllegalArgumentException("page argument is null!");
        }
        if( !(page instanceof Page) ) {
            throw new IllegalArgumentException("page argument is not right type!");
        }
        return (Page)page;

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

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title)  {
        Objects.requireNonNull(parentPage, "parentPage is null!");

        return getPageByTitle( parentPage.getId(), title)
                .thenCompose( page ->
                    ( page.isPresent() )
                        ? removePage( page.get().getId() )
                        : completedFuture(false) );
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {

            Page result = new Page(Collections.emptyMap());
            result.setSpace(parentPage.getSpace());
            result.setParentId(parentPage.getId());
            result.setTitle(title);

            return completedFuture(result);
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        
        if( page.getId() == null ) {
            throw new IllegalStateException("PageId is null. Attachment cannot be added!");
        }
        
        final Attachment a = cast(attachment);
        
        final CompletableFuture<Model.Attachment> result = new CompletableFuture<>();
        
        try(final BufferedInputStream fis = new BufferedInputStream(source, 4096 )) {

            final ByteArrayOutputStream baos = new ByteArrayOutputStream( );

            byte [] readbuf = new byte[4096];

            int len;

            while( (len=fis.read(readbuf))==readbuf.length ) {
                    baos.write(readbuf, 0, len);
            }
            if( len> 0 ) baos.write(readbuf, 0, len);

            a.setPageId( page.getId() );

            result.complete(connection.addAttachment( Long.parseLong(page.getId()), a, baos.toByteArray() ));     
            
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
     
        return result;
    }


    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page)  {
        final Page p = cast(page);
        
        final CompletableFuture<Model.Page> result = new CompletableFuture<>();
        try {
            result.complete(connection.storePage(p));
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        
        return result;
    }

   @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        if( content == null ) {
            throw new IllegalArgumentException("content argument is null!");
        }
        
        final Page p = cast(page);
        
        p.setContent(content.value);

        final CompletableFuture<Model.Page> result = new CompletableFuture<>();
        try {
            result.complete(connection.storePage(p));
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        
        return result;

    }

    /**
     *
     * @return
     */
    public boolean logout() {

        try {
            if (!connection.logout()) {
                //log.error("connection logout has failed!", null);
                return false;
            }
        } catch (Exception e) {
            //log.error("connection logout has failed due exception ", e);
            return false;
        }
    
        return true;

    }

    /**
     *
     * @param id
     * @param labels
     * @return
     */
    @Override
    public CompletableFuture<Void> addLabelsByName(long id, String[] labels) {
        return CompletableFuture.runAsync( () -> {
            asList(labels).forEach( label -> {
                try {
                    connection.addLabelByName(sanitizeLabel(label), id);
                } catch (Exception e) {
                    // Ignore exception
                }
            });
        });
    }
    

    @Override
    public Model.Attachment createAttachment() {
        return new Attachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(long pageId, String name, String version)  {

        CompletableFuture<Optional<Model.Attachment>> result = new CompletableFuture<>();
        try {
            result.complete(
                    ofNullable(connection.getAttachment(String.valueOf(pageId), name, version)));
        } catch (Exception e) {
            //result.completeExceptionally(e);
            result.complete(Optional.empty());

        }
        
        return result;
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle)  {
        
        CompletableFuture<Optional<Model.Page>> result = new CompletableFuture<>();
        try {
            result.complete(
                    ofNullable(connection.getPage(spaceKey, pageTitle) ));
        } catch (Exception e) {
            //result.completeExceptionally(e);
            result.complete(Optional.empty());
        }
        
        return result;
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(long pageId)  {
        
        CompletableFuture<Optional<Model.Page>> result = new CompletableFuture<>();
        try {
            result.complete(
                    ofNullable(connection.getPage(String.valueOf(pageId))));
        } catch (Exception e) {
            //result.completeExceptionally(e);
            result.complete(Optional.empty());
        }
        
        return result;

    }

    @Override
    public String toString() {
        try {
            final ServerInfo si = connection.getServerInfo();
            
            return format("Confluence version [%d.%d.%d-%s] development version [%b]",  
                            si.getMajorVersion(),                                                                                        
                            si.getMinorVersion(), 
                            si.getPatchLevel(), 
                            si.getBuildId(), 
                            si.isDevelopmentBuild());
            
        } catch (Exception ex) {
            // TODO LOG
            return ex.getMessage();
        }
    }

    @Override
    public CompletableFuture<java.util.List<Model.PageSummary>> getDescendents(long pageId) {
        final CompletableFuture<java.util.List<Model.PageSummary>> result = new CompletableFuture<>();
        try {
            result.complete(connection.getDescendents(String.valueOf(pageId)));
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    @Override
    public CompletableFuture<Boolean> removePage(long pageId) {
        val future = new CompletableFuture<Boolean>();
        
        try {
            connection.removePage(String.valueOf(pageId));
            future.complete(true);
        } catch (Exception e) {
            future.complete(false);
            //future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public void exportPage( String url, 
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
    
}
