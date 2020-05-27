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
public class Permission extends MapObject {

    public Permission() {
        super();
    }

    public Permission(Map<String,Object> data) {
        super(data);
    }

    /**
     * The type of permission. One of 'View' or 'Edit'
     */
    public String getLockType() {
        return getString("lockType");
    }

    public void setLockType(String lockType) {
        setString("lockType", lockType);
    }

    /**
     * The user or group name of the permission's owner
     */
    public String getLockedBy() {
        return getString("lockedBy");
    }

    public void setLockedBy(String lockedBy) {
        setString("lockedBy", lockedBy);
    }

}
