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
package org.bsc.confluence.xmlrpc;

import java.util.Date;
import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class Comment extends MapObject {

    public Comment() {
        super();
    }

    public Comment(Map<String,Object> data) {
        super(data);
    }

    /**
     * numeric id of the comment
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

    /**
     * page ID of the comment
     */
    public String getPageId() {
        return getString("pageId");
    }

    public void setPageId(String pageId) {
        setString("pageId", pageId);
    }

    /**
     * title of the comment
     */
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        setString("title", title);
    }

    /**
     * notated content of the comment (use renderContent to render)
     */
    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        setString("content", content);
    }

    /**
     * url to view the comment online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

    /**
     * creation date of the attachment
     */
    public Date getCreated() {
        return getDate("created");
    }

    public void setCreated(Date created) {
        setDate("created", created);
    }

    /**
     * creator of the attachment
     */
    public String getCreator() {
        return getString("creator");
    }

    public void setCreator(String creator) {
        setString("creator", creator);
    }

    public Map<String,Object> toRawMap() {
        Map<String,Object> map = super.toRawMap();
        map.put("created", getCreated());
        return map;
    }
}
