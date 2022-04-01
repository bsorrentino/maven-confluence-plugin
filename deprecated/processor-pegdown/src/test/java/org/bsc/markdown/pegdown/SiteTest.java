package org.bsc.markdown.pegdown;

import lombok.Data;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.String.format;
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
        public Model.ID getId() {
            return null;
        }

        @Override
        public Model.ID getParentId() {
            return null;
        }

        @Override
        public int getVersion() {
            return 0;
        }


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

    @BeforeEach
    public void loadSite() {
        site = createSiteFromModel(Collections.emptyMap());
        site.setBasedir( Paths.get("src", "test", "resources"));
    }
    
    @Test
    public void validateName() {
        
        final Pattern p = Pattern.compile("^(.*)\\s+$|^\\s+(.*)");
        assertFalse( p.matcher("name").matches() );
        assertTrue( p.matcher("name ").matches() );
        assertTrue( p.matcher("name  ").matches() );
        assertTrue( p.matcher(" name").matches() );
        assertTrue( p.matcher("  name").matches() );
        assertTrue( p.matcher(" name ").matches() );
        assertTrue( p.matcher(" name  ").matches() );
    }
    
    @Test
    public void shouldSupportRefLink() throws IOException {
        val parentPageTitle = Optional.of("Test");
        val stream = getClass().getClassLoader().getResourceAsStream("withRefLink.md");
        assertNotNull( stream );
        val content = processMarkdown(site, site.getHome(), Optional.empty(), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( content );
        val converted = content.split("\n+");

        int i = 2;
        assertEquals( format("* This is a relative link to another page [SECOND PAGE|%s - page 2|].", parentPageTitle.get()), converted[i++] );
        assertEquals( "* This one is [inline|http://google.com|Google].", converted[i++] );
        assertEquals( "* This one is [inline *wo* title|http://google.com|].", converted[i++] );
        assertTrue( converted[i++].contains("[google|http://google.com]") );
        assertTrue( converted[i++].contains("[more complex google|http://google.com|Other google]") );
        assertEquals( "* This is my [relative|relativepage|] link defined after.", converted[i++] );
        assertTrue( converted[i++].contains("[rel|Test - relativeagain]") );
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

        int i = 2;
        assertTrue( converted[i++].contains("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon!") );
        assertTrue( converted[i++].contains("!${page.title}^conf-icon-64.png|conf-icon!") );
        assertTrue( converted[i++].contains("!${page.title}^conf-icon-64.png|conf-icon!") );
        assertTrue( converted[i++].contains("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y!") );
        assertTrue( converted[i++].contains("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|conf-icon-y1!") );
        assertTrue( converted[i++].contains("!${page.title}^conf-icon-64.png|conf-icon-y2!") );
        assertTrue( converted[i++].contains("!${page.title}^conf-icon-64.png|conf-icon-none!") );
    }

    @Test
    public void shouldSupportSimpleNode() throws IOException {
        val parentPageTitle = Optional.of("Test");

        final InputStream stream = getClass().getClassLoader().getResourceAsStream("simpleNodes.md");
        assertNotNull( stream );
        val converted = processMarkdown(site, site.getHome(), Optional.empty(), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( converted );

        assertTrue(converted.contains("----\n1\n----\n2\n----\n3\n----\n4\n----"), "All forms of HRules should be supported" );
        /* only when Extensions.SMARTS is activated
        assertTrue( converted.contains("&hellip;") );
        assertTrue( converted.contains("&ndash;") );
        assertTrue( converted.contains("&mdash;") );
        */
        assertTrue( converted.contains("Forcing a line-break\nNext line in the list") );
        assertTrue( converted.contains("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") );
    }

    @Test
    public void shouldCreateSpecificNoticeBlock() throws IOException {
        val parentPageTitle = Optional.of("Test Macro");

        final InputStream stream = getClass().getClassLoader().getResourceAsStream("createSpecificNoticeBlock.md");
        assertNotNull( stream );
        val converted = processMarkdown(site, site.getHome(), Optional.empty(), IOUtils.toString(stream), parentPageTitle);
        assertNotNull( converted );

        assertTrue( converted.contains("{info:title=About me}\n") );
        assertFalse(converted.contains("{note:title=}\n"), "Should not generate unneeded param 'title'" );
        assertTrue( converted.contains("{tip:title=About you}\n") );
        assertTrue( converted.contains("bq. test a simple blockquote") );
        assertTrue( converted.contains("{quote}\n") );
        assertTrue( converted.contains("* one\n* two") );
        assertTrue( converted.contains("a *strong* and _pure_ feeling") );

    }

    
}