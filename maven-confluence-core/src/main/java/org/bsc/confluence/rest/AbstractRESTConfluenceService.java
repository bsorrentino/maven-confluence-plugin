/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Reader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.rest.model.Attachment;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 * @author softphone
 */
public abstract class AbstractRESTConfluenceService {
    
    private static final String EXPAND = "space,version,container";
     
    final OkHttpClient.Builder client = new OkHttpClient.Builder();
    
    public abstract ConfluenceService.Credentials getCredentials();
    
    protected abstract HttpUrl.Builder urlBuilder();

    @SuppressWarnings("serial")
	public static class ServiceException extends Exception {
        public final Response res;

        public ServiceException(String message, Response res ) {
            super(message);
            this.res = res;
        }
        
    }
    protected Observable<Response> fromRequest( final Request req, final String description ) {
        
         return rx.Observable.create((t) -> {
                try {
                    final Response res = client.build().newCall(req).execute();

                    if( !res.isSuccessful() ) {
                        t.onError(new ServiceException( format("error: %s\n%s", description, res.toString()), res));
                        return;
                    }

                    t.onNext(res);
                    t.onCompleted();                        
                                        
                } catch (IOException ex) {
                    
                    t.onError(ex);
                }                
        });
       
    }
    
    protected Observable<Response> fromUrlGET( final HttpUrl url, final String description ) {
        final String credential = 
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )  
                .get()
                .build();
        
