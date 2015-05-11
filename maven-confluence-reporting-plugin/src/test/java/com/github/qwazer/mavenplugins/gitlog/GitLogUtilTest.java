package com.github.qwazer.mavenplugins.gitlog;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.exparity.hamcrest.date.DateMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.github.qwazer.mavenplugins.gitlog.GitLogUtil.fetchCommitsFromStartTagToEndTag;
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


}