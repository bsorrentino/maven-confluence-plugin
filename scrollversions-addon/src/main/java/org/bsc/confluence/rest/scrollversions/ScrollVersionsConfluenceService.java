package org.bsc.confluence.rest.scrollversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersions;
import org.bsc.ssl.SSLCertificateInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class ScrollVersionsConfluenceService implements ConfluenceService {
    static final MediaType JSON_MEDIA_TYPE                         = MediaType.parse("application/json; charset=utf-8");
    static final String     REQUEST_BODY_GET_ALL_VERSIONED_PAGES    = "[{\"queryArg\": \"pageType\", \"value\": \"change\"}]";
    static final Integer    GET_CREATED_PAGE_MAX_RETRIES            = 3;
    static final Long       GET_CREATED_PAGE_WAIT_MS                = 3000L;

    final RESTConfluenceServiceImpl delegate;
    final URL           scrollVersionsUrl;
    final String        versionName;
    final ObjectMapper objectMapper = new ObjectMapper();

    public ScrollVersionsConfluenceService( String confluenceUrl,
                                            String versionName,
                                            Credentials credentials,
                                            SSLCertificateInfo sslCertificateInfo)
    {
        if (versionName == null)
            throw new java.lang.IllegalArgumentException("versionName is null!");
        this.versionName = versionName;

        try {
            val regex = "/rest/api(/?)$";
            this.scrollVersionsUrl = new URL(confluenceUrl.replaceAll(regex, "/rest/scroll-versions/1.0"));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid Scroll Versions url", e);
        }

        this.delegate = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslCertificateInfo);

    }

    private HttpUrl.Builder urlBuilder() {

        int port = scrollVersionsUrl.getPort();
        port = (port > -1 ) ? port : scrollVersionsUrl.getDefaultPort();

        return new HttpUrl.Builder().scheme(scrollVersionsUrl.getProtocol())
                .host(scrollVersionsUrl.getHost())
                .port(port)
                .addPathSegments(scrollVersionsUrl.getPath().replaceAll("^/+", ""));
    }


    private Request.Builder requestBuilder() {
        val credentials = getCredentials();
        return new Request.Builder()
                .header("Authorization", okhttp3.Credentials.basic(credentials.username, credentials.password))
                .header("X-Atlassian-Token", "nocheck");
    }

    /**
     *
     * @param spaceKey
     * @return
     */
    CompletableFuture<List<ScrollVersions.Model.Version>> getScrollVersions(String spaceKey) {

        val httpUrl =
                urlBuilder()
                .addPathSegment("versions")
                .addPathSegment(spaceKey)
                .build();
        val request = requestBuilder()
                .url(httpUrl)
                .get()
                .build();

        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<List<ScrollVersions.Model.Version>>();

            val responseBody = ofNullable(response.body());

            if (!responseBody.isPresent()) {
                futureResult.completeExceptionally(new Exception("could not retrieve versions info"));
                return futureResult;
            }

            try {
                val responseBodyString = responseBody.get().string();

                val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.Version[].class);

                futureResult.complete(asList(result));

            } catch (IOException e) {
                futureResult.completeExceptionally( e );
            }

            return futureResult;
        });

    }


    public static void main( String...args ) {
        val ssl = new SSLCertificateInfo();

         val service = new ScrollVersionsConfluenceService(
                "http://192.168.1.145:8090/rest/api",
                "alpha",
                new Credentials( "admin", "admin"),
                ssl);

         val s1 = service.getScrollVersions("TSV")
                 .thenAccept( versions -> {
                     System.out.println( "VERSIONS" );
                     versions.forEach( System.out::println );
                 });

        val s2 = service.delegate.getPage( "TSV", "Test Scroll Versions Home")
                .thenAccept( page -> {
                    page.ifPresent( p -> System.out.printf( "PAGE id=[%s], title=[%s]\n", p.getId(), p.getTitle() ));
                });

        CompletableFuture.allOf( s1, s2 );





    }


    ///
    /// Confluence Service Implementation
    ///

    @Override
    public Credentials getCredentials() {
        return delegate.getCredentials();
    }

    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> findPageByTitle(String parentPageId, String title) {
        return delegate.findPageByTitle(parentPageId, title);
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title) {
        return delegate.createPage(parentPage, title);
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
        return delegate.getPage(pageId);
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
        return delegate.getPage(spaceKey, pageTitle);
    }

    @Override
    public CompletableFuture<List<Model.PageSummary>> getDescendents(String pageId) {
        return delegate.getDescendents(pageId);
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        return delegate.storePage(page, content);
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        return delegate.storePage(page);
    }

    @Override
    public CompletableFuture<Void> addLabelsByName(long id, String[] labels) {
        return delegate.addLabelsByName(id, labels);
    }

    @Override
    public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
        delegate.exportPage(url, spaceKey, pageTitle, exfmt, outputFile);
    }

    @Override
    public Model.Attachment createAttachment() {
        return delegate.createAttachment();
    }

    @Override
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
        return delegate.getAttachment(pageId, name, version);
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        return delegate.addAttachment(page, attachment, source);
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        return delegate.removePage(parentPage, title);
    }

    @Override
    public CompletableFuture<Boolean> removePage(String pageId) {
        return delegate.removePage(pageId);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }


}
