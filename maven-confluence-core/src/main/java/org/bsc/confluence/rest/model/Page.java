/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.model;

import javax.json.JsonObject;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;

/**
 *
 * @author softphone
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
    public String getId() {
        
        return data.getString("id");
    }

    @Override
    public String getTitle() {
        return data.getString("title");
    }

    @Override
    public String getSpace() {
        return data.getString("space");
    }
    
}
