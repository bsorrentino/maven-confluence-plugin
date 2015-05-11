package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import com.github.qwazer.mavenplugins.gitlog.GitLogUtil;
import com.github.qwazer.mavenplugins.gitlog.VersionUtil;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, String gitLogSinceTagName, String gitLogUntilTagName, List<String> jiraProjectKeyList,
                                    String currentVersion, CalculateRuleForSinceTagName calculateRuleForSinceTagName,
                                    String gitLogTagNamesPattern, Log log) {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
        this.gitLogUntilTagName = gitLogUntilTagName;
        this.currentVersion = currentVersion;
        this.calculateRuleForSinceTagName = calculateRuleForSinceTagName;
        this.jiraProjectKeyList = jiraProjectKeyList;
        this.gitLogTagNamesPattern = gitLogTagNamesPattern;
        this.log = log;
    }

    public static String formatJiraIssuesToString(Collection<String> jiraIssues) {

        StringBuilder output = new StringBuilder(100);

        for (String jiraIssueKey : jiraIssues) {
            output.append("{jira:" + jiraIssueKey + "}\\\\\n");
        }
        return output.toString();

    }

    @Override
    public String getTitle() {
        return "GitLogJiraIssuesRendererTitle";  //todo implement getTitle in GitLogJiraIssuesRenderer
    }

    @Override
    protected void renderBody() {


        //    startSection( getTitle() );

        //GitLogUtil gitLogUtil = new GitLogUtil(log);
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
            log.debug(String.format(
                    "Try to calculate tag name part by currentVersion %s and rule %s"
                    , currentVersion, calculateRuleForSinceTagName));
            overrideGitLogSinceTagNameIfNeeded(versionTagList);
            log.info("Nearest version tag name found: " + gitLogSinceTagName);
        }

        if (gitLogSinceTagName==null || gitLogSinceTagName.isEmpty()) {
            log.warn("gitLogSinceTagName is not specified and cannot be calculated via calculateRuleForSinceTagName");
        }

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList != null && !jiraProjectKeyList.isEmpty()) {

            String patternKeys = "";
            for (String pkey : jiraProjectKeyList) {
                patternKeys += pkey + "|";
            }
            patternKeys = patternKeys.substring(0, patternKeys.length() - 1);
            pattern = "(" + patternKeys + ")-\\d+";

        }


        Set<String> jiraIssues = null;
        try {
            jiraIssues = GitLogUtil.extractJiraIssues(repository, gitLogSinceTagName, gitLogUntilTagName, pattern);
        } catch (Exception e) {
            log.error("cannot extractJiraIssues with error " + e);
        }
        log.info(String.format("Found %d jira issues", jiraIssues.size()));
        log.debug(": " + jiraIssues);

        String report = formatJiraIssuesToString(jiraIssues);

        sink.rawText(report);

    }

    private void overrideGitLogSinceTagNameIfNeeded(Set<String> versionTagList) {
            String tagNamePart = VersionUtil.calculateVersionTagNamePart(currentVersion, calculateRuleForSinceTagName);
            log.info(String.format("Calculated tag name part is %s", tagNamePart));
            String nearestVersionTagName = VersionUtil.findNearestVersionTagsBefore(versionTagList, tagNamePart);
            gitLogSinceTagName = nearestVersionTagName;

    }

    public String getGitLogSinceTagName() {
        return gitLogSinceTagName;
    }
}
