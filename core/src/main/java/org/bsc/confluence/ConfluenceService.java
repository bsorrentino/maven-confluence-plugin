/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import java.io.Closeable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 *
 * @author bsorrentino
 */
public interface ConfluenceService extends Closeable{

    String connectTimeoutInSeconds = "confluence.timeout.connect.secs";
    String writeTimeoutInSeconds   = "confluence.timeout.write.secs";
    String readTimeoutInSeconds    = "confluence.timeout.read.secs";

    enum Protocol {
        
        XMLRPC ("rpc/xmlrpc"),
        REST ("rest/api");
        
        private final String path;
        
        Protocol( String path ) {
            this.path = path;
        }   
        
        public String path() { return path; }
        
        /**
         * add protocol path segment as suffix 
         * 
         * @param endpoint
         * @return 
         */
        public String addTo( String endpoint ) {
            if( null == endpoint ) {
                throw new IllegalArgumentException("endpoint argument is null!");
            }
            if( endpoint.endsWith(path) ) {
                return endpoint;
            }
            
            if (!endpoint.endsWith("/")) {
                endpoint = endpoint.concat("/");
            } 
            
            return endpoint.concat(path);
        }
        
        /**
         * remove protocol path segment from given string
         * 
         * @param endpoint
         * @return endpoint without 
         */
        public String removeFrom( String endpoint ) {
            if( null==endpoint ) {
                throw new IllegalArgumentException( "endpoint argument is null!");
            }

            String result = endpoint.replace(path, "");
            result = (result.startsWith("/")) ? result.substring(1) : result;

            return result;

        }
        
        public boolean match( String endpoint )  {
        		return Pattern.matches( format(".+(%s)[/]?", path), endpoint );
        }
        
    }
        
        
    class Storage {
        
        public enum Representation {
            STORAGE,
            WIKI;

            @Override
            public String toString() {
                return name().toLowerCase();
            }
        }
        
        public final String value;
        public final Representation rapresentation;

        private Storage(String value, Representation rapresentation) {
            this.value = value;
            this.rapresentation = rapresentation;
        }

        public static Storage of( String value, Representation rapresentation ) {
            return new Storage(value,rapresentation);
        }
        
    }
    class Credentials {
    
        public final String username;
        public final String password;

        public Credentials(String username, String password) {
            if( username==null ) {
                throw new IllegalArgumentException("username argument is null!");
            }
            this.username = username;
            this.password = password;
        }
        
    }   
    
    interface Model {

        class ID implements Comparable<ID> {
            final long value;

            private ID(long value) { this.value = value; }

            public long getValue() { return value; }

            public String toString() { return String.valueOf(value); }

            public static ID of(long value ) { return new ID(value); }
            public static ID of( String value ) { return new ID(Long.valueOf(value)); }

            @Override
            public int compareTo(Model.ID o) {
                return (int) (value - o.value);
            }
        }
        interface Attachment {
                void setFileName(String name);
                String getFileName();

                void setContentType(String contentType);

                void setComment( String comment );

                java.util.Date getCreated();
        }            

        interface PageSummary {
            
            ID getId();
            
            String getTitle();
            
            String getSpace();
            
            ID getParentId();
        }

        interface Page extends PageSummary {

            int getVersion();
        }

        interface Blogpost  {
            ID getId();

            String getTitle();

            String getSpace();

            int getVersion();
        }

    }

    static long getConnectTimeout(TimeUnit timeUnit) {
        final long seconds = Long.valueOf(System.getProperty( connectTimeoutInSeconds, "10"));
        return  TimeUnit.SECONDS.convert(seconds, timeUnit);
    }
    static long getWriteTimeout(TimeUnit timeUnit) {
        final long seconds = Long.valueOf(System.getProperty( writeTimeoutInSeconds, "10"));
        return  TimeUnit.SECONDS.convert(seconds, timeUnit);
    }
    static long getReadTimeout(TimeUnit timeUnit) {
        final long seconds = Long.valueOf(System.getProperty( readTimeoutInSeconds, "10"));
        return  TimeUnit.SECONDS.convert(seconds, timeUnit);
    }

    static void setConnectTimeouts( long value, TimeUnit timeUnit) {
        final long seconds = timeUnit.toSeconds(value);
        System.setProperty( connectTimeoutInSeconds, String.valueOf(seconds) );
    }
    static void setWriteTimeouts( long value, TimeUnit timeUnit ) {
        final long seconds = timeUnit.toSeconds(value);
        System.setProperty( writeTimeoutInSeconds, String.valueOf(seconds) );
    }
    static void setReadTimeouts( long value, TimeUnit timeUnit ) {
        final long seconds = timeUnit.toSeconds(value);
        System.setProperty( readTimeoutInSeconds, String.valueOf(seconds) );
    }

