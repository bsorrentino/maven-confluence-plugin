/**
 *
 * Copyright 2006 David Blevins
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.bsc.confluence.xmlrpc.model;

import org.bsc.confluence.xmlrpc.model.MapObject;

import java.util.Date;
import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class UserInformation extends MapObject {

    public UserInformation() {
        super();
    }

    public UserInformation(Map<String,Object> data) {
        super(data);
    }

    /**
     * the username of this user
     */
    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        setString("username", username);
    }

    /**
     * the user description
     */
    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        setString("content", content);
    }

    /**
     * the creator of the user
     */
    public String getCreatorName() {
        return getString("creatorName");
    }

    public void setCreatorName(String creatorName) {
        setString("creatorName", creatorName);
    }

    /**
     * the url to view this user online
     */
    public String getLastModifierName() {
        return getString("lastModifierName");
    }

    public void setLastModifierName(String lastModifierName) {
        setString("lastModifierName", lastModifierName);
    }

    /**
     * the version
     */
    public int getVersion() {
        return getInt("version");
    }

    public void setVersion(int version) {
        setInt("version", version);
    }

    /**
     * the ID of the user
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

    /**
     * the date the user was created
     */
    public Date getCreationDate() {
        return getDate("creationDate");
    }

    public void setCreationDate(Date creationDate) {
        setDate("creationDate", creationDate);
    }

    /**
     * the date the user was last modified
     */
    public Date getLastModificationDate() {
        return getDate("lastModificationDate");
    }

    public void setLastModificationDate(Date lastModificationDate) {
        setDate("lastModificationDate", lastModificationDate);
    }

    public Map<String,Object> toRawMap() {
        Map<String,Object> map = super.toRawMap();
        map.put("version", getVersion());
        map.put("creationDate", getCreationDate());
        map.put("lastModificationDate", getLastModificationDate());
        return map;
    }
}
