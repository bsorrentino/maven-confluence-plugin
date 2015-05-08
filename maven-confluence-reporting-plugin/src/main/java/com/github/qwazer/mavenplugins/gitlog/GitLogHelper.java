package com.github.qwazer.mavenplugins.gitlog;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitLogHelper {

    private RevWalk walk;
    private Set<String> versionTagList;
    private final Log log;
    private Repository repository;



    public GitLogHelper(Log log) {
        this.log = log;
    }

    public void openRepositoryAndInitVersionTagList(String gitLogTagNamesPattern) throws IOException, NoGitRepositoryException {
        log.debug("Try to open git repository.");
        try {
            repository = new RepositoryBuilder().findGitDir().build();
        } catch (IllegalArgumentException iae) {
            throw new NoGitRepositoryException();
        }
        log.debug("Opened " + repository + ". Try to load the commits.");
        walk = createWalk(repository);
        log.debug("Loaded commits. try to load version tags.");
        versionTagList = new HashSet<String>();

        for (String tagName : repository.getTags().keySet()){
            if (tagName.matches(gitLogTagNamesPattern)){
                versionTagList.add(tagName);
            }
        }

        log.debug("Loaded version tag names: " + versionTagList);
    }


    public Date extractDateOfCommitWithTagName(String tagName) throws IOException {
        return extractDateOfCommitWithTagName(repository, tagName);
    }


    public static Date extractDateOfCommitWithTagName(Repository repository, String tagName) throws IOException {
        Date result = new Date(0L);
        Map<String, Ref> tagMap = repository.getTags();
        Ref ref = tagMap.get(tagName);
        if (ref != null) {
            RevWalk revWalk = new RevWalk(repository);
            try {

                RevObject revObject = revWalk.parseAny(ref.getObjectId());
                if (revObject!=null ) {
                    RevCommit revCommit = revWalk.parseCommit(revObject.getId());
                    result = new Date((long) revCommit.getCommitTime() * 1000);
                }

            } finally {
                revWalk.release();
            }

        }


        return result;

    }


    public String generateIssuesReport(Date sinceDate, String pattern) {
        Set<String> jiraIssues = extractJiraIssues(walk, pattern, sinceDate);
        return formatJiraIssuesToString(jiraIssues);
    }

    public String formatJiraIssuesToString(Collection<String> jiraIssues) {

        StringBuilder output = new StringBuilder(100);

        for (String jiraIssueKey : jiraIssues) {
            output.append("{jira:" + jiraIssueKey + "}\\\\\n");
        }
        return output.toString();

    }


    public static Set<String> extractJiraIssues(RevWalk revWalk, String jiraIssuePattern, Date sinceTime) {
        HashSet jiraIssues = new HashSet();
        final long sinceTimeInSec = sinceTime.getTime() / 1000;
        for (RevCommit commit : revWalk) {
            if (commit.getCommitTime() >= sinceTimeInSec) {
                jiraIssues.addAll(extractJiraIssuesFromString(commit.getFullMessage(), jiraIssuePattern));
            }
        }

        return jiraIssues;

    }


    protected static List<String> extractJiraIssuesFromString(String s, String jiraIssuePattern) {

        Pattern p = Pattern.compile(jiraIssuePattern);
        Matcher m = p.matcher(s);
        List<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group(0));
        }
        return list;

    }




    protected static RevWalk createWalk(Repository repository) throws IOException {
        RevWalk walk = new RevWalk(repository);
        ObjectId head = repository.resolve("HEAD");
        if (head != null) {
            // if head is null, it means there are no commits in the repository.  The walk will be empty.
            RevCommit mostRecentCommit = walk.parseCommit(head);
            walk.markStart(mostRecentCommit);
            //todo  http://wiki.eclipse.org/JGit/User_Guide#Reducing_memory_usage_with_RevWalk
        }
        return walk;
    }


    public Set<String> getVersionTagList() {
        return versionTagList;
    }
}
