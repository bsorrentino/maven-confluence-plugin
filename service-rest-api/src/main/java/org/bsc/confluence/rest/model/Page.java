/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.model;

import javax.json.JsonObject;
import org.bsc.confluence.ConfluenceService.Model;

import java.util.Optional;

import static java.util.Optional.empty;

/**
 *
 * @author bsorrentino
 */
public class Page implements Model.Page {

    public final JsonObject data;

    public Page(JsonObject delegate) {
        if( delegate==null ) {
            throw new IllegalArgumentException("delegate argument is null!");
        } 
        
        this.data = delegate;
    }
    
    
    @Override
    public Model.ID getId() { return Model.ID.of(data.getString("id")); }

    @Override
    public String getTitle() {
        return data.getString("title");
    }

    @Override
    public String getSpace() {
        return data.getJsonObject("space").getString("key");
    }

    @Override
    public int getVersion() {
        return data.getJsonObject("version").getInt("number", 0);
    }

    @Override
    public Model.ID getParentId() {
        return Model.ID.of( IdHelper.getId(data.getJsonObject("container")) );
    }

    @Override
    public String toString() {
        return data.toString();
    }
    
}
