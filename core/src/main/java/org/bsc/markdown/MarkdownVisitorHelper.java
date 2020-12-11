package org.bsc.markdown;

import lombok.NonNull;
import org.bsc.confluence.FileExtension;
import org.bsc.confluence.model.Site;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

public class MarkdownVisitorHelper {

    /**
     *
     * @param uri
     * @return
     */
    private static Optional<String> getFileName(String uri ) {

        try {
            final java.net.URI uriObject = java.net.URI.create(uri);

            final String scheme = uriObject.getScheme();
            if (scheme != null) {

                switch (scheme.toLowerCase()) {
                    case "classpath":
                        return Optional.empty();
                    case "http":
                    case "https":
                        return ofNullable(uri);
                }
            }

            final Path path = Paths.get(uriObject.getPath());

            return ofNullable(path.getFileName().toString());

        }
        catch( Throwable e ) {
            return Optional.empty();
        }

    }

    public static boolean isURL( String url ) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            // ignore exception;
            return false;
        }
        return true;
    }

    private static Pattern patternUri = Pattern.compile("(?:(\\$\\{.+\\})\\^)?(.+)");

    /**
     *
     * @param url
     * @return
     */
    public static  String processImageUrl( String url, MarkdownParserContext context ) {

        if( isURL(url) ) {
            return url;
        }

        final Matcher m = patternUri.matcher(url);

        if( !m.matches() ) {
            return url;
        }

        if( m.group(1) != null ) { // the uri contains explictly a macro : ${ ... }
            return url;
        }

        return getFileName(m.group(2))
                .map( fileName -> (context.isLinkPrefixEnabled()) ? "${page.title}^".concat(fileName) : fileName )
                .orElse(url);


    }

    public static String processLinkUrl( String url, MarkdownParserContext parseContext ) {

        // GUARD CONDITION
        if( !parseContext.getPage().isPresent() || isURL(url) || !FileExtension.MARKDOWN.isExentionOf(url) )
            return url;

        final Predicate<Site.Page> comparePath = ( p ) -> {

            final Path parentPath = Paths.get(parseContext.getPage().get().getUri()).getParent();

            final Path relativePath = parentPath.relativize( Paths.get(p.getUri()));

            final boolean result =  relativePath.equals( Paths.get(url) );

            return result;
        };

        return parseContext.getSite()
                .flatMap( site -> site.getHome().findPage( comparePath ) )
                .map( page -> parseContext.getPagePrefixToApply()
                        .filter( prefixToApply -> !url.startsWith(prefixToApply) )
                        .map( prefixToApply -> format( "%s - %s", prefixToApply, page.getName() ) )
                        .orElse( page.getName() ) )
                .orElse(url)
                ;

    }

//    public enum SkipEscapeMarkdownText {
//
//        TOC( "^\\{[Tt][Oo][Cc](([:]\\w+=\\w+)([|].+)*)?\\}$" ),
//        CHILDREN( "^\\{[Cc]hildren(([:]\\w+=\\w+)([|].+)*)?\\}$" )
//        ;
//
//        private final Pattern patternToSkip;
//
//        public boolean matches( String text ) {
//            return this.patternToSkip.matcher(text).matches();
//        }
//
//        SkipEscapeMarkdownText( String patternToSkip ) {
//            this.patternToSkip = Pattern.compile(patternToSkip);
//        }
//
//    }

    /**
     *
     */
    private static Pattern isConfluenceMacroPattern = Pattern.compile( "^[\\s]*\\{([\\w-]+)(([:][\\w-]+(=(.+))?)([|].+)*)?\\}[\\s]*$" );

    /**
     *
     * @param text
     * @return
     */
    public static boolean isConfluenceMacro( String text ) {
        // GUARD
        if( text == null || text.isEmpty() ) return false;
        return isConfluenceMacroPattern.matcher(text).matches();
    }

    /**
     *
     * @param text
     * @return
     */
    public static String escapeMarkdownText( String text ) {
        // GUARD
        if( text == null || text.isEmpty() ) return text;

//        if( SkipEscapeMarkdownText.TOC.matches( text ) ) return text;
//        if( SkipEscapeMarkdownText.CHILDREN.matches( text ) ) return text;

        final BiFunction<String,String,String> replaceAll = (pattern, value ) -> {
            final Matcher m = Pattern.compile(pattern).matcher(value);

            boolean result = m.find();
            if (result) {
                final StringBuffer sb = new StringBuffer();
                do {
                    m.appendReplacement(sb, " $2");
                    sb.setCharAt( sb.length() - 2, '\\');
                    result = m.find();
                } while (result);
                m.appendTail(sb);
                return sb.toString();
            }
            return value;
        };

        final String leftS[] = { "(\\\\)?(\\[)", "(\\\\)?(\\{)" };
        final String rightS[] = { "(\\\\)?(])", "(\\\\)?(\\})" };

        return replaceAll
                .andThen( result -> replaceAll.apply( rightS[0], result ) )
                .andThen( result -> replaceAll.apply( rightS[1], result ) )
                .andThen( result -> replaceAll.apply( leftS[0], result ) )
                .apply( leftS[1], text );

    }

}
