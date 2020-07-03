package org.bsc.confluence.rest.scrollversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import okhttp3.*;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ExportFormat;
import org.bsc.confluence.rest.RESTConfluenceService;
import org.bsc.confluence.rest.scrollversions.model.ScrollVersions;
import org.bsc.ssl.SSLCertificateInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public class ScrollVersionsConfluenceService implements ConfluenceService {


    enum ChangeType {
        ADD_VERSION("Modify"),
        REMOVE_VERSION("Remove");

        String typeName;

        ChangeType( String typeName ) {
            this.typeName = typeName;
        }
    }


    static final MediaType  JSON_MEDIA_TYPE     = MediaType.parse("application/json; charset=utf-8");
    static final String     REQUEST_BODY_FORMAT = "[{\"queryArg\": \"%s\", \"value\": \"%s\"}]";

    final RESTConfluenceService delegate;
    final URL           scrollVersionsUrl;
    final String        versionName;
    final ObjectMapper objectMapper = new ObjectMapper();

    final boolean removeHard = true;

    private Optional<ScrollVersions.Model.Version> currentVersion = Optional.empty();

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

        this.delegate = new RESTConfluenceService(confluenceUrl, credentials, sslCertificateInfo);

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
    CompletableFuture<ScrollVersions.Model.Version> getCurrentVersion(String spaceKey) {

        return currentVersion
                .map( v -> completedFuture(v) )
                .orElseGet( () ->
                    getScrollVersions(spaceKey)
                        .thenCompose( versions ->
                            versions.stream()
                                    .filter(v -> versionName.equals(v.getName()))
                                    .findFirst()
                                    .map(v -> {
                                        currentVersion = Optional.of(v); // cache version
                                        return completedFuture(v);
                                    })
                                    .orElseGet(() ->
                                        completeExceptionally( new Exception(format("version [%s] doesn't exists!", versionName)) )
                                    )
                        ));



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

        //debug( "getScrollVersions( '%s' )",spaceKey );
        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<List<ScrollVersions.Model.Version>>();

            return ofNullable(response.body()).map(b -> {

                try {
                    val responseBodyString = b.string();
                    //trace( "getScrollVersions response\n%s", responseBodyString );
                    val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.Version[].class);

                    futureResult.complete(asList(result));

                } catch (IOException e) {
                    futureResult.completeExceptionally(e);
                }

                return futureResult;

            }).orElseGet(() -> {
                futureResult.completeExceptionally(new Exception("could not retrieve versions info!"));
                return futureResult;
            });

        });
    }

    /**
     *
     * @param spaceKey
     * @return
     */
    CompletableFuture<List<ScrollVersions.Model.Page>> getVersionsPages(String spaceKey, String queryArg, String value) {

        val httpUrl =
                urlBuilder()
                        .addPathSegment("page")
                        .addPathSegment(spaceKey)
                        .build();

        val body = RequestBody.create(format(REQUEST_BODY_FORMAT, queryArg, value), JSON_MEDIA_TYPE );

        val request = requestBuilder()
                .url(httpUrl)
                .post(body)
                .build();

        //debug( "getVersionPage( '%s', '%', '%s')",spaceKey, queryArg, value );
        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<List<ScrollVersions.Model.Page>>();

            return ofNullable(response.body()).map( b -> {
                try {
                    val responseBodyString = b.string();
                    //trace( "getVersionsPages response\n%s", responseBodyString );
                    val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.Page[].class);

                    futureResult.complete( asList(result) );


                } catch (IOException e) {
                    futureResult.completeExceptionally( e );
                }
                return futureResult;
            }).orElseGet( () -> {
                futureResult.completeExceptionally(new Exception("could not retrieve versions info!s"));
                return futureResult;
            });

        });

    }


    /**
     *
     * @param spaceKey
     * @return
     */
    CompletableFuture<Optional<ScrollVersions.Model.PageResult>> getVersionPage(String spaceKey, String title) {

        val httpUrl =
                urlBuilder()
                        .addPathSegment("page")
                        .addPathSegment(spaceKey)
                        .build();

        val body = RequestBody.create(format(REQUEST_BODY_FORMAT, "scrollPageTitle", title), JSON_MEDIA_TYPE );

        val request = requestBuilder()
                .url(httpUrl)
                .post(body)
                .build();

        //debug( "getVersionPage( '%s', '%s')",spaceKey, title );
        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<Optional<ScrollVersions.Model.PageResult>>();

            return ofNullable(response.body()).map( b -> {
                try {
                    val responseBodyString = b.string();
                    //trace( "getVersionPage response\n%s", responseBodyString );
                    val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.Page[].class);

                    if( result == null || result.length == 0 ) {
                        futureResult.complete( Optional.empty() );
                    }
                    else {

                        if( result.length==1 ) {
                            futureResult.complete( Optional.of( ScrollVersions.Model.PageResult.of( result[0], emptyList())) );
                        }
                        else {
                            val masterPage = Arrays.stream(result).filter( p -> p.isMasterPage() ).findFirst();

                            futureResult.complete(
                                    masterPage.map( mp -> {
                                        val versionPages = Arrays.stream(result).filter( p -> !p.isMasterPage() ).collect(toList());
                                        return Optional.of( ScrollVersions.Model.PageResult.of( mp, versionPages ) );
                                    }).orElse( Optional.empty()));
                        }
                    }


                } catch (IOException e) {
                    futureResult.completeExceptionally( e );
                }
                return futureResult;
            }).orElseGet( () -> {
                futureResult.completeExceptionally(new Exception("could not retrieve versions info!s"));
                return futureResult;
            });

        });

    }

    /**
     *
     * @param spaceKey
     * @param title
     * @param byVersion
     * @return
     */
    CompletableFuture<Optional<ScrollVersions.Model.PageResult>> getVersionPage(String spaceKey, String title, ScrollVersions.Model.Version byVersion ) {

        return getVersionPage( spaceKey, title )
                    .thenApply( result ->
                        result.map( r ->
                            r.getVersionPages().stream()
                                .filter( p -> byVersion.getId().equals(p.getTargetVersion().getVersionId()) )
                                .findFirst()
                                .map( pp -> Optional.of(ScrollVersions.Model.PageResult.of( r.getMasterPage(), singletonList(pp) )) )
                                //.orElseGet( () -> Optional.of(ScrollVersions.Model.PageResult.of( r.getMasterPage(), emptyList() )) )
                                .orElse( Optional.empty() )
                        ).orElse( result )
                    );
    }

    /**
     *
     * @param spaceKey
     * @param masterPageId
     * @param title
     * @param version
     * @return
     */
    CompletableFuture<ScrollVersions.Model.NewPageResult> createVersionPage(String spaceKey, Model.ID masterPageId, String title, ScrollVersions.Model.Version version) {

        val httpUrl =
                urlBuilder()
                        .addPathSegment("page")
                        .addPathSegment("new")
                        .addPathSegment(spaceKey)
                        .build();

        val body = new FormBody.Builder()
                .add("parentConfluenceId", masterPageId.toString() )
                .add( "versionId", version.getId() )
                .add( "pageTitle", title )
                .build();

        val request = requestBuilder()
                .url(httpUrl)
                .post(body)
                .build();

        //debug( "createVersionPage( '%s', '%d', '%s', '%s')",spaceKey, masterPageId, title, version.getName() );
        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<ScrollVersions.Model.NewPageResult>();

            return ofNullable(response.body()).map( b -> {
                try {
                    val responseBodyString = b.string();
                    //debug( "createVersionPage response\n%s", responseBodyString );
                    val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.NewPageResult.class);

                    futureResult.complete( result );

                } catch (IOException e) {
                    futureResult.completeExceptionally( e );
                }
                return futureResult;
            }).orElseGet( () -> {
                futureResult.completeExceptionally(new Exception( format("could not create new page [%s] in version [%s]!", title, version.getName() )));
                return futureResult;
            });

        });

    }

    /**
     *
     * @param masterPageId
     * @param title
     * @param version
     * @param changeType
     * @return
     */
    CompletableFuture<ScrollVersions.Model.NewPageResult> manageVersionPage(Model.ID masterPageId, String title, ScrollVersions.Model.Version version, ChangeType changeType ) {

        val httpUrl =
                urlBuilder()
                        .addPathSegment("page")
                        .addPathSegment("modify")
                        .build();

        val body = new FormBody.Builder()
                .add("masterPageId", masterPageId.toString() )
                .add( "pageTitle", title)
                .add( "versionId", version.getId() )
                .add( "changeType", changeType.typeName )
                .build();

        val request = requestBuilder()
                .url(httpUrl)
                .post(body)
                .build();

        //debug( "manageVersionPage( '%s', '%s', '%s', '%s')",masterPageId, title, version.getName(), changeType.typeName );
        return delegate.fromRequestAsync(request).thenCompose( response -> {

            val futureResult = new CompletableFuture<ScrollVersions.Model.NewPageResult>();

            return ofNullable(response.body()).map( b -> {
                try {
                    val responseBodyString = b.string();
                    //trace( "manageVersionPage response\n%s", responseBodyString );

                    val result = objectMapper.readValue(responseBodyString, ScrollVersions.Model.NewPageResult.class);

                    futureResult.complete( result );

                } catch (IOException e) {
                    futureResult.completeExceptionally( e );
                }
                return futureResult;
            }).orElseGet( () -> {
                futureResult.completeExceptionally(new Exception( format("could not create new page [%s] in version [%s]!", title, version.getName() )));
                return futureResult;
            });

        });

    }

    private void debug(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }
    private void trace(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }

    private CompletableFuture<ScrollVersions.Model.Result> toResult(Model.Page page ) {
        //debug( "toResult( %s ) title=[%s]", page.getClass().getName(), page.getTitle());
        if( page instanceof ScrollVersions.Model.Result) {
            return completedFuture((ScrollVersions.Model.Result)page);
        }

        throw new IllegalArgumentException(format("page [%s] is not a result type! %s", page.getTitle(), page,getClass().getName() ));

//        final Pattern versionsTitlePattern = Pattern.compile( "^[\\.](.+)\\sv(.+)$" );
//        val m =  versionsTitlePattern.matcher(page.getTitle());
//        if( !m.matches() ) {
//            throw new IllegalArgumentException("title doesn't match");
//        }
//        return getVersionPage(page.getSpace(),m.group(1)).thenApply( p -> p.get() );

    }


    /**
     *
     * @param ex
     * @param <U>
     * @return
     */
    static <U> CompletableFuture<U> completeExceptionally( Throwable ex ) {
        val future = new CompletableFuture<U>();
        future.completeExceptionally( ex );
        return future;
    }

    private <S,T> CompletableFuture<T> cast( CompletableFuture<S> s ) {
        return (CompletableFuture<T>)s;
    }

    private <S,T> Optional<T> cast( Optional<S> s ) {
        return (Optional<T>)s;
    }

    private static Pattern vesrsionsTitlePattern = Pattern.compile( "^[\\.](.+)\\sv(.+)$");

    private String decodeTitle( String title ) {
        val m = vesrsionsTitlePattern.matcher(title);

        val result =  (m.matches())
                ? m.group(1)
                : title;
        //debug( "decodeTitle('%s')='%s'", title,  result );

        return result;
    }

    private boolean isVersion( String title, ScrollVersions.Model.Version version ) {
        val m = vesrsionsTitlePattern.matcher(title);

        return ( m.matches() )
                ? m.group(2).equals( version.getName() )
                : false;
    }

    ///
    /// Confluence Service Implementation
    ///

    @Override
    public Credentials getCredentials() {
        return delegate.getCredentials();
    }

    @Override
    public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title, Storage content ) {
        //debug( "createPage(): parent.id=[%s] title=[%s]", parentPage.getId(), title );

        return getCurrentVersion( parentPage.getSpace() )
                .thenCombine( delegate.getPage( parentPage.getSpace(), title ), (version, page) ->
                    page.map( p -> manageVersionPage( p.getId(), title, version, ChangeType.ADD_VERSION) )
                            .orElseGet( () ->
                                    toResult( parentPage )
                                            .thenCompose( p -> createVersionPage( parentPage.getSpace(), p.getMasterPageId(), title, version))
                            )
                )
                .thenCompose( future -> cast(future) )
                ;
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
        //debug( "getPage: space=[%s] title=[%s]", spaceKey, pageTitle );

        return getCurrentVersion(spaceKey)
                .thenCompose( version -> getVersionPage( spaceKey, pageTitle, version) )
                .thenCompose( vpage ->
                                ( !vpage.isPresent() || vpage.get().getVersionPages().isEmpty() )
                                    ? completedFuture(Optional.empty())
                                    : completedFuture( cast(vpage) )
                            );

    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content) {
        //debug( "storePage(page,content): page.id=[%s] page.version=[%d]", page.getId(), page.getVersion() );
        //trace( "storePage(page,content):\n%s", page );

        return delegate.getPage( page.getId() )
                .thenCompose( confluencePage ->
                        confluencePage.map( p -> delegate.storePage(p, content) )
                                .orElseGet( () ->
                                        completeExceptionally(new Exception(format("page [%s] not found!", page.getTitle()))))
                .thenApply( p -> page ))
                ;
    }

    @Override
    public CompletableFuture<Model.Page> storePage(Model.Page page) {
        //debug( "storePage(page): page.id=[%s] page.version=[%d]", page.getId(), page.getVersion() );
        //trace( "storePage(page):\n%s", page );

        return delegate.getPage( page.getId() )
                .thenCompose( confluencePage ->
                        confluencePage.map(delegate::storePage)
                                .orElseGet( () ->
                                        completeExceptionally(new Exception(format("page [%s] not found!", page.getTitle())))) );
    }


    @Override
    public CompletableFuture<List<Model.PageSummary>> getDescendents(Model.ID pageId) {
        //debug( "getDescendents( pageId:[%s] )", pageId );
        return getPage(pageId)
                .thenCompose(page ->
                        cast(page.map( pp -> toResult(pp)
                                    .thenCompose( result -> delegate.getDescendents( result.getMasterPageId()))
                                    .thenApply( result -> {
                                        val list = result.stream()
                                                .filter( p -> isVersion(p.getTitle(), currentVersion.get()))
                                                .filter( p -> pageId.compareTo(p.getId())!=0)
                                                .collect( toList() );
                                        //debug( "Descendents#: %d", list.size());
                                        return list;
                                    })
                                    ).orElseGet( () -> completedFuture(emptyList())) ))
                ;
    }

    @Override
    public CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(Model.ID parentPageId, String title) {
        //debug( "findPageByTitle( parentPageId:[%s], title:[%s])", parentPageId, title );

        return delegate.getPage( parentPageId )
                .thenCompose( parentPage ->
                    cast(parentPage
                            .map( pp -> this.getPage( pp.getSpace(), title ) )
                            .orElseGet( () -> completedFuture(Optional.empty()) )))
                ;
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
        //debug( "removePage( parent.title:[%s], title:[%s])", parentPage.getTitle(), title );

        val getPage = getPage( parentPage.getSpace(), title );

        if( removeHard ) {
            return getPage
                    .thenCompose( page ->
                            cast(page.map( pp -> delegate.removePage(pp.getId()) )
                                     .orElseGet( () -> completeExceptionally( new Exception( format( "page [%s] not found!",title )) ))))
                    .thenApply( result -> true )
                    ;
        }
        else {
            return getPage
                    .thenCompose( page ->
                            cast(page
                                    .map( pp ->
                                            getCurrentVersion(pp.getSpace())
                                                    .thenCompose( version -> toResult(pp)
                                                            .thenCompose( result ->
                                                                    manageVersionPage( result.getMasterPageId(), pp.getTitle(), version, ChangeType.REMOVE_VERSION ) )))
                                    .orElseGet( () -> completeExceptionally( new Exception( format( "page [%s] not found!",title )) ))))
                    .thenApply( result -> true )
                    ;

        }
    }

    @Override
    public CompletableFuture<Boolean> removePage(Model.ID pageId) {
        //debug( "removePage( pageId:[%s])", pageId );

        val getPage = getPage(pageId);

        if( removeHard ) {
            return getPage
                    .thenCompose(page ->
                            cast(page
                                    .map(pp -> delegate.removePage(pageId) )
                                    .orElseGet(() -> completeExceptionally(new Exception(format("page [%s] not found!", pageId))))))
                    .thenApply(result -> true )
                    ;

        }
        else {
            return getPage
                    .thenCompose(page ->
                            cast(page
                                    .map(pp ->
                                            getCurrentVersion(pp.getSpace())
                                                    .thenCompose(version -> toResult(pp)
                                                            .thenCompose(result ->
                                                                    manageVersionPage(result.getMasterPageId(), pp.getTitle(), version, ChangeType.REMOVE_VERSION))))
                                    .orElseGet(() -> completeExceptionally(new Exception(format("page [%s] not found!", pageId))))))
                    .thenApply(result -> true)
                    ;
        }
    }

    @Override
    public CompletableFuture<Optional<Model.Page>> getPage(Model.ID pageId) {
        //debug( "getPage( pageId:[%s]", pageId );

        return delegate.getPage( pageId )
                .thenCompose( page ->
                        page.map( pp -> getCurrentVersion( pp.getSpace() )
                                .thenCompose( version ->
                                            ( isVersion(pp.getTitle(), version) )
                                                ? this.getPage( pp.getSpace(), decodeTitle(pp.getTitle()) )
                                                : completedFuture(Optional.empty())
                                            )
                                ).orElse( completedFuture(Optional.<Model.Page>empty()))
                            )
            ;
    }

    @Override
    public CompletableFuture<Void> addLabelsByName(Model.ID id, String[] labels) {
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
    public CompletableFuture<Optional<Model.Attachment>> getAttachment(Model.ID pageId, String name, String version) {
        return delegate.getAttachment(pageId, name, version);
    }

    @Override
    public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source) {
        return delegate.addAttachment(page, attachment, source);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // BLOG POST
    ///////////////////////////////////////////////////////////////////////////////

    @Override
    public Model.Blogpost createBlogpost( String space, String title, Storage content, int version) {
        return delegate.createBlogpost(space, title, content, version);
    }

    @Override
    public CompletableFuture<Model.Blogpost> addBlogpost(Model.Blogpost blogpost)  {
        return delegate.addBlogpost(blogpost);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }


}
