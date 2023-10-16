/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceUtils;
import org.bsc.confluence.rest.model.Attachment;
import org.bsc.confluence.rest.model.IdHelper;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

/**
 * AbstractRESTConfluenceService is an abstract class that implements the IdHelper interface.
 * It provides common functionality for making REST API calls to Confluence
 * Sub-classes need to implement getCredentials() and urlBuilder() to provide
 * the necessary information for making API calls.
*/
public abstract class AbstractRESTConfluenceService implements IdHelper {

    private static final String EXPAND = "space,version,container";

    protected HttpClient client;

    public abstract ConfluenceService.Credentials getCredentials();

    protected abstract URI urlBuilder() throws URISyntaxException;

    @SuppressWarnings("serial")
	public static class ServiceException extends Error {
        public final HttpResponse<String> res;

        public ServiceException(String message, HttpResponse<String> res ) {
            super(message);
            this.res = res;
        }

    }

    /**
     *
     * @param reqBuilder
     * @return
     */
    public CompletableFuture<HttpResponse<String>> fromRequestAsync(final HttpRequest.Builder reqBuilder) {

        final var req = reqBuilder.build();
        return client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply( (res) -> {
                    int statusCode = res.statusCode();
                    if (statusCode >= 300 ) {
                        throw new ServiceException(
                                format("error: %s\n%s\n%s", req.uri(), res,res.body()), res);
                    }
                    return res;
                });
    }
    
    protected void fromRequest(final HttpRequest.Builder reqBuilder, final String description, Consumer<HttpResponse<String>> consumer) {

        try{

            final HttpResponse<String> res = client.send( reqBuilder.build() , HttpResponse.BodyHandlers.ofString());

            int statusCode = res.statusCode();
            if (statusCode >= 300 ) {
                throw new ServiceException(
                        format("error: %s\n%s\n%s", description, res,res.body()), res);
            }
            consumer.accept(res);

        } catch (Exception ex) {
            throw new Error(ex);
        }

    }

    protected Stream<JsonObject> mapToStream( HttpResponse<String> res)  {

        final var body = res.body();

        try( final Reader r = new StringReader(body);
             final JsonReader rdr = Json.createReader(r) )
        {

            final JsonObject root = rdr.readObject();

            final Stream.Builder<JsonObject> stream = Stream.builder();
            
            // Check for Array
            if( root.containsKey("results") ) {
                final JsonArray results = root.getJsonArray("results");
                
                if (results != null ) {
                    for( int ii = 0 ; ii < results.size() ; ++ii )
                        stream.add(results.getJsonObject(ii));
                }
            }
            else {
                stream.add( root );
            }

            return stream.build();

        } catch (IOException | JsonParsingException e ) {
            throw new Error(e);
        }

    }

    protected JsonObject mapToObject(HttpResponse<String> res ) {
        final var body = res.body();

        try (Reader r = new StringReader(body)) {

            final JsonReader rdr = Json.createReader(r);

            return rdr.readObject();

        } catch (IOException ex) {

            throw new Error(ex);
        }
    };

    public static URI buildUrl( URI url, List<String> path, Map<String, String> query) throws URISyntaxException {

        final var previousPath = url.getPath();
        var newPath = previousPath;
        if( !path.isEmpty() ) {
            newPath = String.join("/", path);

            if (previousPath != null) {
                newPath = format("%s/%s", previousPath, newPath);
            }
        }

        final var previousQuery = url.getQuery();
        var newQuery = previousQuery;
        if( !query.isEmpty() ) {
            newQuery = query.entrySet().stream()
                    .map(kv -> format("%s=%s", kv.getKey(), kv.getValue()))
                    .collect(Collectors.joining("&"));

            if (previousQuery != null) {
                newQuery = format("%s&%s", previousQuery, newQuery);
            }
        }

        return new URI(
                url.getScheme(),
                url.getUserInfo(),
                url.getHost(),
                url.getPort(),
                newPath, // path
                newQuery, // query
                url.getFragment()
        );

    }

    private URI buildUrl( List<String> path, Map<String, String> query) {

        try {
            return buildUrl( urlBuilder(), path, query );
        }
        catch( URISyntaxException ex ) {
            throw new Error(ex);
        }
    }

