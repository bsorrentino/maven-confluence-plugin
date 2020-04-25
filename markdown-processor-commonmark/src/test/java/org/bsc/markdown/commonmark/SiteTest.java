package org.bsc.markdown.commonmark;

import lombok.Data;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;
import org.bsc.markdown.MarkdownProcessorInfo;
import org.bsc.markdown.MarkdownProcessorProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.bsc.confluence.model.SiteProcessor.processMarkdown;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author bsorrentino
 *
 */
public class SiteTest implements SiteFactory.Model {

    @Data(staticConstructor="of")
    static class TestPage implements Model.Page {
        final String title;
        final String space;


        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getParentId() {
            return null;
        }

        @Override
        public int getVersion() {
            return 0;
        }

    }

    private List<String> splitStrippingOutNewlines(String content ) {
        return asList(content.split("[\n]{1,}+"));
    }

    @Override
    public Site createSiteFromModel(Map<String, Object> variables) {
        val path = Paths.get("src", "test", "resources", "site.yaml");
        try {
            return createFrom( path.toFile(), variables);
        } catch (Exception e) {
            throw new RuntimeException(String.format("error reading site descriptor at [%s]", path), e);
        }
    }

    Site site;

    @BeforeClass
    public static void setMarkdownPorcessorProvider() {
        MarkdownProcessorProvider.instance.setInfo( new MarkdownProcessorInfo("commonmark") );
    }
    @Before
    public void loadSite() {
        site = createSiteFromModel(Collections.emptyMap());
        site.setBasedir( Paths.get("src", "test", "resources"));
    }
    
    @Test
    public void validateName() {
        
        final Pattern p = Pattern.compile("^(.*)\\s+$|^\\s+(.*)");
        assertFalse( p.matcher("name").matches() );
        assertTrue( p.matcher("name ").matches());
        assertTrue( p.matcher("name  ").matches());
        assertTrue( p.matcher(" name").matches());
        assertTrue( p.matcher("  name").matches());
        assertTrue( p.matcher(" name ").matches());
        assertTrue( p.matcher(" name  ").matches());
    }
    
    @Test
    public void shouldSupportRefLink() throws IOException {
        val parentPageTitle = Optional.of("Test");
        val stream = getClass().getClassLoader().getResourceAsStream("withRefLink.md");
        assertNotNull( stream);
        val content = processMarkdown(site, site.getHome(), Optional.empty(), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( content );

        val converted = content.split("\n+");
        int i = 1;

        assertEquals( format("* relative link to another page [SECOND PAGE|%s - page 2].", parentPageTitle.get()), converted[i++] );
        assertEquals( "* This one is [inline|http://google.com|Google].", converted[i++] );
        assertEquals( "* This one is [inline *wo* title|http://google.com].", converted[i++] );
        assertEquals( "* This is my [google|http://google.com] link defined after.", converted[i++]);
        assertEquals( "* This is my [more complex google|http://google.com|Other google] link defined after.", converted[i++]);
        assertEquals( "* This is my [relative|relativepage] link defined after.", converted[i++] );
        assertEquals( "* This is my [rel|relativeagain] link defined after.", converted[i++]);
    }

    @Test
    public void shouldSupportImgRefLink() throws IOException {

        final Model.Page page = TestPage.of( "${page.title}", "spaceKey");

        final val parentPageTitle = Optional.of("Test IMG");

        final InputStream stream = getClass().getClassLoader().getResourceAsStream("withImgRefLink.md");
        assertNotNull( stream );
        val content = processMarkdown(site, site.getHome(), Optional.of(page), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( content );
        final String converted[] = content.split("\n+");

        int i = 1;
        assertEquals( "*  add an absolute !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon!  with title.", converted[i++]);
        assertEquals( "*  add a relative !${page.title}^conf-icon-64.png|conf-icon!  with title.", converted[i++]);
        assertEquals( "*  add a relative !${page.title}^conf-icon-64.png|conf-icon!  without title.", converted[i++]);
        assertEquals("*  add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y!  with title.", converted[i++] );
        assertEquals("*  add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y1!  without title.", converted[i++] );
        assertEquals("*  add a ref img !${page.title}^conf-icon-64.png|conf-icon-y2!  relative.", converted[i++] );
        assertEquals( "*  add a ref img !${page.title}^conf-icon-64.png|conf-icon-none!  relative with default refname.", converted[i++]);
    }


    @Test
    public void shouldSupportSimpleNode() throws IOException {
        val parentPageTitle = Optional.of("Test");

        final InputStream stream = getClass().getClassLoader().getResourceAsStream("simpleNodes.md");
        assertNotNull( stream );
        val converted = processMarkdown(site, site.getHome(), Optional.empty(), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( converted );

        assertLinesMatch(asList( new String[]{
                "h1. Horizontal rules",
                "----",
                "1",
                "----",
                "2",
                "----",
                "3",
                "----",
                "4",
                "----",
                "h1. Apostrophes",
                "'I'm with single quotes'",
                "\"you're with double quotes\"",
                "I'll talk to him 'bout borrowin' one of his models.",
                "h1. Ellipsis",
                "Three dots {{...}} will be converted to ...",
                "h1. Emdash",
                "Use 3 dashes {{---}} for an em-dash --- .",
                "# Range (endash)",
                "\"it's all in chapters 12--14\"",
                "h1. Line break",
                "Forcing a line-break\\",
                "Next line in the list",
                "h1. Nbsp",
                "      This will appear with six space characters in front of it."
        }), splitStrippingOutNewlines(converted) );
    }
    
    
}