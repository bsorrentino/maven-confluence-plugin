module maven.confluence.prepocessor.freemarker {

    requires freemarker;
    requires maven.confluence.core;


    provides org.bsc.confluence.preprocessor.SiteProcessorService
            with org.bsc.confluence.preprocessor.freemarker.FreemarkerPreprocessorImpl;
}
