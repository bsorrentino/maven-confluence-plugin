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

import java.util.Date;
import java.util.Map;
import org.bsc.confluence.ConfluenceService.Model;

/**
 * @version $Revision$ $Date$
 */
class Page extends PageSummary implements Model.Page {

    public Page() {
        super();
    }

    public Page(Map data) {
        super(data);
    }

    /**
     * the version number of this page
     */
    public int getVersion() {
        return getInt("version");
    }

    public void setVersion(int version) {
        setInt("version", version);
    }

    /**
     * the page content
     */
    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        setString("content", content);
    }

    /**
     * timestamp page was created
     */
    public Date getCreated() {
        return getDate("created");
    }

    public void setCreated(Date created) {
        setDate("created", created);
    }

    /**
     * username of the creator
     */
    public String getCreator() {
        return getString("creator");
    }

    public void setCreator(String creator) {
        setString("creator", creator);
    }

    /**
     * timestamp page was modified
     */
    public Date getModified() {
        return getDate("modified");
    }

    public void setModified(Date modified) {
        setDate("modified", modified);
    }

    /**
     * username of the page's last modifier
     */
    public String getModifier() {
        return getString("modifier");
    }

    public void setModifier(String modifier) {
        setString("modifier", modifier);
    }

    /**
     * whether or not this page is the space's homepage
     */
    public boolean isHomePage() {
        return getBoolean("homePage");
    }

    public void setHomePage(boolean homePage) {
        setBoolean("homePage", homePage);
    }

    /**
     * status of the page (eg current or deleted)
     */
    public String getContentStatus() {
        return getString("contentStatus");
    }

    public void setContentStatus(String contentStatus) {
        setString("contentStatus", contentStatus);
    }

    /**
     * whether the page is current and not deleted
     */
    public boolean isCurrent() {
        return getBoolean("current");
    }

    public void setCurrent(boolean current) {
        setBoolean("current", current);
    }

    public Map toRawMap() {
        Map map = super.toRawMap();
        map.put("created", getCreated());
        map.put("modified", getModified());
        map.put("homePage", new Boolean(isHomePage()));
        map.put("current", new Boolean(isCurrent()));
        map.put("version", new Integer(getVersion()));
        return map;
    }
}
