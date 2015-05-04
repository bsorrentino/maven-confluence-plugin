package com.github.danielflower.mavenplugins.gitlog;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.danielflower.mavenplugins.gitlog.GitLogHelper.createWalk;
import static com.github.danielflower.mavenplugins.gitlog.GitLogHelper.extractJiraIssuesFromString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * @author ar
 * @since Date: 04.05.2015
 */
public class GitLogHelperTest {
    private static final String repoFile =
            "/home/ar/projects/github/maven-confluence-plugin/maven-confluence-reporting-plugin/src/it/spring-roo/pom.xml";


    @Test
    public void testOpenRepo() throws IOException {

        Repository repository = new RepositoryBuilder().findGitDir(new File(repoFile)).build();
        System.out.println("repository = " + repository);
        RevWalk walk = createWalk(repository);



    }

    @Test
    public void testJiraIssuePattern() {
        String patternStr = "ROO-\\d+";
        List<String> list = extractJiraIssuesFromString("ROO-1", patternStr);
        assertThat(list.size(), is(1));
        assertThat(list, hasItem("ROO-1"));
    }

    @Test
    public void testJiraIssuePattern2() {
        String patternStr = "ROO-\\d+";
        List<String> list = extractJiraIssuesFromString("ROO-1 ROOO-1", patternStr);
        assertThat(list.size(), is(1));
        assertThat(list, hasItem("ROO-1"));
    }

    @Test
    public void testJiraIssuePattern3() {
        String patternStr = "ROO-\\d+";
        List<String> list = extractJiraIssuesFromString("ROO-1 asq ROO-154353", patternStr);
        assertThat(list.size(), is(2));
        assertThat(list, hasItem("ROO-1"));
        assertThat(list, hasItem("ROO-154353"));
    }

    @Test
    public void testJiraIssuePattern4() {
        String patternStr = "(ROO|ABC)-\\d+";
        List<String> list = extractJiraIssuesFromString("ROO-1 asq ABC-154353", patternStr);
        assertThat(list.size(), is(2));
        assertThat(list, hasItem("ROO-1"));
        assertThat(list, hasItem("ABC-154353"));
    }


}