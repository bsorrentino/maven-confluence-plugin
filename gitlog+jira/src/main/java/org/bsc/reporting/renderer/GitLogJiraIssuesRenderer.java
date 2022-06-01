package org.bsc.reporting.renderer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.eclipse.jgit.lib.Repository;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    private final Log log;
    private String gitLogSinceTagName;
    private final String gitLogUntilTagName;
    private final CalculateRuleForSinceTagName calculateRuleForSinceTagName;
    private final String currentVersion;
    private final String gitLogTagNamesPattern;
    private final List<String> jiraProjectKeyList;
    private final Boolean gitLogGroupByVersions;
    private final URL gitLogJiraInstanceBaseUrl;

    /**
     * Default constructor.
     *
     * @param sink                      the sink to use.
     */
    public GitLogJiraIssuesRenderer(
            Sink sink,
            String gitLogSinceTagName,
            String gitLogUntilTagName,
            List<String> jiraProjectKeyList,
            String currentVersion,
            CalculateRuleForSinceTagName calculateRuleForSinceTagName,
            String gitLogTagNamesPattern,
            Boolean gitLogGroupByVersions,
            URL gitLogJiraInstanceBaseUrl,
            Log log)
    {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
        this.gitLogUntilTagName = gitLogUntilTagName;
        this.currentVersion = currentVersion;
        this.calculateRuleForSinceTagName = calculateRuleForSinceTagName;
        this.jiraProjectKeyList = jiraProjectKeyList;
        this.gitLogTagNamesPattern = gitLogTagNamesPattern;
        this.gitLogGroupByVersions = gitLogGroupByVersions;
        this.gitLogJiraInstanceBaseUrl = gitLogJiraInstanceBaseUrl;
        this.log = log;
    }

    public String formatJiraIssuesToString(Collection<String> jiraIssues) {

        StringBuilder output = new StringBuilder(100);

        for (String jiraIssueKey : jiraIssues) {
            output.append("{jira:").append(jiraIssueKey);

            if (gitLogJiraInstanceBaseUrl != null) {
                output.append("|url=").append(gitLogJiraInstanceBaseUrl).append("/browse/").append(jiraIssueKey);
            }

            output.append("}\\\\\n");
        }
        return output.toString();

    }

    private String formatJiraIssuesByVersionToString(LinkedHashMap<String, Set<String>> map) {
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

        if (gitLogSinceTagName == null || gitLogSinceTagName.isEmpty()) {
            log.warn("gitLogSinceTagName is not specified and cannot be calculated via calculateRuleForSinceTagName");
        }

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList != null && !jiraProjectKeyList.isEmpty()) {
            pattern = craetePatternFromJiraProjectKeyList();
        }


        String report = "";
        if (gitLogGroupByVersions) {
            LinkedHashMap<String, Set<String>> jiraIssuesByVersion = null;
            try {
                LinkedList<String> linkedList = VersionUtil.sortAndFilter(versionTagList,
                        gitLogSinceTagName,
                        gitLogUntilTagName);

                jiraIssuesByVersion = GitLogUtil.extractJiraIssuesByVersion(repository,
                        linkedList,
                        pattern);
            } catch (Exception e) {
                log.error("cannot extractJiraIssues with error " + e, e);
            }

            if (jiraIssuesByVersion != null) {
                log.info(String.format("Found %d version tags", jiraIssuesByVersion.keySet().size()));
                log.debug(": " + jiraIssuesByVersion);
                report = formatJiraIssuesByVersionToString(jiraIssuesByVersion);
            } else {
                report = "NO_JIRA_ISSUES_BY_VERSION_FOUND";
            }
        } else {
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
        if (!calculateRuleForSinceTagName.equals(CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)) {
            versionTagList = VersionUtil.removeTagWithVersion(versionTagList, tagNamePart);
        }
        gitLogSinceTagName = VersionUtil.findNearestVersionTagsBefore(versionTagList, tagNamePart).orElse(null);

    }

    public String getGitLogSinceTagName() {
        return gitLogSinceTagName;
    }
}
