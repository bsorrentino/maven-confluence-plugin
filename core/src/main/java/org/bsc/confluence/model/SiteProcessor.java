package org.bsc.confluence.model;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.bsc.confluence.FileExtension.*;

public class SiteProcessor {
   
    @Value(staticConstructor="of")
    public static class PageContent {
        @NonNull
        String content;
        @NonNull
        Storage.Representation type;

        public InputStream getInputStream() {
            return IOUtils.toInputStream(content);
        }

        public InputStream getInputStream( Charset charset ) throws IOException {
            return IOUtils.toInputStream(content, charset.toString());
        }

        public String getContent( Charset charset ) {
            if( charset != Charset.defaultCharset() ) {
                return new String(content.getBytes(Charset.defaultCharset()), charset);
            }
            return content;
        }
    }
    /**
     *
     * @param source
     * @return
     */
    private static Optional<java.io.InputStream> getResourceAsStream( String source ) {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        java.io.InputStream is = cl.getResourceAsStream(source);
        if (is == null) {
            cl = Site.class.getClassLoader();
            is = cl.getResourceAsStream(source);
        }
        return ofNullable(is);
    }
    /**
     *
     * @param uri
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T processUri(
            final java.net.URI uri, 
            java.util.function.BiFunction<Optional<Exception>,Optional<java.io.InputStream>, T> callback) 
    {
        requireNonNull(uri, "uri is null!");
        requireNonNull(callback, "callback is null!");

        final String scheme = uri.getScheme();

        requireNonNull(scheme, format("uri [%s] is invalid!", String.valueOf(uri)));
        
        final String source = uri.getRawSchemeSpecificPart();

        Optional<java.io.InputStream> result;

        if ("classpath".equalsIgnoreCase(scheme)) {

            result = getResourceAsStream(source);

            if( !result.isPresent() ) {
                final Exception ex = new Exception(format("resource [%s] doesn't exist in classloader", source));
                return callback.apply( Optional.of(ex), empty());
            }

        } else {

            try {
                
                java.net.URL url = uri.toURL();

                result = ofNullable(url.openStream());

            } catch (IOException e) {
                final Exception ex = new Exception(format("error opening url [%s]!", source), e);
                return callback.apply( Optional.of(ex), empty());
            }
        }

        return callback.apply( empty(), result);
    }
    /**
     *
     * @param site
     * @param child
     * @param page
     * @param uri
     * @param pagePrefixToApply
     * @param <P>
     * @return
     */
   public static <P extends Site.Page> CompletableFuture<PageContent> processPageUri(
           final Site site,
           final P child,
           final Optional<Model.Page> page,
           final java.net.URI uri, 
           final Optional<String> pagePrefixToApply)
   {
       requireNonNull(uri, "uri is null!");

       String scheme = uri.getScheme();

       requireNonNull(scheme, format("uri [%s] is invalid!", String.valueOf(uri)));

       final CompletableFuture<PageContent> result = new CompletableFuture<>();

       final String source = uri.getRawSchemeSpecificPart();

       final String path = uri.getRawPath();

       final boolean isMarkdown =  MARKDOWN.isExentionOf(path);
       final boolean isStorage = XML.isExentionOf(path) || XHTML.isExentionOf(path);

       final Storage.Representation representation = (isStorage) ?
               Storage.Representation.STORAGE :
               Storage.Representation.WIKI;

       String content = null;

       if ("classpath".equalsIgnoreCase(scheme)) {

           final Optional<java.io.InputStream> is = getResourceAsStream(source);

           if( !is.isPresent() ) {
               result.completeExceptionally( new Exception( format("page [%s] doesn't exist in classloader", source)));
               return result;
           }

           try {

               final String candidateContent = IOUtils.toString(is.get(), Charset.defaultCharset());

               content = (isMarkdown) ?
                       processMarkdown( site, child, page, candidateContent, pagePrefixToApply) :
                       candidateContent;

           } catch (IOException e) {
               result.completeExceptionally( new Exception( format("error processing markdown for page [%s] ", source)));
               return result;
           }

       } else {

           try {

               final java.net.URL url = uri.toURL();

               final java.io.InputStream is = url.openStream();

               final String candidateContent = IOUtils.toString(is, Charset.defaultCharset());

               content = (isMarkdown) ? processMarkdown( site, child, page, candidateContent, pagePrefixToApply) : candidateContent;

           } catch (IOException e) {
               result.completeExceptionally( new Exception(format("error opening/processing page [%s]!", source), e));
               return result;

           }
       }

       result.complete(PageContent.of(content, representation) );
       return result;
   }

    
    /**
    *
    * @param uri
    * @return
    * @throws Exception
    */
   public static
   <P extends Site.Page> CompletableFuture<PageContent>
   processUriContent(
               final Site site,
               final P child,
               final java.net.URI uri,                                  
               final Optional<String> homePageTitle )
   {
       requireNonNull(uri, "uri is null!");

       String scheme = uri.getScheme();

       requireNonNull(scheme, format("uri [%s] is invalid!", uri));

       final CompletableFuture<PageContent> result = new CompletableFuture<>();

       final String source = uri.getRawSchemeSpecificPart();

       final String path = uri.getRawPath();

       final boolean isMarkdown = MARKDOWN.isExentionOf(path);
       final boolean isStorage = XML.isExentionOf(path) || XHTML.isExentionOf(path);

       final Storage.Representation representation = (isStorage) ? Storage.Representation.STORAGE
               : Storage.Representation.WIKI;

       String content;

       if ("classpath".equalsIgnoreCase(scheme)) {

           final Optional<java.io.InputStream> is = getResourceAsStream(source);

           if (!is.isPresent()) {
               result.completeExceptionally( new Exception(format("resource [%s] doesn't exist in classloader", source)));
               return result;
           }

           try {
               final String candidateContent = IOUtils.toString(is.get(), Charset.defaultCharset());

               content = (isMarkdown) ? processMarkdown(site, child, empty(), candidateContent, homePageTitle) : candidateContent;

            } catch (IOException e) {
                result.completeExceptionally( new Exception( format("error processing page [%s] ", source)));
                return result;
            }

       } else {

           try {
               final java.net.URL url = uri.toURL();

               final java.io.InputStream is = url.openStream();

               final String candidateContent = IOUtils.toString(is, Charset.defaultCharset());

               content = (isMarkdown) ?
                       processMarkdown( site, child, empty(), candidateContent, homePageTitle) :
                       candidateContent;

           } catch (IOException e) {
               result.completeExceptionally( new Exception(format("error opening url [%s]!", source), e));
               return result;
           }
       }

       result.complete( PageContent.of(content, representation) );
       return result;
   }

    /**
     *
     * @param site
     * @param child
     * @param page
     * @param content
     * @param pagePrefixToApply
     * @return
     * @throws IOException
     */
    public static  String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final Optional<String> pagePrefixToApply) throws IOException {

        return MarkdownProcessor.shared.processMarkdown(new MarkdownParserContext() {
            @Override
            public Optional<Site> getSite() {
                return Optional.of(site);
            }

            @Override
            public Optional<Site.Page> getPage() {
                return Optional.of(child);
            }

            @Override
            public Optional<String> getPagePrefixToApply() {
                return pagePrefixToApply;
            }

            @Override
            public boolean isLinkPrefixEnabled() {
                if( child.isIgnoreVariables() ) return false;

                return page.map( p -> !p.getTitle().contains("[") ).orElse(true);

            }
        }, content);
    }

}
