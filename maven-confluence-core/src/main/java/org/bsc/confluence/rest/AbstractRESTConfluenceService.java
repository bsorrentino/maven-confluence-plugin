/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.rest.model.Attachment;

import lombok.val;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author softphone
 */
public abstract class AbstractRESTConfluenceService {

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
     * @param description
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
    
    public Response fromRequest( final Request req, final String description ) {

        try {
            final Response res = client.build().newCall(req).execute();

            if( !res.isSuccessful() ) {
                throw new ServiceException( 
                        format("error: %s\n%s\n%s", description, res.toString(),res.body().string()), res);
            }

            return res;

        } catch (IOException ex) {

            throw new Error(ex);
        }

    }

    protected Stream<Response> fromUrlGET( final HttpUrl url, final String description ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )
                .get()
                .build();

        return Stream.of(fromRequest(req, description));
    }

    protected Stream<Response> fromUrlDELETE( final HttpUrl url, final String description ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )
                .delete()
                .build();

        return Stream.of(fromRequest(req, description));
    }

    protected Stream<Response> fromUrlPOST( final HttpUrl url, RequestBody inputBody, final String description ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )
                .post( inputBody)
                .build();

        return Stream.of(fromRequest(req, description));
    }

    protected Stream<Response> fromUrlPUT( final HttpUrl url, RequestBody inputBody, final String description ) {
        final String credential =
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )
                .put( inputBody)
                .build();

        return Stream.of(fromRequest(req, description));
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

        final ResponseBody body = res.body();

        try (Reader r = body.charStream()) {

            final JsonReader rdr = Json.createReader(r);

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


    protected Optional<JsonObject> findPageById( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        return fromUrlGET( url, "find page" ).flatMap(this::mapToStream).findFirst();
    }

    protected List<JsonObject> findPages( final String spaceKey, final String title ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addQueryParameter("spaceKey", spaceKey)
                                    .addQueryParameter("title", title)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        return fromUrlGET( url, "find pages" ).flatMap(this::mapToStream).collect( Collectors.toList() );

    }

    protected List<JsonObject> descendantPages( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    //.addPathSegments("descendant/page")
                                    .addPathSegments("child/page")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        return  fromUrlGET( url, "get descendant pages" )
                .flatMap(this::mapToStream)
                .flatMap( (JsonObject o) -> {
                    final String childId = o.getString("id");
                    return Stream.concat(Stream.of(o), descendantPages(childId).stream()) ;
                })
                .collect( Collectors.toList() )
                ;

    }


    protected List<JsonObject> childrenPages( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/page")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        return fromUrlGET( url, "get children pages" ).flatMap(this::mapToStream).collect( Collectors.toList() );

    }

    /**
     *
     * @param spaceKey
     * @param title
     * @return
     */
    public Optional<JsonObject> findPage( final String spaceKey, final String title ) {

        return findPages(spaceKey, title).stream().findFirst();
    }

    protected boolean deletePageById( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    //.addQueryParameter("status", "")
                                    .build();

        fromUrlDELETE( url, "delete page" );

        return true;
    }

    /**
     *
     * @param inputData
     * @return
     */
    public final Optional<JsonObject> createPage( final JsonObject inputData ) {
        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody = RequestBody.create(storageFormat,
                inputData.toString());

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .build();

        return fromUrlPOST(url, inputBody, "create page").map(this::mapToObject).findFirst();
    }

    protected Optional<JsonObject> updatePage( final String pageId, final JsonObject inputData ) {

        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody = RequestBody.create(storageFormat,
                inputData.toString());

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .addPathSegment(pageId)
                                .build();

        return fromUrlPUT(url, inputBody, "update page").map(this::mapToObject).findFirst();
    }

    /**
     *
     * @param inputData
     * @return
     */
    protected final void addLabels( String id,  String ...labels ) {


        final JsonArrayBuilder inputBuilder = Json.createArrayBuilder();

        for( String name : labels ) {

            inputBuilder.add(
                    Json.createObjectBuilder()
                        .add("prefix", "global")
                        .add("name", name)
            );

        }

        final JsonArray inputData = inputBuilder.build();

        final MediaType storageFormat = MediaType.parse("application/json");

        final RequestBody inputBody =
                RequestBody.create(storageFormat, inputData.toString());

        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .addPathSegment(id)
                                .addPathSegment("label")
                                .build();

        fromUrlPOST(url, inputBody, "add label");
    }

    protected List<JsonObject> getAttachments( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        return fromUrlGET( url, "get attachments" ).flatMap(this::mapToStream).collect(Collectors.toList());

    }

    protected List<JsonObject> getAttachment( final String id, final String fileName ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("filename", fileName)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();

        return fromUrlGET( url, "get attachment" ).flatMap(this::mapToStream).collect(Collectors.toList());

    }

    protected List<JsonObject> addAttachment( final String id, final Attachment att, final java.io.InputStream data ) {

        final RequestBody fileBody;

        try {
            fileBody = RequestBody.create( MediaType.parse(att.getContentType()), IOUtils.toByteArray(data));
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

        return fromUrlPOST(builder.build(), inputBody, "create attachment")
                .flatMap( post ->
                    (att.getId() != null) ?
                            Stream.of(mapToObject(post)) :
                            mapToStream(post) )
                .collect( Collectors.toList() );

    }
}
