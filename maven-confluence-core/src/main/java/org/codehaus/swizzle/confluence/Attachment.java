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
class Attachment extends MapObject implements Model.Attachment {

    public Attachment() {
        super();
    }

    public Attachment(Map data) {
        super(data);
    }

    /**
     * numeric id of the attachment
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

    /**
     * page ID of the attachment
     */
    public String getPageId() {
        return getString("pageId");
    }

    public void setPageId(String pageId) {
        setString("pageId", pageId);
    }

    /**
     * title of the attachment
     */
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        setString("title", title);
    }

    /**
     * file name of the attachment {color:#cc3300}(Required){color}
     */
    public String getFileName() {
        return getString("fileName");
    }

    public void setFileName(String fileName) {
        setString("fileName", fileName);
    }

    /**
     * numeric file size of the attachment in bytes
     */
    public String getFileSize() {
        return getString("fileSize");
    }

    public void setFileSize(String fileSize) {
        setString("fileSize", fileSize);
    }

    /**
     * mime content type of the attachment {color:#cc0000}(Required){color}
     */
    public String getContentType() {
        return getString("contentType");
    }

    public void setContentType(String contentType) {
        setString("contentType", contentType);
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

    /**
     * url to download the attachment online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

    /**
     * comment for the attachment {color:#cc3300}(Required){color}
     */
    public String getComment() {
        return getString("comment");
    }

    public void setComment(String comment) {
        setString("comment", comment);
    }

    public Map toRawMap() {
        Map map = super.toRawMap();
        map.put("created", getCreated());
        return map;
    }
}
