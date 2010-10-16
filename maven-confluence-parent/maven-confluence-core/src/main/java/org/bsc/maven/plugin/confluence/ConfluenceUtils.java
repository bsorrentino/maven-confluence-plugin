package org.bsc.maven.plugin.confluence;

import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.codehaus.swizzle.confluence.Attachment;
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


	public static boolean addAttchment( Confluence confluence, Page page, Attachment attachment, java.io.File source ) throws Exception {

                Attachment a = confluence.getAttachment( page.getId(), attachment.getFileName(), "0");

                if( a!=null ) {

                    long lastModified = source.lastModified();

                    java.util.Date date = a.getCreated();
                }

                addAttchment(  confluence, page, attachment, new FileInputStream( source ));
		
		return true;
	}
        
	public static void addAttchment( Confluence confluence, Page page, Attachment attachment, java.io.InputStream source ) throws Exception {

                BufferedInputStream fis = new BufferedInputStream(source, 4096 );
                ByteArrayOutputStream baos = new ByteArrayOutputStream( );

                byte [] readbuf = new byte[4096];

                int len;

                while( (len=fis.read(readbuf))==readbuf.length ) {
                    baos.write(readbuf, 0, len);
                }
                if( len> 0 ) baos.write(readbuf, 0, len);

                attachment.setPageId( page.getId() );
                
		confluence.addAttachment( new Integer(attachment.getPageId()), attachment, baos.toByteArray() );


	}
	
}