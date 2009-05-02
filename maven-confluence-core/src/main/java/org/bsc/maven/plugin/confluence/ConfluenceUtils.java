package org.bsc.maven.plugin.confluence;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;

/**
 * 
 * @author Sorrentino
 *
 */
public class ConfluenceUtils {

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String decode( String value ) {
		if( null==value ) return value;
 		return value.replace("{", "\\{").replace("}", "\\}");	
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String encodeAnchor( String value ) {
		
		if( null==value ) return null;
	
		String v = decode(value);
		
		try {
			new java.net.URL(v);
			
			return v;
			
		} catch (MalformedURLException e) {

			return v.replace(':', '_');	
		
		}
		
		
		
 	}

    /**
     * 
     * @param parentPageId
     * @param title
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public static  PageSummary findPageByTitle( Confluence confluence, String parentPageId, String title) throws Exception {
		if( null==confluence ) throw new IllegalArgumentException("confluence instance is null");
		
		List<PageSummary> children = confluence.getChildren(parentPageId);

        for (PageSummary pageSummary : children ) {

        	if( title.equals(pageSummary.getTitle())) {
        		return pageSummary;
        	}
        }		

        return null;
	}

	/**
	 * 
	 * @param confluence
	 * @param parentPageId
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public static Page getOrCreatePage( Confluence confluence, String spaceKey, String parentPageTitle, String title ) throws Exception {
		if( null==confluence ) throw new IllegalArgumentException("confluence instance is null");

		Page parentPage = confluence.getPage(spaceKey, parentPageTitle);

		PageSummary pageSummary = findPageByTitle( confluence, parentPage.getId(), title);

    	Page result;
    	
    	if( null!=pageSummary ) {
        	result = confluence.getPage(pageSummary.getId());
    	}
    	else {
    		result = new Page(Collections.EMPTY_MAP);
    		result.setSpace(parentPage.getSpace());
    		result.setParentId(parentPage.getId());
    		result.setTitle(title);

    	}

    	return result;
	}

	/**
	 * 
	 * @param confluence
	 * @param parentPage
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public static Page getOrCreatePage( Confluence confluence, Page parentPage, String title ) throws Exception {
		if( null==confluence ) throw new IllegalArgumentException("confluence instance is null");

		PageSummary pageSummary = findPageByTitle( confluence, parentPage.getId(), title);

    	Page result;
    	
    	if( null!=pageSummary ) {
        	result = confluence.getPage(pageSummary.getId());
    	}
    	else {
    		result = new Page(Collections.EMPTY_MAP);
    		result.setSpace(parentPage.getSpace());
    		result.setParentId(parentPage.getId());
    		result.setTitle(title);

    	}

    	return result;
	}
}