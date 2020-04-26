package org.bsc.markdown;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 *
 */
public final class MarkdownProcessorProvider {

    public static final MarkdownProcessorProvider instance = new MarkdownProcessorProvider();

    private MarkdownProcessorInfo info = null;

    public void setInfo(MarkdownProcessorInfo info) {
        this.info = info;
    }

    public MarkdownProcessorInfo getInfo() {
        return info;
    }

    public Optional<MarkdownProcessorInfo> optInfo() { return ofNullable(info);  }

    private MarkdownProcessorProvider() { }

    /**
     *
     * @return
     */
    public MarkdownProcessor Load() {

        final String name = optInfo().flatMap( info -> info.optName() ).orElseThrow( () -> new IllegalStateException( "processor's name doesn't set!" ));

        final ServiceLoader<MarkdownProcessor> loader = ServiceLoader.load(MarkdownProcessor.class);

        final Stream<MarkdownProcessor> processors = StreamSupport.stream( loader.spliterator(), false );

        return processors.filter( p -> name.equalsIgnoreCase(p.getName()))
                    .findFirst()
                    .orElseThrow( () -> new IllegalStateException( format("Markdown processor [%s] not found!", name ) ) );

    }
}
