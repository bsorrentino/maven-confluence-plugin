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

import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class SpaceSummary extends MapObject {

    public SpaceSummary() {
        super();
    }

    public SpaceSummary(Map<String,Object> data) {
        super(data);
    }

    /**
     * the space key
     */
    public String getKey() {
        return getString("key");
    }

    public void setKey(String key) {
        setString("key", key);
    }

    /**
     * the name of the space
     */
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setString("name", name);
    }

    /**
     * the url to view this space online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

}
