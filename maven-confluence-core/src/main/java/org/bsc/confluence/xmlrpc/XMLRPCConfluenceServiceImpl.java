/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.xmlrpc;

import lombok.val;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceUtils;
import org.bsc.confluence.ExportFormat;
import org.bsc.ssl.SSLCertificateInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

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
     * @param proxyInfo
     * @return
     * @throws MalformedURLException
     * @throws SwizzleException
     * @throws URISyntaxException 
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
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        if( parentPageId == null ) {
            throw new IllegalArgumentException("parentPageId argument is null!");
        }
        if( title == null ) {
            throw new IllegalArgumentException("title argument is null!");
        }
        
        final List<PageSummary> children = connection.getChildren(parentPageId);

        for (PageSummary pageSummary : children ) {

        	if( title.equals(pageSummary.getTitle())) {
        		return (Model.PageSummary) pageSummary;
        	}
        }

        return null;
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

        final CompletableFuture<Boolean> result = new CompletableFuture<>();
        try {
            final Model.PageSummary pageSummary = findPageByTitle( parentPage.getId(), title);

            if( pageSummary!=null ) {
                connection.removePage(pageSummary.getId());
                result.complete(true);
            }
            else {
                result.complete(false);
            }
        } catch (Exception e) {
            result.completeExceptionally(e);
        }

        return result;
        
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {

            Page result = new Page(Collections.emptyMap());
            result.setSpace(parentPage.getSpace());
            result.setParentId(parentPage.getId());
            result.setTitle(title);

            return CompletableFuture.completedFuture(result);
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
     * @param confluence
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
     * @param label
     * @param id
     * @return
     * @throws Exception 
     */
    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        return connection.addLabelByName(ConfluenceUtils.sanitizeLabel(label), id);
    }
    

    @Override
    public Model.Attachment createAttachment() {
        return new Attachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version)  {     

        CompletableFuture<Optional<Model.Attachment>> result = new CompletableFuture<>();
        try {
            result.complete(
                    Optional.ofNullable(connection.getAttachment(pageId, name, version)));
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
                    Optional.ofNullable(connection.getPage(spaceKey, pageTitle) ));
        } catch (Exception e) {
            //result.completeExceptionally(e);
            result.complete(Optional.empty());
        }
        
        return result;
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId)  {
        
        CompletableFuture<Optional<Model.Page>> result = new CompletableFuture<>();
        try {
            result.complete(
                    Optional.ofNullable(connection.getPage(pageId)));
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
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {        
        return connection.getDescendents(pageId);
    }

    @Override
    public CompletableFuture<Boolean> removePageAsync(String pageId) {
        val future = new CompletableFuture<Boolean>();
        
        try {
            connection.removePage(pageId);
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
