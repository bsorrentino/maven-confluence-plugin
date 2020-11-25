package org.bsc.reporting.renderer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util methods for work with GIT repo
 * @author ar
 * @since Date: 04.05.2015
 */
public class GitLogUtil {


    /**
     *
     * @return
     * @throws Exception
     */
    public static Repository openRepository() throws Exception {
        return new RepositoryBuilder().findGitDir().build();
    }

    /**
     *
     * @param repository
     * @param versionTagNamePattern
     * @return
     */
    public static Set<String> loadVersionTagList(Repository repository, String versionTagNamePattern) {
        Set<String> versionTagList = new HashSet<String>();
        if (versionTagNamePattern != null) {
            versionTagList = new HashSet<String>();
            for (String tagName : repository.getTags().keySet()) {
                if (tagName.matches(versionTagNamePattern)) {
                    versionTagList.add(tagName);
                }
            }
        } else {
            versionTagList = repository.getTags().keySet();
        }
        return versionTagList;
    }

    /**
     *
     * @param repository
     * @param tagName
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    protected static RevCommit resolveCommitIdByTagName(Repository repository, String tagName) throws IOException, GitAPIException {
        if (tagName == null || tagName.isEmpty()) return null;
        
        RevCommit revCommit = null;
        
        final Map<String, Ref> tagMap = repository.getTags();
        
        final Ref ref = tagMap.get(tagName);
        
        if (ref != null) {
            
            try(RevWalk walk = new RevWalk(repository)) {
                //some reduce memory effors as described in jgit user guide
                walk.setRetainBody(false);
                
                ObjectId from = repository.resolve("refs/heads/master");
                
                if (from == null) {
                    try(Git git = new Git(repository)) {
                        final String lastTagName = git.describe().call();
                        from = repository.resolve("refs/tags/" + lastTagName);
                    }
                }
    
                if (from==null){
                    throw new IllegalStateException("cannot determinate start commit");
                }
                
                ObjectId to = repository.resolve("refs/remotes/origin/master");

                walk.markStart(walk.parseCommit(from));
                walk.markUninteresting(walk.parseCommit(to));

                RevObject revObject = walk.parseAny(ref.getObjectId());
                if (revObject != null) {
                    revCommit = walk.parseCommit(revObject.getId());

                }

            }

        }

        return revCommit;

    }

    public static Set<String> extractJiraIssues(Repository repository,
                                                String sinceTagName,
                                                String untilTagName,
                                                String pattern) throws IOException, GitAPIException {
        try(Git git = new Git(repository)) {
            final RevCommit startCommitId = resolveCommitIdByTagName(repository, sinceTagName);
            
            if (startCommitId == null) {
                throw new IOException("cannot resolveCommitIdByTagName by  " + sinceTagName);
            }
            
            ObjectId endCommitId = resolveCommitIdByTagName(repository, untilTagName);
            
            if (endCommitId == null) {
                endCommitId = repository.resolve(Constants.HEAD);
            }
            
            final Iterable<RevCommit> commits = git.log().addRange(startCommitId, endCommitId).call();
    
            return extractJiraIssues(pattern, commits);
        }
    }


    public static LinkedHashMap<String, Set<String>> extractJiraIssuesByVersion(Repository repository,
                                                                          List<String> versionTagList,
                                                                          String pattern) throws IOException, GitAPIException {

        LinkedHashMap<String, Set<String>> linkedHashMap = new LinkedHashMap<String, Set<String>>();


        int lenght = versionTagList.size();
        for (int i = 0; i < lenght; i++) {
            String sinceTagName = versionTagList.get(i);
            String untilTagName = i + 1 > lenght - 1 ? null : versionTagList.get(i + 1);
            linkedHashMap.put(versionTagList.get(i), extractJiraIssues(repository, sinceTagName, untilTagName, pattern));
        }
        return linkedHashMap;
    }


    private static Set<String> extractJiraIssues(String pattern, Iterable<RevCommit> commits) {
        HashSet<String> jiraIssues = new LinkedHashSet<>();  //preserve insertion order
        for (RevCommit commit : commits) {
            jiraIssues.addAll(extractJiraIssuesFromString(commit.getFullMessage(), pattern));
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
}
