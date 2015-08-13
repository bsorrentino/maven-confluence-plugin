package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import com.github.qwazer.mavenplugins.gitlog.GitLogUtil;
import com.github.qwazer.mavenplugins.gitlog.VersionUtil;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.eclipse.jgit.lib.Repository;

import java.util.*;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    private final Log log;
    private String gitLogSinceTagName;
    private String gitLogUntilTagName;
    private CalculateRuleForSinceTagName calculateRuleForSinceTagName;
    private String currentVersion;
    private String gitLogTagNamesPattern;
    private List<String> jiraProjectKeyList;
    private Boolean gitLogGroupByVersions;

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, String gitLogSinceTagName, String gitLogUntilTagName, List<String> jiraProjectKeyList,
                                    String currentVersion, CalculateRuleForSinceTagName calculateRuleForSinceTagName,
                                    String gitLogTagNamesPattern, Boolean gitLogGroupByVersions, Log log) {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
        this.gitLogUntilTagName = gitLogUntilTagName;
        this.currentVersion = currentVersion;
        this.calculateRuleForSinceTagName = calculateRuleForSinceTagName;
        this.jiraProjectKeyList = jiraProjectKeyList;
        this.gitLogTagNamesPattern = gitLogTagNamesPattern;
        this.gitLogGroupByVersions = gitLogGroupByVersions;
        this.log = log;
    }

    public static String formatJiraIssuesToString(Collection<String> jiraIssues) {

        StringBuilder output = new StringBuilder(100);

        for (String jiraIssueKey : jiraIssues) {
            output.append("{jira:").append(jiraIssueKey).append("}\\\\\n");
        }
        return output.toString();

    }

    private static String formatJiraIssuesByVersionToString(LinkedHashMap<String, Set<String>> map) {
        StringBuilder output = new StringBuilder(100);
        //print the keys in reverse insertion order
        ListIterator<String> iter =
                new ArrayList<String>(map.keySet()).listIterator(map.size());

        while (iter.hasPrevious()) {
            String version = iter.previous();
            output.append(formatJiraIssuesToString(map.get(version)));
            boolean hasIssues = map.get(version).isEmpty();
            if (iter.hasPrevious()) {
                if (!hasIssues) {
                    output.append(" \\\\ ");
                }
                output.append(version).append(" \\\\ ");
            }
        }
        return output.toString();
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    protected void renderBody() {

        Repository repository = null;
        try {
            log.debug("Try to open git repository.");
            repository = GitLogUtil.openRepository();
        } catch (Exception e) {
            log.warn("cannot open git repository  with error " + e);
        }
        log.debug("Try to open load version tag list.");
        Set<String> versionTagList = GitLogUtil.loadVersionTagList(repository, gitLogTagNamesPattern);

        if (!CalculateRuleForSinceTagName.NO_RULE.equals(calculateRuleForSinceTagName)) {
            log.info(String.format(
                    "Try to calculate tag name part by currentVersion %s and rule %s"
                    , currentVersion, calculateRuleForSinceTagName));
            overrideGitLogSinceTagNameIfNeeded(versionTagList);
            log.info("Override gitLogSinceTagName by nearest version tag name found: " + gitLogSinceTagName);
        }

        if (gitLogSinceTagName==null || gitLogSinceTagName.isEmpty()) {
            log.warn("gitLogSinceTagName is not specified and cannot be calculated via calculateRuleForSinceTagName");
        }

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList != null && !jiraProjectKeyList.isEmpty()) {
            pattern = craetePatternFromJiraProjectKeyList();
        }


        String report="";
        if (gitLogGroupByVersions){
            LinkedHashMap<String,Set<String>> jiraIssuesByVersion = null;
            try {
                LinkedList<String> linkedList = VersionUtil.sortAndFilter(versionTagList,
                        gitLogSinceTagName,
                        gitLogUntilTagName );

                jiraIssuesByVersion = GitLogUtil.extractJiraIssuesByVersion(repository,
                        linkedList,
                        pattern);
            } catch (Exception e) {
                log.error("cannot extractJiraIssues with error " + e, e);
            }

            if (jiraIssuesByVersion!=null) {
                log.info(String.format("Found %d version tags", jiraIssuesByVersion.keySet().size()));
                log.debug(": " + jiraIssuesByVersion);
                report = formatJiraIssuesByVersionToString(jiraIssuesByVersion);
            }
            else {
                report = "NO JIRAISSUESBYVERSION FOUND";
            }
        }
        else {
            Set<String> jiraIssues = null;
            try {
                jiraIssues = GitLogUtil.extractJiraIssues(repository, gitLogSinceTagName, gitLogUntilTagName, pattern);
            } catch (Exception e) {
                log.error("cannot extractJiraIssues with error " + e, e);
            }
            log.info(String.format("Found %d JIRA issues", jiraIssues.size()));
            log.debug(": " + jiraIssues);
            report = formatJiraIssuesToString(jiraIssues);
        }

        sink.rawText(report);

    }


    private String craetePatternFromJiraProjectKeyList() {
        String pattern;
        String patternKeys = "";
        for (String pkey : jiraProjectKeyList) {
            patternKeys += pkey + "|";
        }
        patternKeys = patternKeys.substring(0, patternKeys.length() - 1);
        pattern = "(" + patternKeys + ")-\\d+";
        return pattern;
    }

    protected void overrideGitLogSinceTagNameIfNeeded(Collection<String> versionTagList) {
        String tagNamePart = VersionUtil.calculateVersionTagNamePart(currentVersion, calculateRuleForSinceTagName);
        log.info(String.format("Calculated tag name part is %s", tagNamePart));
        versionTagList = VersionUtil.removeTagWithVersion(versionTagList, currentVersion);
        String nearestVersionTagName = VersionUtil.findNearestVersionTagsBefore(versionTagList, tagNamePart);
        gitLogSinceTagName = nearestVersionTagName;

    }

    public String getGitLogSinceTagName() {
        return gitLogSinceTagName;
    }
}
