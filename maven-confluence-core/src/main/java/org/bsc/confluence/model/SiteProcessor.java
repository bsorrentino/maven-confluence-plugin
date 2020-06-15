package org.bsc.confluence.model;

import lombok.Value;
import org.apache.commons.io.IOUtils;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.confluence.FileExtension;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownProcessorProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.bsc.confluence.FileExtension.*;

public class SiteProcessor {
   
    @Value(staticConstructor="of")
    public static class PageContent {
        String content;
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
     * @param uri
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T processUri(
            final java.net.URI uri, 
            java.util.function.BiFunction<Optional<Exception>,Optional<java.io.InputStream>, T> callback) 
    {
        Objects.requireNonNull(uri, "uri is null!");
        Objects.requireNonNull(callback, "callback is null!");

        final String scheme = uri.getScheme();

        Objects.requireNonNull(scheme, format("uri [%s] is invalid!", String.valueOf(uri)));
        
        final String source = uri.getRawSchemeSpecificPart();

        java.io.InputStream result = null;

        if ("classpath".equalsIgnoreCase(scheme)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            result = cl.getResourceAsStream(source);

            if (result == null) {

                cl = Site.class.getClassLoader();

                result = cl.getResourceAsStream(source);

                final Exception ex = new Exception(format("resource [%s] doesn't exist in classloader", source));
                return callback.apply( Optional.of(ex), Optional.empty());

            }

        } else {

            try {
                
                java.net.URL url = uri.toURL();

                result = url.openStream();

            } catch (IOException e) {
                final Exception ex = new Exception(format("error opening url [%s]!", source), e);
                return callback.apply( Optional.of(ex), Optional.empty());
            }
        }

        return callback.apply( Optional.empty(), Optional.of(result));
    }

    /**
     *
     * @param site
     * @param child
     * @param page
     * @param uri
     * @param pagePrefixToApply
     * @param callback
     * @param <T>
     * @param <P>
     * @return
     */
   public static <T,P extends Site.Page> T processPageUri(
           final Site site,
           final P child,
           final Model.Page page,
           final java.net.URI uri, 
           final Optional<String> pagePrefixToApply,
           final BiFunction<Optional<Exception>, Optional<PageContent>, T> callback)
   {
       Objects.requireNonNull(uri, "uri is null!");

       String scheme = uri.getScheme();

       Objects.requireNonNull(scheme, format("uri [%s] is invalid!", String.valueOf(uri)));

       final String source = uri.getRawSchemeSpecificPart();

       final String path = uri.getRawPath();

       final boolean isMarkdown =  FileExtension.MARKDOWN.isExentionOf(path); //(path != null && path.endsWith(".md"));
       final boolean isStorage = (path != null && (path.endsWith(".xml") || path.endsWith(".xhtml")));

       final Storage.Representation representation = (isStorage) ? Storage.Representation.STORAGE
               : Storage.Representation.WIKI;

       String content = null;

       final Function<String, T> throwException = msg -> {
           final Exception ex = new Exception(msg);
           return callback.apply( Optional.of(ex), Optional.empty() );
       };

       if ("classpath".equalsIgnoreCase(scheme)) {
           ClassLoader cl = Thread.currentThread().getContextClassLoader();

           java.io.InputStream is = cl.getResourceAsStream(source);

           if (is == null) {

               cl = Site.class.getClassLoader();

               is = cl.getResourceAsStream(source);

               if (is == null) {
                   return throwException.apply( format("page [%s] doesn't exist in classloader", source) );
               }

               try {
                   final String candidateContent = IOUtils.toString(is);

                   content = (isMarkdown) ? processMarkdown( site, child, ofNullable(page), candidateContent, pagePrefixToApply) : candidateContent;

               } catch (IOException e) {
                   return throwException.apply( format("error processing markdown for page [%s] ", source) );
               }


           }

       } else {

           try {

               final java.net.URL url = uri.toURL();

               final java.io.InputStream is = url.openStream();

               final String candidateContent = IOUtils.toString(is);

               content = (isMarkdown) ? processMarkdown( site, child, ofNullable(page), candidateContent, pagePrefixToApply) : candidateContent;

           } catch (IOException e) {
               final Exception ex = new Exception(format("error opening/processing page [%s]!", source), e);
               return callback.apply( Optional.of(ex), Optional.empty() );
           }
       }

       return callback.apply( Optional.empty(), Optional.of(PageContent.of(content, representation)) );
   }

    
    /**
    *
    * @param uri
    * @return
    * @throws Exception
    */
   public static <T,P extends Site.Page> T processUriContent(
               final Site site,
               final P child,
               final java.net.URI uri,                                  
               final Optional<String> homePageTitle,
               final Function<PageContent, T> onSuccess 
           ) throws /* ProcessUri */Exception 
   {
       Objects.requireNonNull(uri, "uri is null!");

       String scheme = uri.getScheme();

       Objects.requireNonNull(scheme, format("uri [%s] is invalid!", String.valueOf(uri)));

       final String source = uri.getRawSchemeSpecificPart();

       final String path = uri.getRawPath();

       final boolean isMarkdown = MARKDOWN.isExentionOf(path);
       final boolean isStorage = XML.isExentionOf(path) || XHTML.isExentionOf(path);

       final Storage.Representation representation = (isStorage) ? Storage.Representation.STORAGE
               : Storage.Representation.WIKI;

       String content = null;

       if ("classpath".equalsIgnoreCase(scheme)) {

           ClassLoader cl = Thread.currentThread().getContextClassLoader();

           java.io.InputStream is = cl.getResourceAsStream(source);

           if (is == null) {
               // getLog().warn(String.format("resource [%s] doesn't exist in context
               // classloader", source));

               cl = Site.class.getClassLoader();

               is = cl.getResourceAsStream(source);

               if (is == null) {
                   throw new Exception(format("resource [%s] doesn't exist in classloader", source));
               }

               final String candidateContent = IOUtils.toString(is);

               content = (isMarkdown) ? processMarkdown( site, child, Optional.empty(), candidateContent, homePageTitle) : candidateContent;

           }

       } else {

           try {

               java.net.URL url = uri.toURL();

               final java.io.InputStream is = url.openStream();

               final String candidateContent = IOUtils.toString(is);

               content = (isMarkdown) ? processMarkdown( site, child, Optional.empty(), candidateContent, homePageTitle) : candidateContent;

           } catch (IOException e) {
               throw new Exception(format("error opening url [%s]!", source), e);
           }
       }

       return onSuccess.apply( PageContent.of(content, representation) );
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

        return MarkdownProcessorProvider.instance.Load().processMarkdown(new MarkdownParserContext() {
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
