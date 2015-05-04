package com.github.danielflower.mavenplugins.gitlog;

import com.github.danielflower.mavenplugins.gitlog.filters.CommitFilter;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitLogHelper {

    private RevWalk walk;
    private Map<String, List<RevTag>> commitIDToTagsMap;
    private final List<CommitFilter> commitFilters;
    private Set<String> tagNames;
    private final Log log;
    private Repository repository;

    public GitLogHelper(List<CommitFilter> commitFilters, Log log) {
        this.commitFilters = (commitFilters == null) ? new ArrayList<CommitFilter>() : commitFilters;
        this.log = log;
    }

    public GitLogHelper(Log log) {
        this.commitFilters = Defaults.DEFAULT_COMMIT_FILTERS;
        this.log = log;
    }

    public void openRepository() throws IOException, NoGitRepositoryException {
        log.debug("About to open git repository.");
        try {
            repository = new RepositoryBuilder().findGitDir().build();
        } catch (IllegalArgumentException iae) {
            throw new NoGitRepositoryException();
        }
        log.debug("Opened " + repository + ". About to load the commits.");
        walk = createWalk(repository);
        log.debug("Loaded commits. about to load the tags.");
        tagNames = repository.getTags().keySet();
        log.debug("Loaded tag names: " + tagNames);
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


    public String generateIssuesReport(Date sinceDate) {
        Set<String> jiraIssues = extractJiraIssues(walk, "ROO-\\d+", sinceDate);
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


    @Deprecated
    public void generate(String reportTitle) throws IOException {
        generate(reportTitle, new Date(0l));
    }

    @Deprecated
    public void generate(String reportTitle, Date includeCommitsAfter) throws IOException {


        long dateInSecondsSinceEpoch = includeCommitsAfter.getTime() / 1000;
        for (RevCommit commit : walk) {
            int commitTimeInSecondsSinceEpoch = commit.getCommitTime();
            if (dateInSecondsSinceEpoch < commitTimeInSecondsSinceEpoch) {
                List<RevTag> revTags = commitIDToTagsMap.get(commit.name());

                if (revTags != null) {
                    for (RevTag revTag : revTags) {
//							renderer.renderTag(revTag);
                    }
                }
                if (show(commit)) {
//					for (ChangeLogRenderer renderer : renderers) {
//						renderer.renderCommit(commit);
//					}
                }
            }
        }
        walk.dispose();


//		for (ChangeLogRenderer renderer : renderers) {
//			renderer.renderFooter();
//			renderer.close();
//		}
    }

    private boolean show(RevCommit commit) {
        for (CommitFilter commitFilter : commitFilters) {
            if (!commitFilter.renderCommit(commit)) {
                log.debug("Commit filtered out by " + commitFilter.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }

    protected static RevWalk createWalk(Repository repository) throws IOException {
        RevWalk walk = new RevWalk(repository);
        ObjectId head = repository.resolve("HEAD");
        if (head != null) {
            // if head is null, it means there are no commits in the repository.  The walk will be empty.
            RevCommit mostRecentCommit = walk.parseCommit(head);
            walk.markStart(mostRecentCommit);
        }
        return walk;
    }


    private Map<String, List<RevTag>> createCommitIDToTagsMap(Repository repository, RevWalk revWalk) throws IOException {
        Map<String, Ref> allTags = repository.getTags();

        Map<String, List<RevTag>> revTags = new HashMap<String, List<RevTag>>();

        for (Ref ref : allTags.values()) {
            try {
                RevTag revTag = revWalk.parseTag(ref.getObjectId());
                String commitID = revTag.getObject().getId().getName();
                if (!revTags.containsKey(commitID)) {
                    revTags.put(commitID, new ArrayList<RevTag>());
                }
                revTags.get(commitID).add(revTag);
            } catch (IncorrectObjectTypeException e) {
                log.debug("Light-weight tags not supported. Skipping " + ref.getName());
            }
        }

        return revTags;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }
}
