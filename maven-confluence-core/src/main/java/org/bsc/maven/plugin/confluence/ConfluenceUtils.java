package org.bsc.maven.plugin.confluence;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.codehaus.swizzle.confluence.Attachment;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;
import org.codehaus.swizzle.confluence.ServerInfo;

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
 		final String result =  value
                        .replace("{", "\\{")
                        .replace("}", "\\}")
                        .replaceAll("<[Pp][Rr][Ee]>|</[Pp][Rr][Ee]>", "{noformat}")
                        .replaceAll("<[Cc][Oo][Dd][Ee]>", "{{")
                        .replaceAll("</[Cc][Oo][Dd][Ee]>", "}}")
                        .replaceAll("<[Bb]>|</Bb>", "*")
                        .replaceAll("<[Bb][Rr]>|<[Bb][Rr]/>", "\\\\")
                        .replaceAll("<[Hh][Rr]>|<[Hh][Rr]/>", "----")
                        .replaceAll("<[Pp]>", "\n\n")
                        .replaceAll("</[Pp]>", "")
                        
                        ;	
                return ConfluenceHtmlListUtils.replaceHtmlList(result);
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
	public static boolean removePage( Confluence confluence, String spaceKey, String parentPageTitle, String title ) throws Exception {
            
            if( null==confluence ) throw new IllegalArgumentException("confluence instance is null");
            
            Page parentPage = confluence.getPage(spaceKey, parentPageTitle);

            PageSummary pageSummary = findPageByTitle( confluence, parentPage.getId(), title);
            
            if( pageSummary!=null ) {
                confluence.removePage(pageSummary.getId());
                return true;
            }
            
            return false;
        }

        public static boolean removePage( Confluence confluence, Page parentPage, String title ) throws Exception {
            
            if( null==confluence ) throw new IllegalArgumentException("confluence instance is null");
            if( null==parentPage ) throw new IllegalArgumentException("parentPage is null");
            

            PageSummary pageSummary = findPageByTitle( confluence, parentPage.getId(), title);
            
            if( pageSummary!=null ) {
                confluence.removePage(pageSummary.getId());
                return true;
            }
            
            return false;
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
            if (null == confluence) {
                throw new IllegalArgumentException("confluence instance is null");
            }

            Page parentPage = confluence.getPage(spaceKey, parentPageTitle);

            PageSummary pageSummary = findPageByTitle(confluence, parentPage.getId(), title);

            Page result;

            if (null != pageSummary) {
                result = confluence.getPage(pageSummary.getId());
            } else {
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

	public static boolean addAttchment( Confluence confluence, Page page, Attachment attachment, java.net.URL source ) throws Exception {

		addAttchment(  confluence, page, attachment, source.openStream() );

		return true;
	}

	public static boolean addAttchment( Confluence confluence, Page page, Attachment attachment, java.io.File source ) throws Exception {

		addAttchment(  confluence, page, attachment, new FileInputStream( source ));

		return true;
	}
        
	public static void addAttchment( Confluence confluence, Page page, Attachment attachment, java.io.InputStream source ) throws Exception {

                if( page.getId() == null ) {
                    throw new IllegalStateException("PageId is null. Attachment cannot be added!");
                }
		BufferedInputStream fis = new BufferedInputStream(source, 4096 );
		ByteArrayOutputStream baos = new ByteArrayOutputStream( );

		byte [] readbuf = new byte[4096];

		int len;

		while( (len=fis.read(readbuf))==readbuf.length ) {
			baos.write(readbuf, 0, len);
		}
		if( len> 0 ) baos.write(readbuf, 0, len);

		attachment.setPageId( page.getId() );
                
		confluence.addAttachment( new Long(page.getId()), attachment, baos.toByteArray() );


	}
        
        public static String getVersion( Confluence confluence ) {
            try {
                final ServerInfo si = confluence.getServerInfo();
                return String.format("Confluence version [%d.%d.%d-%s] development version [%b]", si.getMajorVersion(), si.getMinorVersion(), si.getPatchLevel(), si.getBuildId(), si.isDevelopmentBuild());
            } catch (Exception ex) {
                // TODO LOG
                return ex.getMessage();
            } 
        
        }
        
        /**
         * 
         * @return ads banner
         */
        public static String getBannerWiki() {
            
            final StringBuilder wiki = new StringBuilder()
                .append("{info:title=").append("Generated page").append('}')
                .append("this page has been generated by plugin: ")
                .append("[org.bsc.maven:maven-confluence-reporting-plugin|https://github.com/bsorrentino/maven-confluence-plugin]")
                .append("{info}")
                .append('\n');

            return wiki.toString();
        }
	
}
