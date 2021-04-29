/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.xmlrpc;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceProxy;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.xmlrpc.model.*;
import org.bsc.ssl.SSLCertificateInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.confluence.ConfluenceUtils.sanitizeLabel;

/**
 *
 * @param <T>
 */
@FunctionalInterface
interface TrySupplier<T> {
    T get() throws Exception;
}


/**
 *
 * @author bsorrentino
 */
public class XMLRPCConfluenceService implements ConfluenceService {


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
    public static XMLRPCConfluenceService createInstanceDetectingVersion(String url, Credentials credentials, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo ) throws Exception {
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
        
        return new XMLRPCConfluenceService( (info.getMajorVersion() < 4) ? c : new Confluence2(c), credentials );
        
    }
    
    /**
     * 
     * @param confluence 
     */
    protected XMLRPCConfluenceService(Confluence confluence, Credentials credentials ) {
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


    @Override
    public Model.Page newPage(Model.ID id, String title) {

        final java.util.Map<String,Object> attributes = new HashMap<>();
        attributes.put( "id", id.toString());
        attributes.put( "title", title);
        return new Page( unmodifiableMap(attributes));
    }

    /**
     * 
     * @param parentPageId
     * @param title
     * @return
     * @throws Exception 
     */
    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(Model.ID parentPageId, String title)  {
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

    private <T> CompletableFuture<T> toFuture( TrySupplier<T> s ) {

        final CompletableFuture<T> result = new CompletableFuture<>();
        try {
            result.complete( s.get() );
        } catch (Exception e) {
            result.completeExceptionally(e);
        }

        return result;

    }

    private <T> CompletableFuture<T> toFuture(TrySupplier<T> s, Supplier<T> onExceptionReturn ) {

        final CompletableFuture<T> result = new CompletableFuture<>();
        try {
            result.complete( s.get() );
        } catch (Exception e) {
            result.complete( onExceptionReturn.get() );
        }

        return result;

    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title, Storage content) {

            final Page result = new Page(Collections.emptyMap());
            result.setSpace(parentPage.getSpace());
            result.setParentId(parentPage.getId());
            result.setTitle(title);
            result.setContent( content.value );

            return storePage( result );
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page)  {
        return toFuture( () -> connection.storePage( Page.class.cast(page)));
    }

   @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        if( content == null ) {
            throw new IllegalArgumentException("content argument is null!");
        }

       final Page p = Page.class.cast(page);

       p.setContent(content.value);

       return toFuture( () -> connection.storePage(p));

    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle)  {
        return toFuture( () -> ofNullable(connection.getPage(spaceKey, pageTitle) ), () -> Optional.empty());
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(Model.ID pageId)  {
        return toFuture( () -> ofNullable(connection.getPage(pageId.toString())), () -> Optional.empty());
    }

    @Override
    public CompletableFuture<java.util.List<Model.PageSummary>> getDescendents(Model.ID pageId) {
        return toFuture( () -> connection.getDescendents(pageId.toString()));
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
    public CompletableFuture<Boolean> removePage(Model.ID pageId) {

        return toFuture( () -> {
            connection.removePage(pageId.toString());
            return true;
        }, () -> false );
    }

    /**
     *
     * @param id
     * @param labels
     * @return
     */
    @Override
    public CompletableFuture<Void> addLabelsByName(Model.ID id, String[] labels) {
        return CompletableFuture.runAsync( () -> {
            asList(labels).forEach( label -> {
                try {
                    connection.addLabelByName(sanitizeLabel(label), id.getValue());
                } catch (Exception e) {
                    // Ignore exception
                }
            });
        });
    }

    ////////////////////////////////////////////////////////////////////////////////
    // ATTACHMENT
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public Model.Attachment createAttachment() {
        return new Attachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(Model.ID pageId, String name, String version)  {
        return toFuture( () -> ofNullable(connection.getAttachment(String.valueOf(pageId), name, version)), () -> Optional.empty() );
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {

        if( page.getId() == null ) {  throw new IllegalStateException("PageId is null. Attachment cannot be added!"); }

        return toFuture( () ->
                connection.addAttachment( page.getId().getValue(), Attachment.class.cast(attachment), IOUtils.toByteArray(source) ));
    }

    ////////////////////////////////////////////////////////////////////////////////
    // BLOG POST
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public Model.Blogpost createBlogpost( String space, String title, Storage content, int version) {
        final BlogEntry result = new BlogEntry();

        result.setSpace(space);
        result.setTitle(title);
        result.setVersion(version);
        result.setContent( content.value );

        return result;

    }

    @Override
    public CompletableFuture<Model.Blogpost> addBlogpost(Model.Blogpost blogpost )  {
        return toFuture( () -> connection.storeBlogEntry( BlogEntry.class.cast(blogpost) ));
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


}
