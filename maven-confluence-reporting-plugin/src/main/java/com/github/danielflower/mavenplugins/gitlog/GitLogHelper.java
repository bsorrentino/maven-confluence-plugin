package com.github.danielflower.mavenplugins.gitlog;

import com.github.danielflower.mavenplugins.gitlog.filters.CommitFilter;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
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
	private final Log log;
	private Repository repository;

	public GitLogHelper(List<CommitFilter> commitFilters, Log log) {
		this.commitFilters = (commitFilters == null) ? new ArrayList<CommitFilter>() : commitFilters;
		this.log = log;
	}

	public GitLogHelper(Log log) {
		this.commitFilters =  Defaults.DEFAULT_COMMIT_FILTERS;
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
		commitIDToTagsMap = createCommitIDToTagsMap(repository, walk);
		log.debug("Loaded tag map: " + commitIDToTagsMap);
	}


    public String generateIssuesReport(){
        Set<String> jiraIssues = extractJiraIssues(walk, "ROO-\\d+");
        return formatJiraIssuesToString(jiraIssues);
    }

    public String formatJiraIssuesToString(Collection<String> jiraIssues){

        String res = "";

        for (String jiraIssueKey : jiraIssues){
            res += "{jira:" + jiraIssueKey + "}\n";
        }
        return res;

    }


    public static Set<String> extractJiraIssues(RevWalk revWalk, String jiraIssuePattern){
        HashSet jiraIssues = new HashSet();

        for (RevCommit commit : revWalk){
             jiraIssues.addAll(extractJiraIssuesFromString(commit.getFullMessage(), jiraIssuePattern));
        }

        return jiraIssues;

    }


    protected static List<String> extractJiraIssuesFromString(String s, String jiraIssuePattern) {

        Pattern p = Pattern.compile(jiraIssuePattern);
        Matcher m = p.matcher(s);
        List<String> list = new ArrayList<String>();
        while (m.find()){
           list.add(m.group(0));
        }
        return list;

    }

    public String generateIssuesReport(Date sinceDateTime){
        String result = null;

		for (String s : repository.getTags().keySet()){
			result += s + "\n";

		}

//        for (RevCommit commit : walk){
//            result += commit.getName() + " | " + commit.getFullMessage() + "\n";
//        }
        return result;
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


}
