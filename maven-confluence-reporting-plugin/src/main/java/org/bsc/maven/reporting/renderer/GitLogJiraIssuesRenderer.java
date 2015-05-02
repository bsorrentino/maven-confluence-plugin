package org.bsc.maven.reporting.renderer;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Scm;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink) {
        super(sink);
    }

    @Override
    public String getTitle() {
        return "GitLogJiraIssuesRendererTitle";  //todo implement getTitle in GitLogJiraIssuesRenderer
    }

    @Override
    protected void renderBody() {


            startSection( getTitle() );

            sink.rawText("{jira:TST-66458}");

            endSection();

            return;


    }

//    public void render()
//    {
//
//
//        sink.body();
//        renderBody();
//        sink.body_();
//
//        sink.flush();
//
//        sink.close();
//    }
}
