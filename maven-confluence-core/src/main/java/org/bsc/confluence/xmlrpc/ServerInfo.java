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
public class ServerInfo extends MapObject {

    public ServerInfo() {
        super();
    }

    public ServerInfo(Map data) {
        super(data);
    }

    /**
     * the major version number of the Confluence instance
     */
    public int getMajorVersion() {
        return getInt("majorVersion");
    }

    public void setMajorVersion(int majorVersion) {
        setInt("majorVersion", majorVersion);
    }

    /**
     * the minor version number of the Confluence instance
     */
    public int getMinorVersion() {
        return getInt("minorVersion");
    }

    public void setMinorVersion(int minorVersion) {
        setInt("minorVersion", minorVersion);
    }

    /**
     * the patch-level of the Confluence instance
     */
    public int getPatchLevel() {
        return getInt("patchLevel");
    }

    public void setPatchLevel(int patchLevel) {
        setInt("patchLevel", patchLevel);
    }

    /**
     * the build ID of the Confluence instance (usually a number)
     */
    public String getBuildId() {
        return getString("buildId");
    }

    public void setBuildId(String buildId) {
        setString("buildId", buildId);
    }

    /**
     * Whether the build is a developer-only release or not
     */
    public boolean isDevelopmentBuild() {
        return getBoolean("developmentBuild");
    }

    public void setDevelopmentBuild(boolean developmentBuild) {
        setBoolean("developmentBuild", developmentBuild);
    }

    /**
     * The base URL for the confluence instance
     */
    public String getBaseUrl() {
        return getString("baseUrl");
    }

    public void setBaseUrl(String baseUrl) {
        setString("baseUrl", baseUrl);
    }

    public Map toRawMap() {
        Map map = super.toRawMap();
        map.put("majorVersion", new Integer(getMajorVersion()));
        map.put("minorVersion", new Integer(getMinorVersion()));
        map.put("patchLevel", new Integer(getPatchLevel()));
        map.put("developmentBuild", new Boolean(isDevelopmentBuild()));
        return map;
    }
}
