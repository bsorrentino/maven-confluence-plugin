package org.bsc.confluence.preprocessor;

import java.util.Map;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2019
 */
public interface Preprocessor {

    /**
     * Singleton instance of the preprocessor to be used in all places
     */
    static final Preprocessor INSTANCE = new PreprocessorImpl();

    /**
     * Handles preprocessing of the input using Freemarker library
     * Variables are added to the freemarker model
     */
    String preprocess(String input, Map<String, Object> variables);

}
