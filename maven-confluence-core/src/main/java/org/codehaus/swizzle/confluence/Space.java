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
public class Space extends SpaceSummary {

    public Space() {
        super();
    }

    public Space(Map data) {
        super(data);
    }

    /**
     * the id of the space homepage
     */
    public String getHomepage() {
        return getString("homepage");
    }

    public void setHomepage(String homepage) {
        setString("homepage", homepage);
    }

    /**
     * the HTML rendered space description
     */
    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        setString("description", description);
    }

}
