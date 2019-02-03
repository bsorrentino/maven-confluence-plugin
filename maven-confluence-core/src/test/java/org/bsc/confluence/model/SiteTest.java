package org.bsc.confluence.model;

import static org.bsc.confluence.model.SiteProcessor.processMarkdown;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNull;
import org.junit.Test;

public class SiteTest {

    @Test
    public void validateName() {
        
        final Pattern p = Pattern.compile("^(.*)\\s+$|^\\s+(.*)");
        assertThat( p.matcher("name").matches(), is(false));
        assertThat( p.matcher("name ").matches(), is(true));
        assertThat( p.matcher("name  ").matches(), is(true));
        assertThat( p.matcher(" name").matches(), is(true));
        assertThat( p.matcher("  name").matches(), is(true));
        assertThat( p.matcher(" name ").matches(), is(true));
        assertThat( p.matcher(" name  ").matches(), is(true));
    }
    
    @Test
    public void shouldSupportReferenceNode() throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("withRefLink.md");
        assertThat( stream, IsNull.notNullValue());
        final InputStream inputStream = processMarkdown(stream, "Test");
        assertThat( inputStream, IsNull.notNullValue());
        final String converted = IOUtils.toString(inputStream);

        assertThat(converted, containsString("[rel|Test - relativeagain]"));
        assertThat(converted, containsString("[more complex google|http://google.com|Other google]"));
        assertThat(converted, containsString("[google|http://google.com]"));
    }

    @Test
    public void shouldSupportImgRefLink() throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("withImgRefLink.md");
        assertThat( stream, IsNull.notNullValue());
        final InputStream inputStream = processMarkdown(stream, "Test IMG");
        assertThat( inputStream, IsNull.notNullValue());
        final String converted = IOUtils.toString(inputStream);

        assertThat(converted, containsString("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon\"|title=\"My conf-icon\"!"));
        assertThat(converted, containsString("!conf-icon-64.png|alt=\"conf-icon\"|title=\"My conf-icon\"!"));
        assertThat(converted, containsString("!conf-icon-64.png|alt=\"conf-icon\"!"));
        assertThat(converted, containsString("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon-y\"|title=\"My conf-icon\"!"));
        assertThat(converted, containsString("!http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon-y1\"!"));
        assertThat(converted, containsString("!conf-icon-64.png|alt=\"conf-icon-y2\"!"));
        assertThat(converted, containsString("!conf-icon-64.png|alt=\"conf-icon-none\"!"));
    }

    @Test
    public void shouldSupportSimpleNode() throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("simpleNodes.md");
        assertThat( stream, IsNull.notNullValue());
        final InputStream inputStream = processMarkdown(stream, "Test");
        assertThat( inputStream, IsNull.notNullValue());
        final String converted = IOUtils.toString(inputStream);

        assertThat("All forms of HRules should be supported", converted, containsString("----\n1\n----\n2\n----\n3\n----\n4\n----"));
        /* only when Extensions.SMARTS is activated
        assertThat(converted, containsString("&hellip;"));
        assertThat(converted, containsString("&ndash;"));
        assertThat(converted, containsString("&mdash;"));
        */
        assertThat(converted, containsString("Forcing a line-break\nNext line in the list"));
        assertThat(converted, containsString("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
    }
    
    @Test
    public void shouldCreateSpecificNoticeBlock() throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("createSpecificNoticeBlock.md");
        assertThat( stream, IsNull.notNullValue());
        final InputStream inputStream = processMarkdown(stream, "Test Macro");
        assertThat( inputStream, IsNull.notNullValue());
        final String converted = IOUtils.toString(inputStream);

        assertThat(converted, containsString("{info:title=About me}\n"));
        assertThat("Should not generate unneeded param 'title'", converted, not(containsString("{note:title=}\n")));
        assertThat(converted, containsString("{tip:title=About you}\n"));
        assertThat(converted, containsString("bq. test a simple blockquote"));
        assertThat(converted, containsString("{quote}\n"));
        assertThat(converted, containsString("* one\n* two"));
        assertThat(converted, containsString("a *strong* and _pure_ feeling"));

    }
    
    
}