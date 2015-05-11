package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import com.github.qwazer.mavenplugins.gitlog.GitLogHelper;
import com.github.qwazer.mavenplugins.gitlog.VersionUtil;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    private final Log log;
    private String gitLogSinceTagName;
    private CalculateRuleForSinceTagName calculateRuleForSinceTagName;
    private String currentVersion;
    private String gitLogTagNamesPattern;
    private List<String> jiraProjectKeyList;

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, String gitLogSinceTagName, List<String> jiraProjectKeyList,
                                    String currentVersion, CalculateRuleForSinceTagName calculateRuleForSinceTagName,
                                    String gitLogTagNamesPattern, Log log) {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
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

        GitLogHelper gitLogHelper = new GitLogHelper(log);
        try {
            gitLogHelper.openRepositoryAndInitVersionTagList(gitLogTagNamesPattern);
        } catch (Exception e) {
            log.warn("cannot open git repository and init VersionTagList with error " + e);
        }

        Date sinceDate = new Date(0L);


        if (!CalculateRuleForSinceTagName.NO_RULE.equals(calculateRuleForSinceTagName)) {
            log.debug(String.format(
                    "Try to calculate tag name part by currentVersion %s and rule %s"
                    , currentVersion, calculateRuleForSinceTagName));
            String tagNamePart = VersionUtil.calculateVersionTagNamePart(currentVersion, calculateRuleForSinceTagName);
            log.info(String.format("Calculated tag name part is %s", tagNamePart));
            Collection<String> versionTagList = gitLogHelper.getVersionTagList();
            String nearestVersionTagName = VersionUtil.findNearestVersionTagsBefore(versionTagList, tagNamePart);
            log.info("Nearest version tag name found: " + nearestVersionTagName);
            gitLogSinceTagName = nearestVersionTagName;
        }

        if (gitLogSinceTagName==null || gitLogSinceTagName.isEmpty()) {
            log.warn("gitLogSinceTagName is not specified and cannot be calculated via calculateRuleForSinceTagName");
        }
        else {
            try {
                sinceDate = gitLogHelper.extractDateOfCommitWithTagName(gitLogSinceTagName);
            } catch (IOException e) {
                log.warn("cannot extract date of commit with tag name ", e);
            }
        }


        log.debug("Date of commit with tagName " + gitLogSinceTagName + " is " + sinceDate);

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList != null && !jiraProjectKeyList.isEmpty()) {

            String patternKeys = "";
            for (String pkey : jiraProjectKeyList) {
                patternKeys += pkey + "|";
            }
            patternKeys = patternKeys.substring(0, patternKeys.length() - 1);
            pattern = "(" + patternKeys + ")-\\d+";

        }
        log.info("Extract issues from gitlog since " + sinceDate + " by pattern " + pattern);

        Set<String> jiraIssues = gitLogHelper.extractJiraIssues(sinceDate, pattern);
        log.info(String.format("Found %d jira issues", jiraIssues.size()));
        log.debug(": " + jiraIssues);

        String report = formatJiraIssuesToString(jiraIssues);

        sink.rawText(report);

    }

    public String getGitLogSinceTagName() {
        return gitLogSinceTagName;
    }
}