        return fromRequest(req, description)
        				//.doOnNext( this::debugBody )
;
    }
    
    protected Observable<Response> fromUrlDELETE( final HttpUrl url, final String description ) {
        final String credential = 
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )  
                .delete()
                .build();
        
        return fromRequest(req, description);
    }
    
    protected Observable<Response> fromUrlPOST( final HttpUrl url, RequestBody inputBody, final String description ) {
        final String credential = 
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )  
                .post( inputBody)
                .build();
        
        return fromRequest(req, description);
    }
    
    protected Observable<Response> fromUrlPUT( final HttpUrl url, RequestBody inputBody, final String description ) {
        final String credential = 
                okhttp3.Credentials.basic(getCredentials().username, getCredentials().password);

        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .header("X-Atlassian-Token","nocheck")
                .url( url )  
                .put( inputBody)
                .build();
        
        return fromRequest(req, description);
    }
    
    protected void debugBody( Response res ) {

    		final ResponseBody body = res.body();
      
        try {
			System.out.printf( "BODY\n%s\n", new String(body.bytes()) );
		} catch (IOException e) {
			System.out.printf( "READ BODY EXCEPTION\n%s\n", e.getMessage() );		
		}
        
    }
    protected Observable<JsonObject> mapToArray( Response res)  {

        final ResponseBody body = res.body();
        
        try (Reader r = body.charStream()) {

            final JsonReader rdr = Json.createReader(r);
            
            final JsonObject root = rdr.readObject();

            if( !root.containsKey("results") ) {
            		return Observable.error( new Exception(root.toString()) );
            }
            
            // {"statusCode":404,"data":{"authorized":false,"valid":true,"errors":[],"successful":false,"notSuccessful":true},"message":"No space with key : TEST"}
            final JsonArray results = root.getJsonArray("results");

            //System.out.println( root.toString() );
            if (results == null || results.isEmpty()) {
                return Observable.empty();
            }

            JsonObject array[] = new JsonObject[ results.size() ];
            for( int ii = 0 ; ii < results.size() ; ++ii )
                array[ii] = results.getJsonObject(ii);

            return Observable.from( array );

        } catch (IOException | JsonParsingException e ) {
        		return Observable.error(e);
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
 

    protected Observable<JsonObject> rxfindPageById( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        
        return fromUrlGET( url, "find page" )
                .map( this::mapToObject );
    }

    protected Observable<JsonObject> rxfindPages( final String spaceKey, final String title ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addQueryParameter("spaceKey", spaceKey)
                                    .addQueryParameter("title", title)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        return fromUrlGET( url, "find pages" )
                .flatMap( this::mapToArray );
        
    }
    
    protected Observable<JsonObject> rxDescendantPages( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    //.addPathSegments("descendant/page")
                                    .addPathSegments("child/page")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        return fromUrlGET( url, "get descendant pages" )
        			//.doOnNext( this::debugBody )
                .flatMap( this::mapToArray )
                .concatMap( (o) -> {
                		final String childId = o.getString("id");
                		return Observable.concat( Observable.just(o), rxDescendantPages(childId) );
                })
                
                ;
        
    }
    

    protected Observable<JsonObject> rxChildrenPages( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addPathSegments("child/page")                
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        return fromUrlGET( url, "get children pages" )
                .flatMap( this::mapToArray );
        
    }

    /**
     * 
     * @param spaceKey
     * @param title
     * @return 
     */
    public Observable<JsonObject> rxfindPage( final String spaceKey, final String title ) {
        
        return rxfindPages(spaceKey, title).take(1);
    }
    
    protected Observable<Response> rxDeletePageById( final String id ) {
        
        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    //.addQueryParameter("status", "")
                                    .build();
        
        return fromUrlDELETE( url, "delete page" );
    }
    
    /**
     * 
     * @param inputData
     * @return 
     */
    public final Observable<JsonObject> rxCreatePage( final JsonObject inputData ) {
        final MediaType storageFormat = MediaType.parse("application/json");
        
        final RequestBody inputBody = RequestBody.create(storageFormat, 
                inputData.toString());
        
        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .build();
 
        return fromUrlPOST(url, inputBody, "create page").map( this::mapToObject );
    }
    
    protected Observable<JsonObject> rxUpdatePage( final String pageId, final JsonObject inputData ) {

        final MediaType storageFormat = MediaType.parse("application/json");
        
        final RequestBody inputBody = RequestBody.create(storageFormat, 
                inputData.toString());
        
        final HttpUrl url =  urlBuilder()
                                .addPathSegment("content")
                                .addPathSegment(pageId)
                                .build();

        return fromUrlPUT(url, inputBody, "update page").map( this::mapToObject );
    }

    /**
     * 
     * @param inputData
     * @return 
     */
    protected final Observable<Response> rxAddLabels( String id,  String ...labels ) {
        
        
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
 
        return fromUrlPOST(url, inputBody, "add label");
    }

    protected Observable<JsonObject> rxAttachments( final String id ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        
        return fromUrlGET( url, "get attachments" )
                .flatMap( this::mapToArray );
        
    }
    
    protected Observable<JsonObject> rxAttachment( final String id, final String fileName ) {

        final HttpUrl url =  urlBuilder()
                                    .addPathSegment("content")                
                                    .addPathSegment(id)
                                    .addPathSegments("child/attachment")
                                    .addQueryParameter("filename", fileName)
                                    .addQueryParameter("expand", EXPAND)
                                    .build();
        
        return fromUrlGET( url, "get attachment" )
                .flatMap( this::mapToArray )
                ;
        
    }
    
    protected Observable<JsonObject> rxAddAttachment( final String id, final Attachment att, final java.io.InputStream data ) {

        final RequestBody fileBody;
        
        try {
            fileBody = RequestBody.create( MediaType.parse(att.getContentType()), IOUtils.toByteArray(data));
        } catch (IOException ex) {
            return Observable.error( ex );
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
 
        final Observable<Response> post =  fromUrlPOST(builder.build(), inputBody, "create attachment")
        		/*
                .onErrorResumeNext((ex) -> {
                    if( ex instanceof ServiceException ) {
                        final ServiceException rex = (ServiceException)ex;
                      
                    }                    
                    return Observable.error(ex);
                });
        		*/
        		;
        return (att.getId() != null) ? 
                post.map( this::mapToObject ) : 
                post.flatMap( this::mapToArray )
                ;
        
    }
}
