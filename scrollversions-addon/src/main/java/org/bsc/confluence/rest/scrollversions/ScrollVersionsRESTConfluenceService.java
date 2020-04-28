package org.bsc.confluence.rest.scrollversions;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsNewPage;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsPage;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsPageByTitle;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsTargetVersion;
import org.bsc.ssl.SSLCertificateInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.val;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implements a {@link ConfluenceService} with Scroll Versions support via REST APIs.
 *
 * @author Nicola Lagnena
 * @reviewer bsorrentino
 * 
 * @see <a href="https://help.k15t.com/scroll-versions/latest/api-resources-164037049.html">Scroll Versions API resources</a>
 */
public class ScrollVersionsRESTConfluenceService implements ConfluenceService {

    public static final String CHANGE_TYPE_MODIFY_QUERY_PARAM = "Modify";

    @SuppressWarnings("serial")
    public class ScrollVersionsException extends Exception {
        public ScrollVersionsException(String message) {
            super(message);
        }

        public ScrollVersionsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static final MediaType  JSON_MEDIA_TYPE                         = MediaType.parse("application/json; charset=utf-8");
    static final String     REQUEST_BODY_GET_ALL_VERSIONED_PAGES    = "[{\"queryArg\": \"pageType\", \"value\": \"change\"}]";
    static final Integer    GET_CREATED_PAGE_MAX_RETRIES            = 3;
    static final Long       GET_CREATED_PAGE_WAIT_MS                = 3000L;

    final RESTConfluenceServiceImpl delegate;

    final URL           scrollVersionsUrl;
    final String        scrollVersionsName;
    final ObjectMapper  objectMapper;

    public ScrollVersionsRESTConfluenceService( String confluenceUrl, 
                                                String scrollVersionName, 
                                                Credentials credentials, 
                                                SSLCertificateInfo sslCertificateInfo) 
    {
        if (scrollVersionName == null)
            throw new java.lang.IllegalArgumentException("scrollVersionName is null!");

        try {
            val regex = "/rest/api(/?)$";
            this.scrollVersionsUrl = new URL(confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0"));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid Scroll Versions url", e);
        }

        this.scrollVersionsName = scrollVersionName;
        this.delegate = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslCertificateInfo);
        this.objectMapper = new ObjectMapper();

    }

    HttpUrl.Builder urlBuilder() {
        
        int port = scrollVersionsUrl.getPort();
        port = (port > -1 ) ? port : scrollVersionsUrl.getDefaultPort();

        return new HttpUrl.Builder().scheme(scrollVersionsUrl.getProtocol())
                                    .host(scrollVersionsUrl.getHost())
                                    .port(port)
                                    .addPathSegments(scrollVersionsUrl.getPath().replaceAll("^/+", ""));
    }

    Request.Builder requestBuilder() {
        return new Request.Builder().header("Authorization", okhttp3.Credentials.basic(getCredentials().username, getCredentials().password))
                                    .header("X-Atlassian-Token", "nocheck");
    }

