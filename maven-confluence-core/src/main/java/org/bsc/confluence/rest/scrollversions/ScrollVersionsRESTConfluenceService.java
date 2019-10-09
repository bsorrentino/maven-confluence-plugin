package org.bsc.confluence.rest.scrollversions;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implements a {@link ConfluenceService} with Scroll Versions support via REST APIs.
 *
 * @author Nicola Lagnena
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
    final String        scrollVersionName;
    final ObjectMapper  objectMapper;

    public ScrollVersionsRESTConfluenceService( String confluenceUrl, 
                                                String scrollVersionsUrl, 
                                                Credentials credentials, 
                                                SSLCertificateInfo sslCertificateInfo, 
                                                String scrollVersionName) 
    {

        Objects.requireNonNull(scrollVersionsUrl, "url cannot be null");
        try {
            this.scrollVersionsUrl = new URL(scrollVersionsUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url", e);
        }

        Objects.requireNonNull(scrollVersionName, "scroll version name cannot be null");
        this.scrollVersionName = scrollVersionName;

        this.delegate = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslCertificateInfo);
        this.objectMapper = new ObjectMapper();

    }

    @Override
    public Credentials getCredentials() {
        return delegate.getCredentials();
    }

    @Override
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
       
        Supplier<ScrollVersionsException> notFoundException = () -> 
                new ScrollVersionsException(format("no page found for parent id %s and title %s in version %s",
                    parentPageId,
                    title,                                                                                                                   
                    scrollVersionName));
                
        val page = delegate.findPageByTitle(parentPageId, title);
        
        return getPage(page.getSpace(), page.getTitle())
                    .get()
                    .orElseThrow(notFoundException);
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        try {
            
            Model.PageSummary page = delegate.findPageByTitle(parentPage.getId(), title);
            return removePageAsync(page.getId());
            
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public CompletableFuture<Boolean> removePageAsync(String pageId) {
        return getDotPageId(pageId)
                .thenCompose( dotPageId -> delegate.removePageAsync(dotPageId) )
                ;   
    }

    
    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {
        return getMasterPageId(parentPage.getId())
                .thenCompose(parentPageId -> getScrollVersionId(parentPage.getSpace())
                                                 .thenCompose(scrollVersionId -> 
         {
            
            val httpUrl = getHttpUrlBuilder().addPathSegment("page")
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
            
            Request request = getRequestBuilder().url(httpUrl)
                                                 .post(body)
                                                 .build();

            
            return delegate.fromRequestAsync( request )
                .thenCompose( response -> getCreatedPageAsync(response, title) )
                ; 
         }));
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
        return getDotPageId(pageId).thenCompose(delegate::getPage);
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
        return getDotPageId(spaceKey, pageTitle).thenCompose(delegate::getPage);
    }

    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        return delegate.addLabelByName(label, parseLong(getDotPageId(String.valueOf(id)).get()));
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? delegate.storePage(dotPage.get(), content)
                                                            : completeExceptionally(new ScrollVersionsException(String.format("failed to store page with id %s", page.getId()))));
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? delegate.storePage(dotPage.get())
                                                            : completeExceptionally(new ScrollVersionsException(String.format("failed to store page with id %s", page.getId()))));
    }

    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        return delegate.getDescendents(getMasterPageId(pageId).get())
                                            .stream()
                                            .filter(page -> isDotPage(page.getSpace(), page.getTitle()))
                                            .collect(Collectors.toList());
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
        return getDotPageId(pageId).thenCompose(versionedPageId -> delegate.getAttachment(versionedPageId, name, version));
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
                            .orElseThrow( () -> new IllegalStateException(String.format("no page found for id %s", pageId))))
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

        val httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, mapToJsonString(scrollVersionsPageByTitle));
        val request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        return delegate.fromRequestAsync(request).thenCompose( response -> {
            val future = new CompletableFuture<String>();
            
            val responseBody = response.body();
            
            if (null == responseBody) {
                future.completeExceptionally(new ScrollVersionsException(format("could not retrieve content for page %s in space %s with version %s",
                        pageTitle,
                        spaceKey,
                        scrollVersionName)));
                return future ;
            }
            
            try {
                val responseBodyString = responseBody.string();
                
                val pages = objectMapper.readValue(responseBodyString, ScrollVersionsPage[].class);
                
                Optional<String> optionalFutureVersionedPageId = Arrays.stream(pages)
                        .filter(scrollVersionsPage -> scrollVersionName.equals(scrollVersionsPage.getTargetVersion().getName()))
                        .map(ScrollVersionsPage::getConfluencePageId)
                        .map(String::valueOf)
                        .findAny();
                if (optionalFutureVersionedPageId.isPresent()) {
                    future.complete(optionalFutureVersionedPageId.get());
                } else {
                    val msg = format("no page named %s found in space %s with version %s",
                                                                        pageTitle,
                                                                        spaceKey,
                                                                        scrollVersionName);
                    completeExceptionally( future, msg );
                }
            } catch (IOException e) {
                    val msg = format("could not interpret response for page named %s in space %s with version %s",
                            pageTitle,
                            spaceKey,
                            scrollVersionName);
                    completeExceptionally( future, msg, e );
                }
            
            
            return future;
        })
        
        ;
        
    }

    CompletableFuture<String> getScrollVersionId(String spaceKey) {  

        val httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = getRequestBuilder().url(httpUrl)
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
                                                           .filter(scrollVersionsTargetVersion -> scrollVersionName.equals(scrollVersionsTargetVersion.getName()))
                                                           .map(ScrollVersionsTargetVersion::getVersionId)
                                                           .findAny();

                if (optionalVersionId.isPresent()) {
                    futureResult.complete(optionalVersionId.get());
                } else {
                    val msg = format("no version named %s found in space %s",
                                                            scrollVersionName,
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
                                                                 scrollVersionName);
                               completeExceptionally( futureResult, msg );
                           }
                       });
            } else {
                val msg = format("failed to retrieve master page for page id %s with version %s",
                                                                              pageId,
                                                                              scrollVersionName);
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

        val httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        
        return delegate.fromRequestAsync(request).thenCompose( response -> {
            val futureResult = new CompletableFuture<String>();

            try {
                val responseBody = response.body().string();
           
                val pages = objectMapper.readValue(responseBody, ScrollVersionsPage[].class);
                Optional<String> optionalMasterPageTitle = Arrays.stream(pages)
                                                                 .filter(page -> scrollVersionName.equals(page.getTargetVersion().getName())
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
                            scrollVersionName);
                    completeExceptionally( futureResult, msg );
                }
            
            
            } catch (IOException e) {
                val msg = format("failed to retrieve page %s in space %s with version %s",
                        pageTitle,
                        spaceKey,
                        scrollVersionName);
                completeExceptionally( futureResult, msg, e );
            }

            
            return futureResult;
            
        });
    }

    @SneakyThrows
    boolean isDotPage(String spaceKey, String pageTitle) {
        
        val httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        val body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        val request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();
        val responseBody = delegate.fromRequest(request, "isDotPage" ).body().string();
        
        return Arrays.stream(objectMapper.readValue(responseBody, ScrollVersionsPage[].class))
                     .anyMatch(page -> scrollVersionName.equals(page.getTargetVersion().getName())
                                       && spaceKey.equals(page.getSpaceKey())
                                       && pageTitle.equals(page.getConfluencePageTitle()));
    }

    HttpUrl.Builder getHttpUrlBuilder() {
        return new HttpUrl.Builder().scheme(scrollVersionsUrl.getProtocol())
                                    .host(scrollVersionsUrl.getHost())
                                    .port(scrollVersionsUrl.getPort())
                                    .addPathSegments(scrollVersionsUrl.getPath().replaceAll("^/+", ""));
    }

    Request.Builder getRequestBuilder() {
        return new Request.Builder().header("Authorization", okhttp3.Credentials.basic(getCredentials().username, getCredentials().password))
                                    .header("X-Atlassian-Token", "nocheck");
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
                    scrollVersionName);
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
     * @param <U>
     * @param ex
     * @return
     */
    <U> CompletableFuture<U> completeExceptionally(Throwable ex) {
        val completableFuture = new CompletableFuture<U>();
        completableFuture.completeExceptionally(ex);
        return completableFuture;
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

@Deprecated
class DeprecatedMethods extends ScrollVersionsRESTConfluenceService {
    final OkHttpClient  okHttpClient;
    final Credentials   credentials;

    public DeprecatedMethods(String confluenceUrl, String scrollVersionsUrl, Credentials credentials,
            SSLCertificateInfo sslCertificateInfo, String scrollVersionName) {
        super(confluenceUrl, scrollVersionsUrl, credentials, sslCertificateInfo, scrollVersionName);
        
        Objects.requireNonNull(credentials, "credentials cannot be null");
        this.credentials = credentials;

        Objects.requireNonNull(sslCertificateInfo, "ssl info cannot be null");

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (!sslCertificateInfo.isIgnore() && "https".equals(this.scrollVersionsUrl.getProtocol())) {
            okHttpClientBuilder.hostnameVerifier(sslCertificateInfo.getHostnameVerifier())
                               .sslSocketFactory(sslCertificateInfo.getSSLSocketFactory(),
                                                 sslCertificateInfo.getTrustManager());
        }
        this.okHttpClient = okHttpClientBuilder.build();

    }
    public CompletableFuture<Model.Page> _getOrCreatePage(String spaceKey, String parentPageTitle, String title) {
        return getPage(spaceKey, parentPageTitle).thenCompose(parentPage -> {
            
            if (parentPage.isPresent()) {
                try {
                    Optional<Model.Page> optionalFoundPage = getPage(spaceKey, title).get();
                    if (optionalFoundPage.isPresent()) {
                        return CompletableFuture.completedFuture(optionalFoundPage);
                    } else {
                        return completeExceptionally(new ScrollVersionsException(String.format("error getting page with title %s", title)));
                    }
                } catch (Exception e) {
                    try {
                        Model.Page createdPage = createPage(parentPage.get(), title).get();
                        return CompletableFuture.completedFuture(Optional.of(createdPage));
                    } catch (Exception ex) {
                        return completeExceptionally(new ScrollVersionsException(String.format("error creating page with title %s", title), ex));
                    }
                }
            } else {
                return completeExceptionally(new ScrollVersionsException(String.format("no parent page found with title %s", parentPageTitle)));
            }
        }).thenApply(Optional::get);
    }
    
    @SuppressWarnings("unused")
    private CompletableFuture<String> _getMasterPageTitle(String spaceKey, String pageTitle) {
        if (!isDotPage(spaceKey, pageTitle)) {
            return delegate.getPage(spaceKey, pageTitle)
                                                .thenCompose(optionalPage -> mapToPageId(spaceKey, pageTitle, optionalPage));
        }

        HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        Request request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        CompletableFuture<String> futureMasterPageTitle = new CompletableFuture<>();
        okHttpClient.newCall(request)
                    .enqueue(new Callback() {

                        private void completeExceptionally(CompletableFuture<String> futureMasterPageId, String pageTitle, String spaceKey) {
                            futureMasterPageId.completeExceptionally(new ScrollVersionsException(String.format("failed to retrieve page %s in space %s with version %s",
                                                                                                               pageTitle,
                                                                                                               spaceKey,
                                                                                                               scrollVersionName)));
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            completeExceptionally(futureMasterPageTitle, pageTitle, spaceKey);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseBody = response.body().string();

                            Optional<String> optionalMasterPageTitle = Arrays.stream(objectMapper.readValue(responseBody, ScrollVersionsPage[].class))
                                                                             .filter(page -> scrollVersionName.equals(page.getTargetVersion().getName())
                                                                                             && spaceKey.equals(page.getSpaceKey())
                                                                                             && pageTitle.equals(page.getConfluencePageTitle()))
                                                                             .map(ScrollVersionsPage::getScrollPageTitle)
                                                                             .findAny();

                            if (optionalMasterPageTitle.isPresent()) {
                                futureMasterPageTitle.complete(optionalMasterPageTitle.get());
                            } else {
                                completeExceptionally(futureMasterPageTitle, pageTitle, spaceKey);
                            }
                        }
                    });
        return futureMasterPageTitle;
    }
    
    CompletableFuture<String> _getScrollVersionId(String spaceKey) {
        CompletableFuture<String> futureVersionId = new CompletableFuture<>();

        HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        Request request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                futureVersionId.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();

                if (null == responseBody) {
                    futureVersionId.completeExceptionally(new ScrollVersionsException("could not retrieve versions info"));
                } else {
                    try {
                        String responseBodyString = responseBody.string();
                        Optional<String> optionalVersionId = Arrays.stream(objectMapper.readValue(responseBodyString, ScrollVersionsPage[].class))
                                                                   .map(ScrollVersionsPage::getTargetVersion)
                                                                   .filter(scrollVersionsTargetVersion -> scrollVersionName.equals(scrollVersionsTargetVersion.getName()))
                                                                   .map(ScrollVersionsTargetVersion::getVersionId)
                                                                   .findAny();

                        if (optionalVersionId.isPresent()) {
                            futureVersionId.complete(optionalVersionId.get());
                        } else {
                            futureVersionId.completeExceptionally(new ScrollVersionsException(String.format("no version named %s found in space %s",
                                                                                                            scrollVersionName,
                                                                                                            spaceKey)));
                        }
                    } catch (IOException e) {
                        futureVersionId.completeExceptionally(new ScrollVersionsException("could not interpret response", e));
                    }
                }
            }
        });

        return futureVersionId;
    }
    
    @SneakyThrows
    private boolean _isDotPage(String spaceKey, String pageTitle) {
        HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                             .addPathSegment(spaceKey)
                                             .build();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, REQUEST_BODY_GET_ALL_VERSIONED_PAGES);
        Request request = getRequestBuilder().url(httpUrl)
                                             .post(body)
                                             .build();

        String responseBody = okHttpClient.newCall(request)
                                          .execute()
                                          .body()
                                          .string();

        return Arrays.stream(objectMapper.readValue(responseBody, ScrollVersionsPage[].class))
                     .anyMatch(page -> scrollVersionName.equals(page.getTargetVersion().getName())
                                       && spaceKey.equals(page.getSpaceKey())
                                       && pageTitle.equals(page.getConfluencePageTitle()));
    }
    
    private void _getCreatedPage( Response response, CompletableFuture<Model.Page> futurePage, String title) throws IOException, InterruptedException {
        String responseBody = response.body().string();
        ScrollVersionsNewPage scrollVersionsNewPage = objectMapper.readValue(responseBody, ScrollVersionsNewPage.class);

        // even though we have a successful response, trying to immediately retrieve the page that was just created
        // often results in a 404;
        Model.Page createdPage = null;
        int retries = 0;
        do {
            try {
                createdPage = getPage(String.valueOf(scrollVersionsNewPage.getConfluencePage().getId())).thenApply(Optional::get).get();
            } catch (Exception ignored) {
                TimeUnit.MILLISECONDS.sleep(GET_CREATED_PAGE_WAIT_MS);
            }
        } while (null == createdPage && retries++ < GET_CREATED_PAGE_MAX_RETRIES);

        if (null == createdPage) {
            futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to get created page with title %s after %d attempts", title, retries)));
        } else {
            futurePage.complete(createdPage);
        }
    }


    @SuppressWarnings("unused")
    private CompletableFuture<Model.Page> _createPage(Model.Page parentPage, String title) {
        return getMasterPageId(parentPage.getId())
                .thenCompose(parentPageId -> getScrollVersionId(parentPage.getSpace())
                                                 .thenCompose(scrollVersionId -> 
         {
            
            HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
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
            
            Request request = getRequestBuilder().url(httpUrl)
                                                 .post(body)
                                                 .build();                             
            
            final CompletableFuture<Model.Page> futurePage = new CompletableFuture<>();
            
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to create page with title %s", title)));
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        _getCreatedPage(response, futurePage, title);
                    } catch (Exception e) {
                        if (e.getMessage().contains("Validation Error: A page already exists with the title")) {
                            try {
                                Optional<Model.Page> optionalMasterPage = delegate.getPage(parentPage.getSpace(), title).get();
                                if (optionalMasterPage.isPresent()) {
                                    Model.Page masterPage = optionalMasterPage.get();

                                    HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                                                         .addPathSegment("modify")
                                                                         .addQueryParameter("masterPageId", masterPage.getId())
                                                                         .addEncodedQueryParameter("pageTitle", masterPage.getTitle())
                                                                         .addQueryParameter("versionId", scrollVersionId)
                                                                         .addQueryParameter("changeType", CHANGE_TYPE_MODIFY_QUERY_PARAM)
                                                                         .build();
                                    Request request = getRequestBuilder().url(httpUrl)
                                                                         .post(new FormBody.Builder().build())
                                                                         .build();

                                    okHttpClient.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to create versioned page with title %s in space %s with version %s",
                                                                                                                       title,
                                                                                                                       parentPage.getSpace(),
                                                                                                                       scrollVersionName), e));
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            try {
                                                _getCreatedPage(response, futurePage, title);
                                            } catch (Exception e) {
                                                futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to interpret result of create versioned page with title %s in space %s with version %s",
                                                                                                                           title,
                                                                                                                           parentPage.getSpace(),
                                                                                                                           scrollVersionName),
                                                                                                             e));
                                            }
                                        }
                                    });
                                } else {
                                    futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to get master page with title %s in space %s",
                                                                                                               title,
                                                                                                               parentPage.getSpace())));
                                }
                            } catch (Exception ex) {
                                futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to get master page with title %s in space %s",
                                                                                                           title,
                                                                                                           parentPage.getSpace()),
                                                                                             ex));
                            }
                        } else {
                            futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to get created page with title %s in space %s",
                                                                                                       title,
                                                                                                       parentPage.getSpace())));
                        }
                    }
                }
            });
            return futurePage;
        }));
    } 
        
    @SuppressWarnings("unused")
    private CompletableFuture<String> _getDotPageId(String spaceKey, String pageTitle) {
            if ( isDotPage(spaceKey, pageTitle) ) {
                return  delegate.getPage(spaceKey, pageTitle)
                                .thenCompose( optionalPage -> mapToPageId(spaceKey, pageTitle, optionalPage));
            }

            ScrollVersionsPageByTitle[] scrollVersionsPageByTitle = {
                    new ScrollVersionsPageByTitle(pageTitle)
            };

            HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                                 .addPathSegment(spaceKey)
                                                 .build();
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, mapToJsonString(scrollVersionsPageByTitle));
            Request request = getRequestBuilder().url(httpUrl)
                                                 .post(body)
                                                 .build();

            CompletableFuture<String> futureVersionedPageId = new CompletableFuture<>();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    futureVersionedPageId.completeExceptionally(new ScrollVersionsException(String.format("failed to retrieve page %s in space %s with version %s",
                                                                                                          pageTitle,
                                                                                                          spaceKey,
                                                                                                          scrollVersionName),
                                                                                            e));
                }

                @Override
                public void onResponse(Call call, Response response) {
                    ResponseBody responseBody = response.body();

                    if (null == responseBody) {
                        futureVersionedPageId.completeExceptionally(new ScrollVersionsException(String.format("could not retrieve content for page %s in space %s with version %s",
                                                                                                              pageTitle,
                                                                                                              spaceKey,
                                                                                                              scrollVersionName)));
                    } else {
                        try {
                            String responseBodyString = responseBody.string();
                            Optional<String> optionalFutureVersionedPageId = Arrays.stream(objectMapper.readValue(responseBodyString, ScrollVersionsPage[].class))
                                                                                   .filter(scrollVersionsPage -> scrollVersionName.equals(scrollVersionsPage.getTargetVersion().getName()))
                                                                                   .map(ScrollVersionsPage::getConfluencePageId)
                                                                                   .map(String::valueOf)
                                                                                   .findAny();

                            if (optionalFutureVersionedPageId.isPresent()) {
                                futureVersionedPageId.complete(optionalFutureVersionedPageId.get());
                            } else {
                                futureVersionedPageId.completeExceptionally(new ScrollVersionsException(String.format("no page named %s found in space %s with version %s",
                                                                                                                      pageTitle,
                                                                                                                      spaceKey,
                                                                                                                      scrollVersionName)));
                            }
                        } catch (IOException e) {
                            futureVersionedPageId.completeExceptionally(new ScrollVersionsException(String.format("could not interpret response for page named %s in space %s with version %s",
                                                                                                                  pageTitle,
                                                                                                                  spaceKey,
                                                                                                                  scrollVersionName),
                                                                                                    e));
                        }
                    }
                }
            });

            return futureVersionedPageId;
        }
    
}