    Credentials getCredentials();

    CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(Model.ID parentPageId, String title)  ;

    CompletableFuture<Boolean> removePage( Model.Page parentPage, String title ) ;

    CompletableFuture<Boolean> removePage(Model.ID pageId ) ;

    CompletableFuture<Model.Page> createPage( Model.Page parentPage, String title, Storage content ) ;

    CompletableFuture<Model.Page> storePage( Model.Page page, Storage content ) ;

    CompletableFuture<Model.Page> storePage( Model.Page page ) ;

    CompletableFuture<Optional<Model.Page>> getPage( Model.ID pageId ) ;

    CompletableFuture<Optional<Model.Page>> getPage( String spaceKey, String pageTitle ) ;

    CompletableFuture<Void> addLabelsByName( Model.ID id, String[] labels ) ;

    default CompletableFuture<Void> addLabelsByName( Model.ID id, java.util.List<String> labels ) {
        if( labels == null || labels.isEmpty() ) return completedFuture(null);

        final String[] labelArray = new String[ labels.size() ];
        return addLabelsByName( id, labels.toArray(labelArray) );
    }

    CompletableFuture<java.util.List<Model.PageSummary>> getDescendents(Model.ID pageId) ;

    ////////////////////////////////////////////////////////////////////////////////
    // ATTACHMENT
    ///////////////////////////////////////////////////////////////////////////////
    
    /**
     * factory method
     * 
     * @return 
     */
    Model.Attachment createAttachment();

    /**
     *
     * @param pageId
     * @param name
     * @param version
     * @return
     */
    CompletableFuture<Optional<Model.Attachment>> getAttachment( Model.ID pageId, String name, String version) ;

    /**
     *
     * @param page
     * @param attachment
     * @param source
     * @return
     */
    CompletableFuture<Model.Attachment> addAttachment( Model.Page page, Model.Attachment attachment, java.io.InputStream source ) ;

    ////////////////////////////////////////////////////////////////////////////////
    // BLOG POST
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * factory method
     *
     * @param space space id
     * @param title post's title
     * @param content post's content
     *
     * @return
     */
    Model.Blogpost createBlogpost( String space, String title, Storage content, int version );

    /**
     *
     * @param blogpost
     * @return
     */
    CompletableFuture<Model.Blogpost> addBlogpost( Model.Blogpost blogpost) ;

    /**
     * 
     * @param spaceKey
     * @param parentPageTitle
     * @param title
     * @return
     */
    default CompletableFuture<Model.Page> getOrCreatePage( 
            String spaceKey, 
            String parentPageTitle, 
            String title ) 
    {
        return getPage(spaceKey, parentPageTitle)
                .thenApply( parent -> 
                    parent.orElseThrow( () -> 
                        new RuntimeException( 
                                format("cannot find parent page [%s] in space [%s]", parentPageTitle, spaceKey))) )
                .thenCombine( getPage(spaceKey, title), ParentChildTuple::of)
                .thenCompose( tuple -> ( tuple.getChild().isPresent() )
                        ? completedFuture(tuple.getChild().get())
                        : createPage(tuple.getParent(), title, Storage.of( "", Storage.Representation.WIKI)))
                ;
        }

    /**
     * 
     * @param <T>
     * @param times
     * @param delay
     * @param timeUnit
     * @param resultHandler
     * @param action
     * @return
     * @see "https://gist.github.com/gitplaneta/5065bbba980b2858a55f"
     */
    default <T> CompletableFuture<T> retry( int times, 
                                            long delay, 
                                            TimeUnit timeUnit,
                                            Optional<CompletableFuture<T>> resultHandler,
                                            Supplier<CompletableFuture<T>> action) {
        final CompletableFuture<T> future = resultHandler.orElseGet( () -> new CompletableFuture<>() );
        action.get()
                .thenAccept(a -> future.complete(a))
                .exceptionally(ex -> {
                    if (times <= 0) {
                        future.completeExceptionally(ex);
                    } else {
                            try {
                                timeUnit.sleep(delay);
                                retry( times - 1, delay, timeUnit, Optional.of(future), action);
                            } catch ( InterruptedException e) {
                                future.completeExceptionally(e);
                            }
                    }
                    return null;
                });

        return future;
    }  
    
  }
