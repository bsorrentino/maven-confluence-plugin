package org.bsc.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import org.apache.maven.plugin.testing.SilentLog;
import org.junit.Test;

import java.util.List;

import static com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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


    private static String calculateSinceTagName(GitLogJiraIssuesRenderer renderer) {
        List<String> list = asList("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0");
        renderer.overrideGitLogSinceTagNameIfNeeded(list);
        return renderer.getGitLogSinceTagName();
    }



    @Test
    public void testCalculateTagName_initial_release_Major_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0",  CURRENT_MAJOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_not_initial_release_Major_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.1",  CURRENT_MAJOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_not_initial_release2_Major_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.1.1",  CURRENT_MAJOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_hotfix_release_Major_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("11.1.11",  CURRENT_MAJOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("10.1.9", result);
    }

    @Test
    public void testCalculateTagName_hotfix_release2_Major_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("11.0.2",  CURRENT_MAJOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("10.1.9", result);
    }



    @Test
    public void testCalculateTagName_initial_release_Minor_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0",  CURRENT_MINOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_not_initial_release_Minor_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.1",  CURRENT_MINOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_hotfix_Minor_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("11.1.11",  CURRENT_MINOR_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.0.1", result);
    }




    @Test
    public void testCalculateTagName_initial_release_Latest_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.0", LATEST_RELEASE_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }

    @Test
    public void testCalculateTagName_not_initial_release_Latest_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("12.0.1",  LATEST_RELEASE_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("12.0.0", result);
    }

    @Test
    public void testCalculateTagName_hotfix_Latest_rule() throws Exception {
        GitLogJiraIssuesRenderer renderer = createRendererWithParams("11.1.11",  LATEST_RELEASE_VERSION);
        String result = calculateSinceTagName(renderer);
        assertEquals("11.1.10", result);
    }


}