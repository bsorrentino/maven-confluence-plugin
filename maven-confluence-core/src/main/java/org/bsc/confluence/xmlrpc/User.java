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
public class User extends MapObject {

    public User() {
        super();
    }

    public User(Map<String,Object> data) {
        super(data);
    }

    /**
     * the username of this user
     */
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setString("name", name);
    }

    /**
     * the full name of this user
     */
    public String getFullname() {
        return getString("fullname");
    }

    public void setFullname(String fullname) {
        setString("fullname", fullname);
    }

    /**
     * the email address of this user
     */
    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        setString("email", email);
    }

    /**
     * the url to view this user online
     */
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

}
