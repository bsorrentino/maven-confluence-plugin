package org.bsc.markdown;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * Markdown Processor interface
 */
public interface MarkdownProcessor {

    /**
     * markdown processor identifier used to choose which procerror use at run-time
     *
     * @return identifier
     */
    String getName();

    /**
     * translate a markdown source in the confluence wiki counterpart
     *
     * @param context parse context
     * @param content content to parse
     * @return translated confluence wiki format
     * @throws IOException
     */
    String processMarkdown( MarkdownParserContext context, String content ) throws IOException;

    /**
     * default method
     *
     * @param content
     * @return
     * @throws IOException
     */
    default String processMarkdown( String content ) throws IOException {
        return processMarkdown(new MarkdownParserContext() {
        }, content);
    }

    /**
     * factory method
     *
     * @return
     */
    static MarkdownProcessor load( String name ) {

        final ServiceLoader<MarkdownProcessor> loader = ServiceLoader.load(MarkdownProcessor.class);

        final Stream<MarkdownProcessor> processors = StreamSupport.stream( loader.spliterator(), false );

        return processors.filter( p -> name.equalsIgnoreCase(p.getName()))
                .findFirst()
                .orElseThrow( () -> new IllegalStateException( format("Markdown processor [%s] not found!", name ) ) );

    }

    class Shared implements MarkdownProcessor {
        private String name = "commonmark";
        private MarkdownProcessor processor;

        @Override
        public String getName() {
            return ofNullable(shared.name)
                    .orElseThrow( () -> new IllegalStateException( "processor's name doesn't set!" ));
        }

        @Override
        public String processMarkdown(MarkdownParserContext context, String content) throws IOException {
            return ofNullable(processor)
                    .orElseThrow( () -> new IllegalStateException( "processor has not been initialized yet!" ))
                    .processMarkdown( context, content);
        }

        public void setName(String value) {
            name = ofNullable(value)
                    .orElseThrow( () -> new IllegalArgumentException( "processor's name cannot be null!" ));
        }

        public MarkdownProcessor init( ) {
            if( processor!=null ) {
                throw new IllegalStateException( "processor's is already initialized!" );
            }
            processor =  MarkdownProcessor.load( getName() );
            return processor;
        }
    }

    Shared shared = new Shared();
}
