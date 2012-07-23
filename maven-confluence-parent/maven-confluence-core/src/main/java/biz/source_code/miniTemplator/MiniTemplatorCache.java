// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, http://www.gnu.org/licenses/lgpl.html
//
// This module is provided "as is", without warranties of any kind.

package biz.source_code.miniTemplator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
* A cache manager for MiniTemplator objects.
* This class is used to cache MiniTemplator objects in memory, so that
* each template file is only read and parsed once.
*
* <p>
* Home page: <a href="http://www.source-code.biz/MiniTemplator">www.source-code.biz/MiniTemplator</a><br>
* Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
* Multi-licensed: EPL/LGPL.
*/
public class MiniTemplatorCache {

private HashMap<String,MiniTemplator> cache;               // buffered MiniTemplator objects

/**
* Creates a new MiniTemplatorCache object.
*/
public MiniTemplatorCache() {
   cache = new HashMap<String,MiniTemplator>(); }

/**
* Returns a cloned MiniTemplator object from the cache.
* If there is not yet a MiniTemplator object with the specified <code>templateFileName</code>
* in the cache, a new MiniTemplator object is created and stored in the cache.
* Then the cached MiniTemplator object is cloned and the clone object is returned.
* @param  templateSpec      the template specification.
* @return                   a cloned and reset MiniTemplator object.
*/
public synchronized MiniTemplator get (MiniTemplator.TemplateSpecification templateSpec)
      throws IOException, MiniTemplator.TemplateSyntaxException {
   String key = generateCacheKey(templateSpec);
   MiniTemplator mt = cache.get(key);
   if (mt == null) {
      mt = new MiniTemplator(templateSpec);
      cache.put(key, mt); }
   return mt.cloneReset(); }

private static String generateCacheKey (MiniTemplator.TemplateSpecification templateSpec) {
   StringBuilder key = new StringBuilder(128);
   if (templateSpec.url != null)
      key.append(templateSpec.url);
    else
      throw new IllegalArgumentException("No template url specified.");
   if (templateSpec.conditionFlags != null) {
      for (String flag : templateSpec.conditionFlags) {
         key.append('|');
         key.append(flag.toUpperCase()); }}
   return key.toString(); }

/**
* Clears the cache.
*/
public synchronized void clear() {
   cache.clear(); }

} // end class MiniTemplatorCache