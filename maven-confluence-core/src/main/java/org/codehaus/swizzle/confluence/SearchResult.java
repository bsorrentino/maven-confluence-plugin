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
package org.codehaus.swizzle.confluence;

import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class SearchResult extends MapObject {

    public SearchResult() {
        super();
    }

    public SearchResult(Map data) {
        super(data);
    }

    /**
     * the feed's title
     */
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        setString("title", title);
    }

    /**
     * the remote URL needed to view this search result online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

    /**
     * a short excerpt of this result if it makes sense
     */
    public String getExcerpt() {
        return getString("excerpt");
    }

    public void setExcerpt(String excerpt) {
        setString("excerpt", excerpt);
    }

    /**
     * the type of this result - page, comment, spacedesc, attachment, userinfo, blogpost
     */
    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        setString("type", type);
    }

    /**
     * the long ID of this result (if the type has one)
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

}
