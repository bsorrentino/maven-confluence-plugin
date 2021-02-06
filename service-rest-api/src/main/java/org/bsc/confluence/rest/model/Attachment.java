/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.bsc.confluence.ConfluenceService.Model;

/*
{
  "id": "att4620361",
  "type": "attachment",
  "status": "current",
  "title": "pom.xml",
  "when": "2017-08-13T15:02:24.082Z",
  "message": "",
  "number": 1,
  "minorEdit": false,
  "metadata": {
    "mediaType": "text/xml",
    "labels": {
      "results": [],
      "start": 0,
      "limit": 200,
      "size": 0,
      "_links": {
        "self": "http://localhost:8090/rest/api/content/att4620361/label"
      }
    },
  "extensions": {
    "mediaType": "text/xml",
    "fileSize": 8356,
    "comment": ""
  }
}
*/
/**
 *
 * @author softphone
 */
public class Attachment implements Model.Attachment {
    
    String id;
    String fileName;
    Date created;
    String contentType;
    String comment;
    
    final DateFormat ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public Attachment(JsonObject delegate) {
        if( delegate==null ) {
            throw new IllegalArgumentException("delegate argument is null!");
        } 
        
        id = delegate.getString("id");
        
        fileName = delegate.getString("title");
        
        final JsonObject extensions = delegate.getJsonObject("extensions");
        
        if( extensions != null ) {
            contentType = extensions.getString("mediaType");
            comment = extensions.getString("comment", "");            
        }
        final JsonObject version = delegate.getJsonObject("version");
        
        if( version != null ) {
            try {
                created = ISO8601Local.parse(version.getString("when"));
            } catch (ParseException ex) {
                created = new Date();
            }
        }
        
    }

    public Attachment() {
    }

    public String getId() {
        return id;
    }
    
    @Override
    public void setFileName(String name) {
        fileName = name;
    }

    @Override
    public String getFileName() {     
        return fileName;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

    @Override
    public Date getCreated() {
        return created;
    }
    
    public JsonObject toJsonObject() {
        
        final JsonObjectBuilder extensions = Json.createObjectBuilder()
                .add( "mediaType", contentType)
                .add( "comment", comment )
                ;
        
        return Json.createObjectBuilder()
                .add( "title", getFileName() )
                //.add( "when", ISO8601Local.format(created))
                .add( "extensions", extensions )
                .build();
    }

	@Override
	public String toString() {
		return new StringBuilder()
		.append("Attachment [id=")
		.append(id)
		.append(", fileName=")
		.append(fileName)
		.append(", created=")
		.append(created)
		.append(", contentType=")
		.append(contentType)
		.append(", comment=")
		.append(comment)
		.append("]")
		.toString();
	}
    
    
}
