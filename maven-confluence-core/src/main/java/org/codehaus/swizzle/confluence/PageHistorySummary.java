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

/**
 * @version $Revision$ $Date$
 */
public class PageHistorySummary extends MapObject {

    public PageHistorySummary() {
        super();
    }

    public PageHistorySummary(Map data) {
        super(data);
    }

    /**
     * the id of the historical page
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

    /**
     * the version of this historical page
     */
    public int getVersion() {
        return getInt("version");
    }

    public void setVersion(int version) {
        setInt("version", version);
    }

    /**
     * the user who made this change
     */
    public String getModifier() {
        return getString("modifier");
    }

    public void setModifier(String modifier) {
        setString("modifier", modifier);
    }

    /**
     * timestamp change was made
     */
    public Date getModified() {
        return getDate("modified");
    }

    public void setModified(Date modified) {
        setDate("modified", modified);
    }

}
