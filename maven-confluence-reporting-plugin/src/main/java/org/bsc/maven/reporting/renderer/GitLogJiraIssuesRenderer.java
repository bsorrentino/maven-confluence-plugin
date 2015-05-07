package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.GitLogHelper;
import com.github.qwazer.mavenplugins.gitlog.CalculateRuleForSinceTagName;
import com.github.qwazer.mavenplugins.gitlog.VersionUtil;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    private final Log log;
    private String gitLogSinceTagName;
    private CalculateRuleForSinceTagName calculateRuleForSinceTagName;
    private String currentVersion;
    private List<String> jiraProjectKeyList;

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, String gitLogSinceTagName, List<String> jiraProjectKeyList, String currentVersion, CalculateRuleForSinceTagName calculateRuleForSinceTagName, Log log) {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
        this.currentVersion = currentVersion;
        this.calculateRuleForSinceTagName = calculateRuleForSinceTagName;
        this.jiraProjectKeyList = jiraProjectKeyList;
        this.log = log;
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
            gitLogHelper.openRepository();
        } catch (Exception e) {
            log.warn("cannot open git repository with message " + e.getMessage());
        }

        Date sinceDate = new Date(0L);
        try {

            if (!CalculateRuleForSinceTagName.NO_RULE.equals(calculateRuleForSinceTagName)) {
                log.debug(String.format(
                        "Try to calculated tag name part by currentVersion %s and sinceVersion %s"
                        , currentVersion, calculateRuleForSinceTagName));
                String tagNamePart = VersionUtil.calculateSinceVersionTagNamePart(currentVersion, calculateRuleForSinceTagName);
                log.info(String.format("Calculated tag name part %s", tagNamePart));
                Collection<String> tagNames = gitLogHelper.getTagNames();
                List<String> tagNamesOfVersions = VersionUtil.calculateTagNamesOfVersions(tagNames, currentVersion, calculateRuleForSinceTagName);

                for (String tagNameWithVersion : tagNamesOfVersions) {
                    Date date = gitLogHelper.extractDateOfCommitWithTagName(tagNameWithVersion);
                    if (date.after(sinceDate)) {
                        sinceDate = date;
                    }
                }


            } else {
                sinceDate = gitLogHelper.extractDateOfCommitWithTagName(gitLogSinceTagName);
            }
        } catch (IOException e) {
            log.warn("cannot extract date of commit with tag name ", e);
        }

        log.debug("Date of commit with tagName " + gitLogSinceTagName + " is " + sinceDate);

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList !=null && !jiraProjectKeyList.isEmpty()) {

            String patternKeys = "";
            for (String pkey : jiraProjectKeyList){
                patternKeys += pkey + "|";
            }
            patternKeys = patternKeys.substring(0, patternKeys.length()-1);
            pattern = "(" + patternKeys +")-\\d+";

        }
        log.info("Extract issues from gitlog since " + sinceDate + " by pattern " + pattern);
        String report = gitLogHelper.generateIssuesReport(sinceDate, pattern);

        sink.rawText(report);

        //    endSection();

        return;


    }


}