    @Override
    public Credentials getCredentials() {
        return delegate.getCredentials();
    }

    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> findPageByTitle(String parentPageId, String title)  {
       
        final CompletableFuture<?> result = delegate.findPageByTitle(parentPageId, title)
               .thenCompose( page -> {
                   if( page.isPresent() ) {
                       return getPage(page.get().getSpace(), page.get().getTitle());
                   }
                   return completedFuture( Optional.empty() );
                });

        return (CompletableFuture<Optional<? extends Model.PageSummary>>) result;
        
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {

        return delegate.findPageByTitle(parentPage.getId(), title)
                            .thenCompose( page -> removePage(page.get().getId()) );
    }

    @Override
    public CompletableFuture<Boolean> removePage(String pageId) {
        return getDotPageId(pageId)
                .thenCompose( dotPageId -> delegate.removePage(dotPageId) )
                ;   
    }

    
    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {
        return getMasterPageId(parentPage.getId())
                .thenCompose(parentPageId -> getScrollVersionId(parentPage.getSpace())
                                                 .thenCompose(scrollVersionId -> {
            
            val httpUrl = urlBuilder().addPathSegment("page")
                                                 .addPathSegment("new")
                                                 .addPathSegment(parentPage.getSpace())
                                                 .addQueryParameter("parentConfluenceId", parentPageId)
                                                 .addEncodedQueryParameter("pageTitle", title)
                                                 .addQueryParameter("versionId", scrollVersionId)
                                                 .build();
            val body = new FormBody.Builder()
                    //todo
                    .add("message", "Your message")
                    .build();
            
            Request request = requestBuilder().url(httpUrl)
                                                 .post(body)
                                                 .build();

            
            return delegate.fromRequestAsync( request )
                .thenCompose( response -> getCreatedPageAsync(response, title) )
                .exceptionally( ex -> {
                    if( ex.getMessage().contains("Validation Error: A page already exists with the title") ) {
                        return parentPage;
                    }
                    throw new IllegalStateException( ex );
                })
                .thenCompose( (id) -> updatePage( parentPage, title, scrollVersionId))
                ; 
         }));
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
        return getDotPageId(pageId)
                .thenComposeAsync(delegate::getPage)
                .exceptionally( e -> Optional.empty())
                ;
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
        if( pageTitle.startsWith("?") ) {
            return delegate.getPage( spaceKey, pageTitle.substring(1));
        }
        return getDotPageId(spaceKey, pageTitle)
                    .thenCompose(delegate::getPage)
                    .exceptionally( e -> Optional.empty())
                    ;
  }

    @Override
    public CompletableFuture<Void> addLabelsByName(long id, String[] labels ) {
        return getDotPageId(String.valueOf(id))
                .thenCompose( pageId -> delegate.addLabelsByName( pageId, labels) );
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? delegate.storePage(dotPage.get(), content)
                                                            : completeExceptionally(format("failed to store page with id %s", page.getId())));
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? delegate.storePage(dotPage.get())
                                                            : completeExceptionally(format("failed to store page with id %s", page.getId())));
    }

    @Override
    public CompletableFuture<List<Model.PageSummary>> getDescendents(String pageId)  {
        return getMasterPageId(pageId)
                .thenCompose( id -> delegate.getDescendents(id) )
                .thenCompose( descendents ->
                    completedFuture(
                            descendents.stream()
                            .filter(page -> isDotPage(page.getSpace(), page.getTitle()))
                            .skip(1) // SKIP PARENT PAGE
                            .collect(Collectors.toList()) ));
    }

    @Override
    public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
        String dotTitle = getPage(spaceKey, pageTitle).get().get().getTitle();
        delegate.exportPage(url, spaceKey, dotTitle, exfmt, outputFile);
    }

    @Override
    public Model.Attachment createAttachment() {
        return delegate.createAttachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
        return getDotPageId(pageId)
                .thenCompose(versionedPageId -> delegate.getAttachment(versionedPageId, name, version))
                .exceptionally( e -> Optional.empty())
                ;
    }

    @Override //todo: really?
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        return delegate.addAttachment(page, attachment, source);
    }

    @Override
    public void close() {
        // nothing to close
    }

    @Override
    public CompletableFuture<Model.Page> getOrCreatePage(String spaceKey, String parentPageTitle, String title) {
        return getPage(spaceKey, parentPageTitle).thenCompose(optionalParentPage -> {
            val futureResult = new CompletableFuture<Model.Page>();
            
            if( !optionalParentPage.isPresent() ) {
                val msg = format("no parent page found with title %s", parentPageTitle);
                return completeExceptionally(futureResult, msg );
            }
            
            return getPage(spaceKey, title).thenCompose( optionalPage -> {
                
                if( !optionalPage.isPresent() ) {                  
                    return createPage(optionalParentPage.get(), title);
                }
                else {
                    futureResult.complete(optionalPage.get());
                }
                
                return futureResult;
            })
            //.exceptionally( e -> createPage(optionalParentPage.get(), title).join())
            ;
            
        });
    }

    private CompletableFuture<Model.Page> getCreatedPageAsync( Response response, String title) {
        val result = new CompletableFuture<Model.Page>();
        
        try {
            val responseBody = response.body().string();
            val scrollVersionsNewPage   = objectMapper.readValue(responseBody, ScrollVersionsNewPage.class);
            
            delegate.retry( GET_CREATED_PAGE_MAX_RETRIES, GET_CREATED_PAGE_WAIT_MS, TimeUnit.MILLISECONDS, Optional.of(result), () -> {
                val pageId = scrollVersionsNewPage.getConfluencePage().getId();
                return getPage( String.valueOf(pageId) )
                        .thenApply(Optional::get);
            });
        } catch (IOException e) {
            
            completeExceptionally( result, "error getting created page", e );
        }
        
        return result;

    }
    
    private CompletableFuture<String> getDotPageId(String pageId) {
        return delegate.getPage(pageId)
                        .thenCompose(optionalPage -> 
                            optionalPage.map( page ->  getDotPageId(page.getSpace(), page.getTitle()) )
                                        .orElseGet( () -> completeExceptionally(format("no page found for id %s", pageId))))
                                        ;  
       
    }

    private CompletableFuture<String> getDotPageId(String spaceKey, String pageTitle) {
        if ( isDotPage(spaceKey, pageTitle) ) {
            return  delegate.getPage(spaceKey, pageTitle)
                            .thenCompose( optionalPage -> mapToPageId(spaceKey, pageTitle, optionalPage));
        }

        final ScrollVersionsPageByTitle[] scrollVersionsPageByTitle = {
                new ScrollVersionsPageByTitle(pageTitle)
        };

        val httpUrl = urlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, mapToJsonString(scrollVersionsPageByTitle));
        val request = requestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        return delegate.fromRequestAsync(request).thenCompose( response -> {
            val future = new CompletableFuture<String>();
            
            val responseBody = response.body();
            
            if (null == responseBody) {
                future.completeExceptionally(new ScrollVersionsException(format("could not retrieve content for page %s in space %s with version %s",
                        pageTitle,
                        spaceKey,
                        scrollVersionsName)));
                return future ;
            }
            
            try {
                val responseBodyString = responseBody.string();
                
                val pages = objectMapper.readValue(responseBodyString, ScrollVersionsPage[].class);
                
                Optional<String> optionalFutureVersionedPageId = Arrays.stream(pages)
                        .filter(scrollVersionsPage -> scrollVersionsName.equals(scrollVersionsPage.getTargetVersion().getName()))
                        .map(ScrollVersionsPage::getConfluencePageId)
                        .map(String::valueOf)
                        .findAny();
                if (optionalFutureVersionedPageId.isPresent()) {
                    future.complete(optionalFutureVersionedPageId.get());
                } else {
                    val msg = format("no page named %s found in space %s with version %s",
                                                                        pageTitle,
                                                                        spaceKey,
                                                                        scrollVersionsName);
                    completeExceptionally( future, msg );
                }
            } catch (IOException e) {
                    val msg = format("could not interpret response for page named %s in space %s with version %s",
                            pageTitle,
                            spaceKey,
                            scrollVersionsName);
                    completeExceptionally( future, msg, e );
                }
            
            
            return future;
        })
        
        ;
        
    }

    CompletableFuture<String> getScrollVersionId(String spaceKey) {  

        val httpUrl = urlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = requestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        return delegate.fromRequestAsync(request).thenCompose( response -> {
            
            val futureResult = new CompletableFuture<String>();
            
            val responseBody = response.body();

            if (null == responseBody) {
                futureResult.completeExceptionally(new ScrollVersionsException("could not retrieve versions info"));
                return futureResult;
            } 
            
            try {
                val responseBodyString = responseBody.string();
                
                val pages = objectMapper.readValue(responseBodyString, ScrollVersionsPage[].class);
                
                Optional<String> optionalVersionId = Arrays.stream(pages)
                                                           .map(ScrollVersionsPage::getTargetVersion)
                                                           .filter(scrollVersionsTargetVersion -> scrollVersionsName.equals(scrollVersionsTargetVersion.getName()))
                                                           .map(ScrollVersionsTargetVersion::getVersionId)
                                                           .findAny();

                if (optionalVersionId.isPresent()) {
                    futureResult.complete(optionalVersionId.get());
                } else {
                    val msg = format("no version named %s found in space %s",
                                                            scrollVersionsName,
                                                            spaceKey);
                    completeExceptionally( futureResult, msg );
                }
            } catch (IOException e) {
                val msg = "could not interpret response" ;
                completeExceptionally( futureResult, msg, e );            
            }
            
            return futureResult;
        });
                    
    }

    CompletableFuture<String> getMasterPageId(String pageId) {
        return getPage(pageId).thenCompose(optionalPage -> {

            val futureResult = new CompletableFuture<String>();

            if (optionalPage.isPresent()) {
                
                val page = optionalPage.get();
                
                getMasterPageTitle(page.getSpace(), page.getTitle())
                        .thenCompose(masterPageTitle -> delegate.getPage(page.getSpace(), masterPageTitle))
                        .thenAccept(optionalMasterPage -> {
                           if (optionalMasterPage.isPresent()) {
                               futureResult.complete(optionalMasterPage.get().getId());
                           } else {
                               val msg = format("failed to retrieve master page for page %s in space %s with version %s",
                                                                 page.getTitle(),
                                                                 page.getSpace(),
                                                                 scrollVersionsName);
                               completeExceptionally( futureResult, msg );
                           }
                       });
            } else {
                val msg = format("failed to retrieve master page for page id %s with version %s",
                                                                              pageId,
                                                                              scrollVersionsName);
                completeExceptionally( futureResult, msg );
            }
            return futureResult;
        });
    }

    private CompletableFuture<String> getMasterPageTitle(String spaceKey, String pageTitle) {
        if (!isDotPage(spaceKey, pageTitle)) {
            return delegate
                    .getPage(spaceKey, pageTitle)                            
                    .thenCompose(optionalPage -> mapToPageId(spaceKey, pageTitle, optionalPage));
        }

        val httpUrl = urlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = requestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        
        return delegate.fromRequestAsync(request).thenCompose( response -> {
            val futureResult = new CompletableFuture<String>();

            try {
                val responseBody = response.body().string();
           
                val pages = objectMapper.readValue(responseBody, ScrollVersionsPage[].class);
                Optional<String> optionalMasterPageTitle = Arrays.stream(pages)
                                                                 .filter(page -> scrollVersionsName.equals(page.getTargetVersion().getName())
                                                                                 && spaceKey.equals(page.getSpaceKey())
                                                                                 && pageTitle.equals(page.getConfluencePageTitle()))
                                                                 .map(ScrollVersionsPage::getScrollPageTitle)
                                                                 .findAny();

                if (optionalMasterPageTitle.isPresent()) {
                    futureResult.complete(optionalMasterPageTitle.get());
                } else {
                    val msg = format("failed to retrieve page %s in space %s with version %s",
                            pageTitle,
                            spaceKey,
                            scrollVersionsName);
                    completeExceptionally( futureResult, msg );
                }
            
            
            } catch (IOException e) {
                val msg = format("failed to retrieve page %s in space %s with version %s",
                        pageTitle,
                        spaceKey,
                        scrollVersionsName);
                completeExceptionally( futureResult, msg, e );
            }

            
            return futureResult;
            
        });
    }

    @SneakyThrows
    boolean isDotPage(String spaceKey, String pageTitle) {
        
        val httpUrl = urlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = requestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();
        val responseBody = delegate.fromRequest(request, "isDotPage" ).body().string();
        
        return Arrays.stream(objectMapper.readValue(responseBody, ScrollVersionsPage[].class))
                     .anyMatch(page -> scrollVersionsName.equals(page.getTargetVersion().getName())
                                       && spaceKey.equals(page.getSpaceKey())
                                       && pageTitle.equals(page.getConfluencePageTitle()));
    }

    CompletableFuture<String> mapToPageId(String spaceKey, String pageTitle, Optional<Model.Page> optionalPage) {
        val future = new CompletableFuture<String>();
        
        if( optionalPage.isPresent() ) {
            future.complete(optionalPage.get().getId());
        }
        else {
            val msg = format("failed to retrieve page %s in space %s with version %s",              
                    pageTitle,
                    spaceKey,
                    scrollVersionsName);
            completeExceptionally( future, msg);            
        }
        
        return future;
    }

    @SneakyThrows
    String mapToJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }
    
    /**
     * 
     * @param parentPage
     * @param title
     * @param scrollVersionId
     * @return
     */
    private CompletableFuture<Model.Page> updatePage( Model.Page parentPage, String title, String scrollVersionId ) {
        
        return delegate.getPage(parentPage.getSpace(), title)
        .thenCompose( optionalMasterPage -> 
            optionalMasterPage.map( p -> completedFuture(p) )
                              .orElseGet( () -> completeExceptionally( format("failed to get master page with title %s in space %s", title, parentPage.getSpace())) ) )
        .thenCompose(masterPage -> {

            val httpUrl = urlBuilder().addPathSegment("page")
                                                 .addPathSegment("modify")
                                                 .addQueryParameter("masterPageId", masterPage.getId())
                                                 .addEncodedQueryParameter("pageTitle", masterPage.getTitle())
                                                 .addQueryParameter("versionId", scrollVersionId)
                                                 .addQueryParameter("changeType", CHANGE_TYPE_MODIFY_QUERY_PARAM)
                                                 .build();
            val request = requestBuilder().url(httpUrl)
                                                 .post(new FormBody.Builder().build())
                                                 .build();

            return delegate.fromRequestAsync( request )
                    .thenCompose( response -> getCreatedPageAsync( response, title ))
                    ;   
        });
    }
    
    /**
     * 
     * @param <U>
     * @param ex
     * @return
     */
    @Deprecated
    <U> CompletableFuture<U> completeExceptionally(Throwable ex) {
        val completableFuture = new CompletableFuture<U>();
        completableFuture.completeExceptionally(ex);
        return completableFuture;
    }
    
    /**
     * 
     * @param <U>
     * @param message
     * @return
     */
    <U> CompletableFuture<U> completeExceptionally( String message ) {
        val completableFuture = new CompletableFuture<U>();
        return completeExceptionally( completableFuture, message );
    }

    /**
     * 
     * @param <U>
     * @param message
     * @param ex
     * @return
     */
    <U> CompletableFuture<U> completeExceptionally( String message, Throwable ex ) {
        val completableFuture = new CompletableFuture<U>();
        return completeExceptionally( completableFuture, message, ex );
    }
    
    /**
     * 
     * @param <T>
     * @param future
     * @param message
     * @return
     */
    <T> CompletableFuture<T> completeExceptionally( CompletableFuture<T> future, String message) {
        future.completeExceptionally(new ScrollVersionsException(format(message)));
        return future;
    }
    
    /**
     * 
     * @param <T>
     * @param future
     * @param message
     * @param e
     * @return
     */
    <T> CompletableFuture<T> completeExceptionally( CompletableFuture<T> future, String message, Throwable e) {
        future.completeExceptionally(new ScrollVersionsException(format(message), e));
        return future;
    }
    
}