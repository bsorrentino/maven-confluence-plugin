package org.bsc.confluence.preprocessor;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2019
 * 
 * updated by bsorrentino
 */
public interface PreprocessorService {


    static Optional<PreprocessorService> getDefaultPreprocessorService() {
        final ServiceLoader<PreprocessorService> loader = ServiceLoader.load(PreprocessorService.class);
        
        final Iterable<PreprocessorService> iterable = () -> loader.iterator();
        
        return StreamSupport.stream(iterable.spliterator(), false)
                .findFirst();
    }
    
    /**
     * name of Preprocessor service
     *
     */
    String getName();
    
    /**
     * Handles preprocessing of the input using a markup library
     * 
     * Variables are added to the markup model
     */
    CompletableFuture<String> preprocess(String input, Map<String, Object> variables);

}
