package org.bsc.maven.test.confluence;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;
import org.codehaus.swizzle.confluence.Space;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Sorrentino
 *
 */
@SuppressWarnings("unchecked")
public class ConfluenceTestCase {
	
	static boolean ENABLE;
	
	static String SPACE = "SWDOC";	
	static String username = "admin";
	static String password = "bartolo";
	static String endpoint;
	
	Confluence confluence;

	
	static String pad(String str, int width) {
        for (int i = str.length(); i < width; i++) {
            str += " ";
        }
        return str.substring(0, width);
    }
	
	@BeforeClass
	public static void loadProperties() {
	
		try {
			java.util.Properties p = new java.util.Properties();
			
			java.io.InputStream is = ConfluenceTestCase.class.getClassLoader().getResourceAsStream("test.properties");

			p.load(is);
			
			ENABLE = Boolean.TRUE.equals( Boolean.valueOf(p.getProperty("test.enable", "false").trim()));

			endpoint = p.getProperty("confluence.endpoint","http://localhost:8080/rpc/xmlrpc" );
			SPACE = p.getProperty("confluence.space","SWDOC" );	
		 	username = p.getProperty("confluence.username","admin" );
		    password = p.getProperty("confluence.password","bartolo" );
			
		} catch (Exception e) {
			
			System.out.printf( "problem to load test.properties the test will be switched off : %s", e.getMessage());
			
			ENABLE = false; 
		}
		
	}
	
	@Before
	public void logIn() throws Exception {
	
		if( !ENABLE ) return;
		
		
		confluence = new Confluence( endpoint );
        confluence.login(username, password);

	}
	
	@After
	public void logOut() throws Exception {

		if( !ENABLE ) return;
		
		confluence.logout();
		confluence = null;
	}
	
	@Test()
	public void createOrUpdate() throws Exception {
		if( !ENABLE ) return;

        Space space = confluence.getSpace(SPACE);
        
        Page home = confluence.getPage(SPACE, "Home");
        
        System.out.printf( "space.name=%s home = %s\n", space.getKey(), home.toString());
        
        PageSummary ps = findPageByTitle(home.getId(), "Test Page");
        
        
        Page page = null;
        
        if( null==ps ) {
        	page = new Page(Collections.EMPTY_MAP);
        	page.setSpace(space.getKey());
        	page.setParentId(home.getId());
            page.setTitle("Test Page");
        }	
        else {
        	page = confluence.getPage(ps.getId());
        }
        
        page.setContent("This is a test");
       
        confluence.storePage(page);
		
	}
	
	private PageSummary findPageByTitle( String parentPageId, String title) throws Exception {
		List<PageSummary> children = confluence.getChildren(parentPageId);

        for (PageSummary pageSummary : children ) {

        	if( title.equals(pageSummary.getTitle())) {
        		return pageSummary;
        	}
        }		

        return null;
	}
	
	@Test
	public void printHomeChildren() throws Exception {
		if( !ENABLE ) return;
		
        Page home = confluence.getPage(SPACE, "Home");

		List<PageSummary> children = confluence.getChildren(home.getId());

        System.out.println("\n\n" + pad("PARENT", 10) + pad("TITLE", 20) + " " + pad("ID", 10) + pad("URL", 50));

        for (PageSummary pageSummary : children ) {

            String parentId = pageSummary.getParentId();
            String title = pageSummary.getTitle();
            String id = pageSummary.getId();
            String url = pageSummary.getUrl();

            System.out.println(pad(parentId, 10) + pad(title, 20) + " " + pad(id, 10) + pad(url, 50));
        }		
	}
}
