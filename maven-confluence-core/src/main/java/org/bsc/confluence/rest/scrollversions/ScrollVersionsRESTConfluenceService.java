package org.bsc.confluence.rest.scrollversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsNewPage;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsPage;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsPageByTitle;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersionsTargetVersion;
import org.bsc.ssl.SSLCertificateInfo;

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
import java.util.stream.Collectors;

/**
 * Implements a {@link ConfluenceService} with Scroll Versions support via REST APIs.
 *
 * @author Nicola Lagnena
 * @see <a href="https://help.k15t.com/scroll-versions/latest/api-resources-164037049.html">Scroll Versions API resources</a>
 */
public class ScrollVersionsRESTConfluenceService implements ConfluenceService {

    public static final String CHANGE_TYPE_MODIFY_QUERY_PARAM = "Modify";

    public class ScrollVersionsException extends Exception {
        public ScrollVersionsException(String message) {
            super(message);
        }

        public ScrollVersionsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String REQUEST_BODY_GET_ALL_VERSIONED_PAGES = "[{\"queryArg\": \"pageType\", \"value\": \"change\"}]";
    private static final Integer GET_CREATED_PAGE_MAX_RETRIES = 3;
    private static final Long GET_CREATED_PAGE_WAIT_MS = 3000L;

    private final RESTConfluenceServiceImpl restConfluenceServiceDelegate;

