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

import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class Label extends MapObject {

    public Label() {
        super();
    }

    public Label(Map<String,Object> data) {
        super(data);
    }

    /**
     * the nameof the label
     */
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setString("name", name);
    }

    /**
     * the username of the owner
     */
    public String getOwner() {
        return getString("owner");
    }

    public void setOwner(String owner) {
        setString("owner", owner);
    }

    /**
     * the namespace of the label
     */
    public String getNamespace() {
        return getString("namespace");
    }

    public void setNamespace(String namespace) {
        setString("namespace", namespace);
    }

    /**
     * the ID of the label
     */
    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        setString("id", id);
    }

}
