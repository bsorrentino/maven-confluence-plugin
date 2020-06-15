package org.bsc.markdown;

import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 *
 */
public class MarkdownProcessorInfo {

    private static class Data {
        private String name = "pegdown";

        public String getName() {
            return ofNullable(shared.name)
                    .orElseThrow( () -> new IllegalStateException( "processor's name doesn't set!" ));
        }

        public void setName(String value) {
            name = value;
        }

    }

    private static final Data shared = new Data();

    /**
     *
     * @return
     */
    public static MarkdownProcessor LoadProcessor() {

        final String name = shared.getName();

        final ServiceLoader<MarkdownProcessor> loader = ServiceLoader.load(MarkdownProcessor.class);

        final Stream<MarkdownProcessor> processors = StreamSupport.stream( loader.spliterator(), false );

        return processors.filter( p -> name.equalsIgnoreCase(p.getName()))
                .findFirst()
                .orElseThrow( () -> new IllegalStateException( format("Markdown processor [%s] not found!", name ) ) );

    }


    public String getName() {
        return shared.getName();
    }

    public void setName(String name) {
        shared.setName(name);
    }

    public MarkdownProcessorInfo() {
    }
}
