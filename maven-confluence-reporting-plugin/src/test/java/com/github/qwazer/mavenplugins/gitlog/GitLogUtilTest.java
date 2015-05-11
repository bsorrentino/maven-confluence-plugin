package com.github.qwazer.mavenplugins.gitlog;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * @author ar
 * @since Date: 04.05.2015
 */
@Ignore
public class GitLogUtilTest {
    private static final String repoFile =
            "/home/ar/projects/github/maven-confluence-plugin/maven-confluence-reporting-plugin/src/it/spring-roo/pom.xml";

    Repository repository;
    String pattern = "([A-Za-z]+)-\\d+";

    @Before
    public void openRepo() throws IOException {

        repository = new RepositoryBuilder().findGitDir(new File(repoFile)).build();
        System.out.println("repository = " + repository);

    }

    @Test
    public void testExtactJiraIssues() throws Exception {
        Set<String> set = GitLogUtil.extractJiraIssues(repository, "1.2.5.RELEASE", null, pattern);
        System.out.println(set);

    }

    @Test
    public void testJiraIssuePattern() {
        String patternStr = "ROO-\\d+";
        List<String> list = GitLogUtil.extractJiraIssuesFromString("ROO-1", patternStr);
        assertThat(list.size(), is(1));
        assertThat(list, hasItem("ROO-1"));
    }

    @Test
    public void testJiraIssuePattern2() {
        String patternStr = "ROO-\\d+";
        List<String> list = GitLogUtil.extractJiraIssuesFromString("ROO-1 ROOO-1", patternStr);
        assertThat(list.size(), is(1));
        assertThat(list, hasItem("ROO-1"));
    }

    @Test
    public void testJiraIssuePattern3() {
        String patternStr = "ROO-\\d+";
        List<String> list = GitLogUtil.extractJiraIssuesFromString("ROO-1 asq ROO-154353", patternStr);
        assertThat(list.size(), is(2));
        assertThat(list, hasItem("ROO-1"));
        assertThat(list, hasItem("ROO-154353"));
    }

    @Test
    public void testJiraIssuePattern4() {
        String patternStr = "(ROO|ABC)-\\d+";
        List<String> list = GitLogUtil.extractJiraIssuesFromString("ROO-1 asq ABC-154353", patternStr);
        assertThat(list.size(), is(2));
        assertThat(list, hasItem("ROO-1"));
        assertThat(list, hasItem("ABC-154353"));
    }

    @Test
    public void testJiraIssuePattern5() {
        String patternStr = "([A-Za-z]+)-\\d+";
        List<String> list = GitLogUtil.extractJiraIssuesFromString("ROO-1 asq ABC-154353", patternStr);
        assertThat(list.size(), is(2));
        assertThat(list, hasItem("ROO-1"));
        assertThat(list, hasItem("ABC-154353"));
    }


    @Test
    public void testExtractJiraIssuesByVersion() throws Exception {
        List<String> list = asList("1.0.0.M1", "1.0.0.M2", "1.0.0.RC1", "1.0.0.RC2",
                "1.0.0.RC3", "1.0.0.RC4", "1.0.0.RELEASE",
                "1.0.1.RELEASE", "1.0.2.RELEASE", "1.1.0.M2",
                "1.1.0.M3", "1.1.0.RC1", "1.1.0.RELEASE", "1.1.1.RELEASE",
                "1.1.2.RELEASE", "1.1.3.RELEASE", "1.1.4.RELEASE", "1.1.5.RELEASE",
                "1.2.0.M1", "1.2.0.RC1", "1.2.0.RELEASE", "1.2.1.RELEASE", "1.2.2.RELEASE",
                "1.2.3.RELEASE", "1.2.4.RELEASE", "1.2.5.RELEASE", "1.3.0.RELEASE", "1.3.1.RC1",
                "1.3.1.RC2", "1.3.1.RELEASE");
     //   LinkedList linkedList =
        HashMap<String, Set<String>> map =  GitLogUtil.extractJiraIssuesByVersion(repository, list, pattern);
        assertEquals(list.size(), map.size());
//        System.out.println("map = " + map);

    }
}