    protected CompletableFuture<Optional<JsonObject>> findPageById( final String id ) {

        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();
        final var url = buildUrl( List.of( "content", id ), Map.of( "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req, "find page", res ->
                result.complete(Stream.of(res).flatMap(this::mapToStream).findFirst()) );

         return result;

    }

    protected CompletableFuture<List<JsonObject>> findPages( final String spaceKey, final String title ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content" ),
                Map.of( "spaceKey", spaceKey, "title", title, "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req,"find pages", res ->
                result.complete( Stream.of(res).flatMap(this::mapToStream).collect( Collectors.toList())));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> descendantPages( final long id ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content", String.valueOf(id), "child", "page" ),
                Map.of( "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req, "get descendant pages", res ->
                result.complete(
                        Stream.of(res).flatMap(this::mapToStream)
                                .flatMap( o -> {
                                    final long childId = IdHelper.getId( o );
                                    return Stream.concat(Stream.of(o), descendantPages(childId).join().stream()) ;
                                })
                                .collect( Collectors.toList() )));
        return result ;


    }


    protected CompletableFuture<List<JsonObject>> childrenPages( final String id ) {
        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content", id, "child", "page" ),
                Map.of( "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req, "get children pages", res ->
                result.complete(Stream.of(res).flatMap(this::mapToStream).collect( Collectors.toList() )));

        return result;
    }

    /**
     *
     * @param spaceKey
     * @param title
     * @return
     */
    public CompletableFuture<Optional<JsonObject>> findPage( final String spaceKey, final String title ) {

        return findPages(spaceKey, title).thenApply( list -> list.stream().findFirst() );
    }

    protected CompletableFuture<Boolean> deletePageById( final String id ) {

        final CompletableFuture<Boolean> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content",id ),
                emptyMap());

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                ;

        fromRequest(req, "delete page", res -> result.complete(true) );

        return result;
    }

    /**
     *
     * @param inputData
     * @return
     */
    public final CompletableFuture<Optional<JsonObject>> createPage( final JsonObject inputData ) {
        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();

        final var inputBody = HttpRequest.BodyPublishers.ofString(inputData.toString(), StandardCharsets.UTF_8);

        final var url = buildUrl(
                List.of( "content" ),
                emptyMap());

        final var req = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(url)
                .POST( inputBody )
                ;

        fromRequest(req, "create page", res ->
                result.complete(Stream.of(res).map(this::mapToObject).findFirst()));

        return result;
    }

    protected CompletableFuture<Optional<JsonObject>> updatePage( final String pageId, final JsonObject inputData ) {

        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();

        final var inputBody = HttpRequest.BodyPublishers.ofString(inputData.toString(), StandardCharsets.UTF_8);

        final var url = buildUrl(
                List.of( "content", pageId ),
                emptyMap());

        final var req = HttpRequest.newBuilder()
                .header("X-Atlassian-Token","nocheck")
                .header("Content-Type", "application/json")
                .uri( url )
                .PUT( inputBody)
                ;

        fromRequest(req, "update page", res ->
                result.complete(Stream.of(res).map(this::mapToObject).findFirst()));

        return result;
    }

    /**
     *
     * @param id
     * @param labels
     */
    protected final CompletableFuture<Void> addLabels( String id,  String ...labels ) {

        final CompletableFuture<Void> result = new CompletableFuture<>();

        final JsonArrayBuilder inputBuilder = Json.createArrayBuilder();

        for( String name : labels ) {

            inputBuilder.add(
                    Json.createObjectBuilder()
                        .add("prefix", "global")
                        .add("name", ConfluenceUtils.sanitizeLabel(name))
            );

        }

        final JsonArray inputData = inputBuilder.build();

        final var inputBody = HttpRequest.BodyPublishers.ofString(inputData.toString(), StandardCharsets.UTF_8);

        final var url = buildUrl(
                List.of( "content", id, "label" ),
                emptyMap());

        final var req = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(url)
                .POST( inputBody )
                ;

        fromRequest(req, "add label", res -> result.complete(null));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> getAttachments( final String id ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content", id, "child", "attachment" ),
                Map.of( "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req, "get attachments", res ->
                result.complete( Stream.of(res).flatMap(this::mapToStream).collect(Collectors.toList())));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> getAttachment( final String id, final String fileName ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final var url = buildUrl(
                List.of( "content", id, "child", "attachment" ),
                Map.of( "filename", fileName, "expand", EXPAND ));

        final var req = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                ;

        fromRequest(req, "get attachment", res ->
                result.complete(Stream.of(res).flatMap(this::mapToStream).collect(Collectors.toList())));

        return result;
    }


    protected CompletableFuture<List<JsonObject>> addAttachment( final String id, final Attachment att, final java.io.InputStream data ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        var bodyPublisher = new MultipartFormDataBodyPublisher()
                    .add( "comment", att.getComment()  )
                    .add( "minorEdit", "true")
                    .addStream(  "file", att.getFileName(), () -> data, att.getContentType() )
                    ;

        final var path = ( att.getId() != null ) ?
                List.of( "content", id, "child", "attachment", att.getId(), "data" ) :
                List.of(  "content", id, "child", "attachment");

        final var url = buildUrl(
                path,
                emptyMap());

        final var req = HttpRequest.newBuilder()
                .header("X-Atlassian-Token","nocheck")
                .headers("Content-Type", bodyPublisher.contentType())
                .uri(url)
                .POST( bodyPublisher )
                ;

        fromRequest(req, "create page", res ->
                result.complete( Stream.of(res).flatMap( post ->
                                (att.getId() != null)
                                        ? Stream.of(mapToObject(post))
                                        : mapToStream(post))
                        .collect( Collectors.toList() )));

        return result;

    }


}