    private final Credentials credentials;
    private final URL scrollVersionsUrl;
    private final String scrollVersionName;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public ScrollVersionsRESTConfluenceService(String confluenceUrl, String scrollVersionsUrl, Credentials credentials, SSLCertificateInfo sslCertificateInfo, String scrollVersionName) {
        Objects.requireNonNull(credentials, "credentials cannot be null");
        this.credentials = credentials;

        Objects.requireNonNull(scrollVersionsUrl, "url cannot be null");
        try {
            this.scrollVersionsUrl = new URL(scrollVersionsUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url", e);
        }

        Objects.requireNonNull(sslCertificateInfo, "ssl info cannot be null");

        Objects.requireNonNull(scrollVersionName, "scroll version name cannot be null");
        this.scrollVersionName = scrollVersionName;

        this.restConfluenceServiceDelegate = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslCertificateInfo);
        this.objectMapper = new ObjectMapper();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (!sslCertificateInfo.isIgnore() && "https".equals(this.scrollVersionsUrl.getProtocol())) {
            okHttpClientBuilder.hostnameVerifier(sslCertificateInfo.getHostnameVerifier())
                               .sslSocketFactory(sslCertificateInfo.getSSLSocketFactory(),
                                                 sslCertificateInfo.getTrustManager());
        }

        this.okHttpClient = okHttpClientBuilder.build();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        Model.PageSummary page = restConfluenceServiceDelegate.findPageByTitle(parentPageId, title);
        return getPage(page.getSpace(), page.getTitle()).get()
                                                        .orElseThrow(() -> new ScrollVersionsException(String.format("no page found for parent id %s and title %s in version %s",
                                                                                                                     parentPageId,
                                                                                                                     title,
                                                                                                                     scrollVersionName)));
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        try {
            Model.PageSummary page = restConfluenceServiceDelegate.findPageByTitle(parentPage.getId(), title);
            removePage(page.getId());
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    @SneakyThrows
    public void removePage(String pageId) {
        restConfluenceServiceDelegate.removePage(getDotPageId(pageId).get());
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {
        return getMasterPageId(parentPage.getId()).thenCompose(parentPageId -> getScrollVersionId(parentPage.getSpace()).thenCompose(scrollVersionId -> {
            HttpUrl httpUrl = getHttpUrlBuilder().addPathSegment("page")
                                                 .addPathSegment("new")
                                                 .addPathSegment(parentPage.getSpace())
                                                 .addQueryParameter("parentConfluenceId", parentPageId)
                                                 .addEncodedQueryParameter("pageTitle", title)
                                                 .addQueryParameter("versionId", scrollVersionId)
                                                 .build();
            Request request = getRequestBuilder().url(httpUrl)
                                                 .post(new FormBody.Builder()
                                                               //todo
                                                               .add("message", "Your message")
                                                               .build())
                                                 .build();

            CompletableFuture<Model.Page> futurePage = new CompletableFuture<>();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    futurePage.completeExceptionally(new ScrollVersionsException(String.format("failed to create page with title %s", title)));
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        getCreatedPage(response, futurePage, title);
                    } catch (Exception e) {
                        if (e.getMessage().contains("Validation Error: A page already exists with the title")) {
                            try {
                                Optional<Model.Page> optionalMasterPage = restConfluenceServiceDelegate.getPage(parentPage.getSpace(), title).get();
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
                                                                                                                       scrollVersionName),
                                                                                                         e));
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            try {
                                                getCreatedPage(response, futurePage, title);
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

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
        return getDotPageId(pageId).thenCompose(restConfluenceServiceDelegate::getPage);
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
        return getDotPageId(spaceKey, pageTitle).thenCompose(restConfluenceServiceDelegate::getPage);
    }

    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        return restConfluenceServiceDelegate.addLabelByName(label, Long.parseLong(getDotPageId(String.valueOf(id)).get()));
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? restConfluenceServiceDelegate.storePage(dotPage.get(), content)
                                                            : failedFuture(new ScrollVersionsException(String.format("failed to store page with id %s", page.getId()))));
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        return getPage(page.getId()).thenCompose(dotPage -> dotPage.isPresent()
                                                            ? restConfluenceServiceDelegate.storePage(dotPage.get())
                                                            : failedFuture(new ScrollVersionsException(String.format("failed to store page with id %s", page.getId()))));
    }

    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
        return restConfluenceServiceDelegate.getDescendents(getMasterPageId(pageId).get())
                                            .stream()
                                            .filter(page -> isDotPage(page.getSpace(), page.getTitle()))
                                            .collect(Collectors.toList());
    }

    @Override
    public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
        String dotTitle = getPage(spaceKey, pageTitle).get().get().getTitle();
        restConfluenceServiceDelegate.exportPage(url, spaceKey, dotTitle, exfmt, outputFile);
    }

    @Override
    public Model.Attachment createAttachment() {
        return restConfluenceServiceDelegate.createAttachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
        return getDotPageId(pageId).thenCompose(versionedPageId -> restConfluenceServiceDelegate.getAttachment(versionedPageId, name, version));
    }

    @Override //todo: really?
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        return restConfluenceServiceDelegate.addAttachment(page, attachment, source);
    }

    @Override
    public void close() {
        // nothing to close
    }

    @Override
    public CompletableFuture<Model.Page> getOrCreatePage(String spaceKey, String parentPageTitle, String title) {
        return getPage(spaceKey, parentPageTitle).thenCompose(parentPage -> {
            if (parentPage.isPresent()) {
                try {
                    Optional<Model.Page> optionalFoundPage = getPage(spaceKey, title).get();
                    if (optionalFoundPage.isPresent()) {
                        return CompletableFuture.completedFuture(optionalFoundPage);
                    } else {
                        return failedFuture(new ScrollVersionsException(String.format("error getting page with title %s", title)));
                    }
                } catch (Exception e) {
                    try {
                        Model.Page createdPage = createPage(parentPage.get(), title).get();
                        return CompletableFuture.completedFuture(Optional.of(createdPage));
                    } catch (Exception ex) {
                        return failedFuture(new ScrollVersionsException(String.format("error creating page with title %s", title), ex));
                    }
                }
            } else {
                return failedFuture(new ScrollVersionsException(String.format("no parent page found with title %s", parentPageTitle)));
            }
        }).thenApply(Optional::get);
    }

    private void getCreatedPage(Response response, CompletableFuture<Model.Page> futurePage, String title) throws IOException, InterruptedException {
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

    private CompletableFuture<String> getDotPageId(String pageId) {
        return restConfluenceServiceDelegate.getPage(pageId)
                                            .thenCompose(optionalPage -> {
                                                if (optionalPage.isPresent()) {
                                                    Model.Page page = optionalPage.get();
                                                    return getDotPageId(page.getSpace(), page.getTitle());
                                                } else {
                                                    return failedFuture(new ScrollVersionsException(String.format("no page found for id %s", pageId)));
                                                }
                                            });
    }

    private CompletableFuture<String> getDotPageId(String spaceKey, String pageTitle) {
        if (isDotPage(spaceKey, pageTitle)) {
            return restConfluenceServiceDelegate.getPage(spaceKey, pageTitle)
                                                .thenCompose(optionalPage -> mapToPageId(spaceKey, pageTitle, optionalPage));
        }

        ScrollVersionsPageByTitle[] scrollVersionsPageByTitle = new ScrollVersionsPageByTitle[]{new ScrollVersionsPageByTitle(pageTitle)};

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

    private CompletableFuture<String> getScrollVersionId(String spaceKey) {
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

    private CompletableFuture<String> getMasterPageId(String pageId) {
        return getPage(pageId).thenCompose(optionalPage -> {
            if (optionalPage.isPresent()) {
                Model.Page page = optionalPage.get();
                return getMasterPageTitle(page.getSpace(), page.getTitle()).thenCompose(masterPageTitle -> restConfluenceServiceDelegate.getPage(page.getSpace(), masterPageTitle))
                                                                           .thenCompose(optionalMasterPage -> {
                                                                               if (optionalMasterPage.isPresent()) {
                                                                                   return CompletableFuture.completedFuture(optionalMasterPage.get().getId());
                                                                               } else {
                                                                                   return failedFuture(new ScrollVersionsException(String.format("failed to retrieve master page for page %s in space %s with version %s",
                                                                                                                                                 page.getTitle(),
                                                                                                                                                 page.getSpace(),
                                                                                                                                                 scrollVersionName)));
                                                                               }
                                                                           });
            } else {
                return failedFuture(new ScrollVersionsException(String.format("failed to retrieve master page for page id %s with version %s",
                                                                              pageId,
                                                                              scrollVersionName)));
            }
        });
    }

    private CompletableFuture<String> getMasterPageTitle(String spaceKey, String pageTitle) {
        if (!isDotPage(spaceKey, pageTitle)) {
            return restConfluenceServiceDelegate.getPage(spaceKey, pageTitle)
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

    @SneakyThrows
    private boolean isDotPage(String spaceKey, String pageTitle) {
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

    private HttpUrl.Builder getHttpUrlBuilder() {
        return new HttpUrl.Builder().scheme(scrollVersionsUrl.getProtocol())
                                    .host(scrollVersionsUrl.getHost())
                                    .port(scrollVersionsUrl.getPort())
                                    .addPathSegments(scrollVersionsUrl.getPath().replaceAll("^/+", ""));
    }

    private Request.Builder getRequestBuilder() {
        return new Request.Builder().header("Authorization", okhttp3.Credentials.basic(getCredentials().username, getCredentials().password))
                                    .header("X-Atlassian-Token", "nocheck");
    }

    private CompletableFuture<String> mapToPageId(String spaceKey, String pageTitle, Optional<Model.Page> optionalPage) {
        return optionalPage.map(page -> CompletableFuture.completedFuture(page.getId()))
                           .orElseGet(() -> failedFuture(new ScrollVersionsException(String.format("failed to retrieve page %s in space %s with version %s",
                                                                                                   pageTitle,
                                                                                                   spaceKey,
                                                                                                   scrollVersionName))));
    }

    @SneakyThrows
    private String mapToJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    private static <U> CompletableFuture<U> failedFuture(Throwable ex) {
        CompletableFuture<U> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(ex);
        return completableFuture;
    }

}