/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.confluence;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 *
 * @author softphone
 */
public class Issue106IntegrationTest {
    
    OkHttpClient.Builder client;
    
    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    @Test @Ignore
    public void dummy() {}
    
    @Before
    public void open() {
         client = new OkHttpClient.Builder(); 
 
         /*
         client.authenticator( new Authenticator() {
             @Override
             public Request authenticate(Route route, Response response) throws IOException {
                System.out.println( "AUTHENTICATOR" );

                if (responseCount(response) >= 3) {
                    return null; // If we've failed 3 times, give up. - in real life, never give up!!
                }
                
                final String credential = Credentials.basic("admin", "admin");
                
                return response.request()
                                .newBuilder()
                                .header("Authorization", credential)
                                .build();
                 
             }
         });
         */
         
         client.connectTimeout(10, TimeUnit.SECONDS);
         client.writeTimeout(10, TimeUnit.SECONDS);
         client.readTimeout(30, TimeUnit.SECONDS);

    }
    
    @Test
    public void getStorageFormat() throws IOException {
        
        final String credential = Credentials.basic("admin", "admin");

        final HttpUrl url =   new HttpUrl.Builder()
                                    .scheme("http")
                                    .host("192.168.99.100")
                                    .port(8090)
                                    .addPathSegments("plugins/viewstorage/viewpagestorage.action")
                                    .addQueryParameter("pageId", "1867778")
                                    .build();
        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )  
                .get()
                .build();
        
        final Response res = client.build().newCall(req).execute();
        
        Assert.assertThat( res, IsNull.notNullValue());
        Assert.assertThat( res.isSuccessful(), Is.is(true));
        final ResponseBody body = res.body();
        Assert.assertThat( body, IsNull.notNullValue());

        System.out.printf( "BODY\n%s\n", body.string() );
        
    }
    
    @Test
    public void findPage() throws IOException {

        final String credential = Credentials.basic("admin", "admin");

        final HttpUrl url =   new HttpUrl.Builder()
                                    .scheme("http")
                                    .host("192.168.99.100")
                                    .port(8090)
                                    .addPathSegments("rest/api/")
                                    .addPathSegment("content")                
                                    .addQueryParameter("spaceKey", "TEST")
                                    .addQueryParameter("title", "Home")
                                    .build();
        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url )  
                .get()
                .build();
        
        final Response res = client.build().newCall(req).execute();
        
        Assert.assertThat( res, IsNull.notNullValue());
        Assert.assertThat( res.isSuccessful(), Is.is(true));
        final ResponseBody body = res.body();
        Assert.assertThat( body, IsNull.notNullValue());

        try( Reader r = body.charStream()) {
            
            final JsonReader rdr = Json.createReader(r);
            
            final JsonObject root = rdr.readObject();
            
            Assert.assertThat( root,  IsNull.notNullValue() );
            Assert.assertThat( root.containsKey("results"), Is.is(true) );
            
            final JsonArray results = root.getJsonArray("results");
            Assert.assertThat( results,  IsNull.notNullValue() );
            Assert.assertThat( results.size(),  IsEqual.equalTo(1) );

            final JsonObject item0 = results.getJsonObject(0);
            Assert.assertThat( item0,  IsNull.notNullValue() );
            Assert.assertThat( item0.containsKey("id"), Is.is(true) );
            Assert.assertThat( item0.getString("id"), IsEqual.equalTo("1867778") );
            
        }
        
    }
    
    @Test
    public void addPage() throws IOException {

        final String credential = Credentials.basic("admin", "admin");

        final HttpUrl.Builder url = new HttpUrl.Builder()
                                    .scheme("http")
                                    .host("192.168.99.100")
                                    .port(8090)
                                    .addPathSegments("rest/api/content")
                                    ;
        
        final MediaType storageFormat = MediaType.parse("application/json");
        
        JsonObject inputData = Json.createObjectBuilder()
                .add("type","page")
                .add("title","test2")
                .add("space",Json.createObjectBuilder().add("key", "TEST"))
                .add("body", Json.createObjectBuilder()
                                .add("storage", Json.createObjectBuilder()
                                                .add("representation","storage")
                                                .add("value","<H1>TITLE 2</H1>")))
                .build();
        final RequestBody inputBody = RequestBody.create(storageFormat, 
                inputData.toString());
        
        final Request req = new Request.Builder()
                .header("Authorization", credential)
                .url( url.build() )  
                .post(inputBody)
                .build();
        
        final Response res = client.build().newCall(req).execute();
        
        Assert.assertThat( res, IsNull.notNullValue());
        Assert.assertThat( res.isSuccessful(), Is.is(true));
        final ResponseBody body = res.body();
        Assert.assertThat( body, IsNull.notNullValue());

        try( Reader r = body.charStream()) {
            
            final JsonReader rdr = Json.createReader(r);
            
            final JsonObject root = rdr.readObject();
            
            Assert.assertThat( root,  IsNull.notNullValue() );
            
            Assert.assertThat( root.containsKey("id"), Is.is(true) );
            //Assert.assertThat( item0.getString("id"), IsEqual.equalTo("1867778") );

            
            // DELETE
            
            
            final Request req1 = new Request.Builder()
                .header("Authorization", credential)
                .url( url.addPathSegment(root.getString("id")).build() )     
                .delete()    
                .build();
            
            final Response res1 = client.build().newCall(req1).execute();
            
            Assert.assertThat( res1, IsNull.notNullValue());
            Assert.assertThat( res1.code(), IsEqual.equalTo(204));

            


        }
        
        
    }
    
}
