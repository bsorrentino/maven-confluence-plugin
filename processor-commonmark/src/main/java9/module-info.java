module confluence.markdown.processor.commonmark {
    requires maven.confluence.core;

    requires lombok;
    requires org.commonmark;
    requires org.commonmark.ext.gfm.strikethrough;
    requires org.commonmark.ext.gfm.tables;

    provides org.bsc.markdown.MarkdownProcessor
                    with org.bsc.markdown.commonmark.CommonmarkMarkdownProcessorImpl;
}