package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.SilentLog;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author ar
 * @since Date: 11.07.2015
 */
public class GitLogJiraIssuesRendererTest {



    public static GitLogJiraIssuesRenderer createRendererWithParams(String currentVersion,
                                                                    CalculateRuleForSinceTagName rule) {

        return new GitLogJiraIssuesRenderer(
                null,
                null,
                null,
                null,
                currentVersion,
                rule,
                null,
                null,
                new SilentLog());

    }


    @Test
    public void testOverrideGitLogSinceTagNameIfNeeded1() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0",  CURRENT_MAJOR_VERSION);
        List<String> list = asList("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0");
        renderer.overrideGitLogSinceTagNameIfNeeded(list);
        String result = renderer.getGitLogSinceTagName();
        assertEquals("11.1.10", result);
    }

    @Test
    public void testOverrideGitLogSinceTagNameIfNeeded2() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0",  CURRENT_MINOR_VERSION);
        List<String> list = asList("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0");
        renderer.overrideGitLogSinceTagNameIfNeeded(list);
        String result = renderer.getGitLogSinceTagName();
        assertEquals("11.1.10", result);
    }

    @Test
    public void testOverrideGitLogSinceTagNameIfNeeded3() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0",  LATEST_RELEASE_VERSION);
        List<String> list = asList("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0");
        renderer.overrideGitLogSinceTagNameIfNeeded(list);
        String result = renderer.getGitLogSinceTagName();
        assertEquals("11.1.10", result);
    }

    @Test
    public void testOverrideGitLogSinceTagNameIfNeeded4() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0", LATEST_RELEASE_VERSION);
        List<String> list = asList("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0", "12.1.1");
        renderer.overrideGitLogSinceTagNameIfNeeded(list);
        String result = renderer.getGitLogSinceTagName();
        assertEquals("11.1.10", result);
    }


}