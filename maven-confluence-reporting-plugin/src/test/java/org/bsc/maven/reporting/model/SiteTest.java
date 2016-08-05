package org.bsc.maven.reporting.model;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

public class SiteTest {

    @Test
    public void shouldSupportReferenceNode() throws IOException {
        InputStream stream = getClass().getResourceAsStream("withRefLink.md");
        InputStream inputStream = Site.processMarkdown(stream, "Test");
        String converted = IOUtils.toString(inputStream);

        assertThat(converted, containsString("[rel|Test - relativeagain]"));
        assertThat(converted, containsString("[more complex google|http://google.com|Other google]"));
        assertThat(converted, containsString("[google|http://google.com]"));
    }
}