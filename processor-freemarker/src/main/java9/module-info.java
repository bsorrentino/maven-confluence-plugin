module maven.confluence.prepocessor.freemarker {

    requires freemarker;
    requires maven.confluence.core;

    provides org.bsc.preprocessor.SiteProcessorService
            with org.bsc.preprocessor.freemarker.FreemarkerPreprocessorImpl;
}
