package org.bsc.maven.reporting.renderer;

import com.github.danielflower.mavenplugins.gitlog.Generator;
import com.github.danielflower.mavenplugins.gitlog.NoGitRepositoryException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Scm;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;

/**
 * @author ar
 * @since Date: 01.05.2015
 */
public class GitLogJiraIssuesRenderer extends AbstractMavenReportRenderer {

    private final Log log;

    /**
     * Default constructor.
     *
     * @param sink the sink to use.
     */
    public GitLogJiraIssuesRenderer(Sink sink, Log log) {
        super(sink);
        this.log = log;
    }

    @Override
    public String getTitle() {
        return "GitLogJiraIssuesRendererTitle";  //todo implement getTitle in GitLogJiraIssuesRenderer
    }

    @Override
    protected void renderBody() {


        //    startSection( getTitle() );

        Generator generator  = new Generator(log);
        try {
            generator.openRepository();
        } catch (Exception e) {
            log.warn("cannot open git repository " , e);
        }

        String report = generator.generateIssuesReport();

        sink.rawText(report);

        //    endSection();

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
