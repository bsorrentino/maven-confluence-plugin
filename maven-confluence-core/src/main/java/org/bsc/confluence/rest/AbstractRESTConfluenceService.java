/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import lombok.val;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceUtils;
import org.bsc.confluence.rest.model.Attachment;
import org.bsc.confluence.rest.model.IdHelper;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 *
 * @author softphone
 */
public abstract class AbstractRESTConfluenceService implements IdHelper {

    private static final String EXPAND = "space,version,container";

    protected final OkHttpClient.Builder client = new OkHttpClient.Builder();

    public abstract ConfluenceService.Credentials getCredentials();

    protected abstract HttpUrl.Builder urlBuilder();

    @SuppressWarnings("serial")
	public static class ServiceException extends Error {
        public final Response res;

        public ServiceException(String message, Response res ) {
            super(message);
            this.res = res;
        }

    }

    /**
     *
     * @param req
     * @return
     */
    public CompletableFuture<Response> fromRequestAsync( final Request req ) {

        val result = new CompletableFuture<Response>();
        
        client.build().newCall(req).enqueue( new Callback() {
                
            @Override
            public void onResponse(Call call, Response res) throws IOException {
                if( !res.isSuccessful() ) {
                    
                    result.completeExceptionally( 
                            new ServiceException( 
                                    format("error: %s\n%s", 
                                         res.toString(),
                                        res.body().string()), 
                                        res));
                    return;
                }
                
                result.complete(res);
                
            }
            
            @Override
            public void onFailure(Call call, IOException e) {          
                result.completeExceptionally( e );
            }
            
        });

        return result;

    }
    
    public void fromRequest(final Request req, final String description, Consumer<Response> consumer) {

        try(final Response res = client.build().newCall(req).execute()) {

            if( !res.isSuccessful() ) {
                throw new ServiceException( 
                        format("error: %s\n%s\n%s", description, res.toString(),res.body().string()), res);
            }
            consumer.accept(res);

        } catch (IOException ex) {
            throw new Error(ex);
        }

    }

    protected void fromUrlGET( final HttpUrl url, final String description, Consumer<Response> consumer ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )
                .get()
                .build();

        fromRequest(req, description, consumer);
    }

    protected void fromUrlDELETE( final HttpUrl url, final String description, Consumer<Response> consumer ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )
                .delete()
                .build();

        fromRequest(req, description, consumer);
    }

    protected void fromUrlPOST( final HttpUrl url, RequestBody inputBody, final String description, Consumer<Response> consumer  ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )
                .post( inputBody)
                .build();

        fromRequest(req, description, consumer);
    }

    protected void fromUrlPUT( final HttpUrl url, RequestBody inputBody, final String description, Consumer<Response> consumer  ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )
                .put( inputBody)
                .build();

        fromRequest(req, description, consumer);
    }

    protected void debugBody( Response res ) {

    		final ResponseBody body = res.body();

        try {
			System.out.printf( "BODY\n%s\n", new String(body.bytes()) );
		} catch (IOException e) {
			System.out.printf( "READ BODY EXCEPTION\n%s\n", e.getMessage() );
		}

    }
    
    protected Stream<JsonObject> mapToStream( Response res)  {

        try (final ResponseBody body = res.body();
             final Reader r = body.charStream();
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

    protected JsonObject mapToObject(Response res ) {
        final ResponseBody body = res.body();

        try (Reader r = body.charStream()) {

            final JsonReader rdr = Json.createReader(r);

            final JsonObject root = rdr.readObject();

            return root;

        } catch (IOException ex) {

            throw new Error(ex);
        }
    };


    protected CompletableFuture<Optional<JsonObject>> findPageById( final String id ) {

        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();
        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        fromUrlGET( url, "find page", res ->
                result.complete(Stream.of(res).flatMap(this::mapToStream).findFirst())
        );
        return result;

    }

    protected CompletableFuture<List<JsonObject>> findPages( final String spaceKey, final String title ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();
        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addQueryParameter("spaceKey", spaceKey)
                                    .addQueryParameter("title", title)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        fromUrlGET( url, "find pages", res ->
                result.complete( Stream.of(res).flatMap(this::mapToStream).collect( Collectors.toList())));
        return result;
    }

    protected CompletableFuture<List<JsonObject>> descendantPages( final long id ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(String.valueOf(id))
                                    //.addPathSegments("descendant/page")
                                    .addPathSegments("child/page")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        fromUrlGET( url, "get descendant pages", res ->
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
        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/page")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        fromUrlGET( url, "get children pages", res ->
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
        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    //.addQueryParameter("status", "")
                                    .build();
        fromUrlDELETE( url, "delete page", res -> result.complete(true) );

        return result;
    }

    /**
     *
     * @param inputData
     * @return
     */
    public final CompletableFuture<Optional<JsonObject>> createPage( final JsonObject inputData ) {
        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();

        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody = RequestBody.create(inputData.toString(), storageFormat);

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .build();

        fromUrlPOST(url, inputBody, "create page", res ->
                result.complete(Stream.of(res).map(this::mapToObject).findFirst()));

        return result;
    }

    protected CompletableFuture<Optional<JsonObject>> updatePage( final String pageId, final JsonObject inputData ) {

        final CompletableFuture<Optional<JsonObject>> result = new CompletableFuture<>();

        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody = RequestBody.create(inputData.toString(), storageFormat);

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .addPathSegment(pageId)
                                .build();

        fromUrlPUT(url, inputBody, "update page", res ->
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

        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody =
                RequestBody.create(inputData.toString(), storageFormat);

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .addPathSegment(id)
                                .addPathSegment("label")
                                .build();

        fromUrlPOST(url, inputBody, "add label", res -> result.complete(null));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> getAttachments( final String id ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        fromUrlGET( url, "get attachments", res ->
                result.complete( Stream.of(res).flatMap(this::mapToStream).collect(Collectors.toList())));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> getAttachment( final String id, final String fileName ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("filename", fileName)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        fromUrlGET( url, "get attachment", res ->
                result.complete(Stream.of(res).flatMap(this::mapToStream).collect(Collectors.toList())));

        return result;
    }

    protected CompletableFuture<List<JsonObject>> addAttachment( final String id, final Attachment att, final java.io.InputStream data ) {

        final CompletableFuture<List<JsonObject>> result = new CompletableFuture<>();

        final RequestBody fileBody;

        try {
            fileBody = RequestBody.create( IOUtils.toByteArray(data), MediaType.parse(att.getContentType()) );
        } catch (IOException ex) {
            throw new Error( ex );
        }

        final RequestBody inputBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("comment", att.getComment())
                .addFormDataPart("minorEdit", "true")
                .addFormDataPart("file", att.getFileName(), fileBody)
                .build();

        final HttpUrl.Builder builder = urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment");
        if( att.getId() != null ) {
            builder.addPathSegment( att.getId() )
                    .addPathSegment("data");

        }

        fromUrlPOST(builder.build(), inputBody, "create attachment", res ->
                result.complete( Stream.of(res).flatMap( post ->
                        (att.getId() != null)
                            ? Stream.of(mapToObject(post))
                            : mapToStream(post))
                        .collect( Collectors.toList() )));

        return result;

    }


}
