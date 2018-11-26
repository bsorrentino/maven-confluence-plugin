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

import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class BlogEntry extends MapObject {

    public BlogEntry() {
        super();
    }

    public BlogEntry(Map<String,Object> data) {
        super(data);
    }

    /**
     * the id of the blog entry
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

    /**
     * the key of the space that this blog entry belongs to
     */
    public String getSpace() {
        return getString("space");
    }

    public void setSpace(String space) {
        setString("space", space);
    }
    
    /**
     * username of the author
     */
    public String getAuthor() {
        return getString("author");
    }

    public void setAuthor(String author) {
        setString("author", author);
    }

    /**
     * the title of the page
     */
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        setString("title", title);
    }

    /**
     * the url to view this blog entry online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

    /**
     * the version number of this blog entry
     */
    public int getVersion() {
        return getInt("version");
    }

    public void setVersion(int version) {
        setInt("version", version);
    }

    /**
     * the blog entry content
     */
    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        setString("content", content);
    }

    /**
     * the number of locks current on this page
     */
    public int getLocks() {
        return getInt("locks");
    }

    public void setLocks(int locks) {
        setInt("locks", locks);
    }

    public Map<String,Object> toRawMap() {
        Map<String,Object> map = super.toRawMap();
        map.put("version", new Integer(getVersion()));
        map.put("locks", new Integer(getLocks()));
        return map;
    }

}
