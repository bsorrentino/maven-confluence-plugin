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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public class MapObject {

    private static final SimpleDateFormat[] formats;

    static {
        formats = new SimpleDateFormat[] { new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.US), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd HH:mmZ", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US),
                // XML-RPC spec compliant iso8601 formats
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd", Locale.US), new SimpleDateFormat("yyyyMMdd", Locale.US) };
    }

    protected final Map fields;

    protected MapObject() {
        this(new HashMap());
    }

    protected MapObject(Map data) {
        fields = new HashMap(data);
    }

    protected String getString(String key) {
        Object o = fields.get(key);

        if (o instanceof String) {
            return (String) o;
        }

        if (o == null) return null;

        if (o instanceof Date) {
            return fromDate((Date) o);
        }

        return o.toString();
    }

    protected void setString(String key, String value) {
        fields.put(key, value);
    }

    protected void setInt(String key, int value) {
        fields.put(key, new Integer(value));
    }

    protected void setInt(String key, String value) {
        fields.put(key, new Integer(value));
    }

    protected int getInt(String key) {
        Object o = fields.get(key);

        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }

        if (o instanceof String) {
            setInt(key, (String) o);
            return getInt(key);
        }

        if (o == null) return 0;

        throw new IllegalStateException("Field '" + key + "' is of unknown type: " + o.getClass().getName());
    }

    protected void setBoolean(String key, boolean value) {
        fields.put(key, new Boolean(value));
    }

    protected void setBoolean(String key, String value) {
        boolean b = (value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("yes"));
        setBoolean(key, b);
    }

    protected boolean getBoolean(String key) {
        Object o = fields.get(key);

        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }

        if (o instanceof String) {
            setBoolean(key, (String) o);
            return getBoolean(key);
        }

        if (o == null) return false;

        throw new IllegalStateException("Field '" + key + "' is of unknown type: " + o.getClass().getName());
    }

    protected void setDate(String key, Date value) {
        fields.put(key, value);
    }

    protected void setDate(String key, String value) {
        fields.put(key, toDate(value));
    }

    protected Date getDate(String key) {
        Object o = fields.get(key);

        if (o instanceof Date) {
            return (Date) o;
        }

        if (o instanceof String) {
            setDate(key, (String) o);
            return getDate(key);
        }

        if (o == null) return null;

        throw new IllegalStateException("Field '" + key + "' is of unknown type: " + o.getClass().getName());
    }

    private String fromDate(Date value) {
        return formats[0].format(value);
    }

    private Date toDate(String value) {
        if (value == null || value.equals("")) return new Date();

        ParseException notParsable = null;
        for (int i = 0; i < formats.length; i++) {
            try {
                return formats[i].parse(value);
            } catch (ParseException e) {
                notParsable = e;
            }
        }

        notParsable.printStackTrace();
        return new Date();
    }

    /*
     * Defensive programming. Since we are allowing users to insert arrays and maps in MapObjects, we check that those structured types don't contain any not supported types.
     */
    private void checkXmlRpcTypes(Object object) {
        boolean checkPassed = false;

        if (object.getClass().isArray()) {
            Object[] objects = (Object[]) object;
            for (int i = 0; i < objects.length; i++) {
                checkXmlRpcTypes(objects[i]);
            }

            checkPassed = true;
        } else if (object instanceof List) {
            List list = (List) object;
            for (int i = 0; i < list.size(); i++) {
                checkXmlRpcTypes(list.get(i));
            }

            checkPassed = true;
        } else if (object instanceof Map) {
            Map map = (Map) object;
            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                Object key = i.next();
                Object value = map.get(key);
                checkXmlRpcTypes(value);
            }

            checkPassed = true;
        } else if (object instanceof String) {
            checkPassed = true;
        } else if (object instanceof Date) {
            checkPassed = true;
        } else if (object instanceof Integer) {
            checkPassed = true;
        } else if (object instanceof Boolean) {
            checkPassed = true;
        }

        if (!checkPassed) {
            throw new IllegalStateException("Object '" + object.toString() + "' has a not supported type " + object.getClass().getName());
        }
    }

    protected void setList(String key, List value) {
        checkXmlRpcTypes(value);
        fields.put(key, value);
    }

    protected List getList(String key) {
        Object objects = fields.get(key);

        if (objects == null) return null;

        if (objects instanceof List) return (List) objects;

        if (objects.getClass().isArray()) {
            ArrayList result = new ArrayList();
            Object[] array = (Object[]) objects;
            for (int i = 0; i < array.length; i++) {
                result.add(array[i]);
            }
            return result; // Arrays.asList((Object[]) objects);
        }

        throw new IllegalStateException("Field '" + key + "' is of unknown type: " + objects.getClass().getName());
    }

    protected void setMap(String key, Map value) {
        checkXmlRpcTypes(value);
        fields.put(key, value);
    }

    protected Map getMap(String key) {
        Object object = fields.get(key);

        if (object == null) return null;

        if (object instanceof Map) return (Map) object;

        throw new IllegalStateException("Field '" + key + "' is of unknown type: " + object.getClass().getName());
    }

    public Map toMap() {
        HashMap map = new HashMap(fields.size());
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            map.put(key, getString(key));
        }
        return map;
    }

    public Map toRawMap() {
        return new HashMap(fields);
    }

    public String toString() {
        return toMap().toString();
    }
}
