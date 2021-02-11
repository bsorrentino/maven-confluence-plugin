package org.bsc.preprocessor;

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
public interface SiteProcessorService {

	/**
	 * search the first suitable PreprocessorService published through SPI
	 * 
	 * @return
	 */
    static Optional<SiteProcessorService> getDefaultPreprocessorService() {
        final ServiceLoader<SiteProcessorService> loader = ServiceLoader.load(SiteProcessorService.class);
        
        final Iterable<SiteProcessorService> iterable = () -> loader.iterator();
        
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
    CompletableFuture<String> process(String input, Map<String, Object> variables);

}
