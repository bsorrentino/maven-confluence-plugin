package org.bsc.maven.reporting.renderer;

import com.github.qwazer.mavenplugins.gitlog.GitLogHelper;
import com.github.qwazer.mavenplugins.gitlog.SinceVersion;
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
    private SinceVersion sinceVersion;
    private String currentVersion;
    private List<String> jiraProjectKeyList;

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, String gitLogSinceTagName, List<String> jiraProjectKeyList, String currentVersion, SinceVersion sinceVersion, Log log) {
        super(sink);
        this.gitLogSinceTagName = gitLogSinceTagName;
        this.currentVersion = currentVersion;
        this.sinceVersion = sinceVersion;
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
            log.warn("cannot open git repository ", e);
        }

        Date sinceDate = new Date(0L);
        try {

            if (!SinceVersion.SINCE_BEGINNING.equals(sinceVersion)) {
                String tagNamePart = VersionUtil.calculateSinceVersionTagNamePart(currentVersion, sinceVersion);
                Collection<String> tagNames = gitLogHelper.getTagNames();
                List<String> tagNamesOfVersions = VersionUtil.calculateTagNamesOfVersions(tagNames, currentVersion, sinceVersion);

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

        String pattern = "([A-Za-z]+)-\\d+";
        if (jiraProjectKeyList !=null && !jiraProjectKeyList.isEmpty()) {

            String patternKeys = "";
            for (String pkey : jiraProjectKeyList){
                patternKeys += pkey + "|";
            }
            patternKeys = patternKeys.substring(0, patternKeys.length()-1);
            pattern = "(" + patternKeys +")-\\d+";

        }

        String report = gitLogHelper.generateIssuesReport(sinceDate, pattern);

        sink.rawText(report);

        //    endSection();

        return;


    }


}
