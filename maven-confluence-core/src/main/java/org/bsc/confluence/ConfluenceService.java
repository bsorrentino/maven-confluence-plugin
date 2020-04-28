/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import static java.lang.String.format;

import java.io.Closeable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.bsc.functional.Tuple2;

import lombok.val;
/**
 *
 * @author bsorrentino
 */
public interface ConfluenceService extends Closeable{

    public enum Protocol {
        
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
        
        
    public static class Storage {
        
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

        public Storage(String value, Representation rapresentation) {
            this.value = value;
            this.rapresentation = rapresentation;
        }
        
        
    }
    public static class Credentials {
    
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
    
    public interface Model {

        public interface Attachment {
                void setFileName(String name);
                String getFileName();

                void setContentType(String contentType);

                void setComment( String comment );

                java.util.Date getCreated();
        }            

        public interface PageSummary {
            
            String getId();
            
            String getTitle();
            
            String getSpace();
            
            String getParentId();
        }

        public interface Page extends PageSummary {

            int getVersion();
        }

    }
    
    Credentials getCredentials();

    Model.PageSummary findPageByTitle( String parentPageId, String title) throws Exception ;

    CompletableFuture<Boolean> removePage( Model.Page parentPage, String title ) ;

    CompletableFuture<Boolean> removePage(String pageId ) ;
    
    CompletableFuture<Model.Page> createPage( Model.Page parentPage, String title ) ;

    CompletableFuture<Optional<Model.Page>> getPage( String pageId ) ;

    CompletableFuture<Optional<Model.Page>> getPage( String spaceKey, String pageTitle ) ;

    CompletableFuture<Void> addLabelsByName( long id, String[] labels ) ;

    default CompletableFuture<Void> addLabelsByName( long id, java.util.List<String> labels ) {
        if( labels == null || labels.isEmpty() ) return CompletableFuture.completedFuture(null);

        final String[] labelArray = new String[ labels.size() ];
        return addLabelsByName( id, labels.toArray(labelArray) );
    }

    default CompletableFuture<Void> addLabelsByName( String id, java.util.List<String> labels ) {
        return addLabelsByName( Long.valueOf(id), labels);
    }

    default CompletableFuture<Void> addLabelsByName( String id, String[] labels  ) {
        return addLabelsByName( Long.valueOf(id), labels);
    }

    CompletableFuture<Model.Page> storePage( Model.Page page, Storage content ) ;
    
    CompletableFuture<Model.Page> storePage( Model.Page page ) ;
    
    java.util.List<Model.PageSummary> getDescendents(String pageId) throws Exception;

    
    void exportPage(    String url, 
                        String spaceKey, 
                        String pageTitle, 
                        ExportFormat exfmt, 
                        java.io.File outputFile) throws Exception;
    
    //
    // ATTACHMENT
    //
    
    /**
     * factory method
     * 
     * @return 
     */
    Model.Attachment createAttachment(); 
    
    CompletableFuture<Optional<Model.Attachment>> getAttachment( String pageId, String name, String version) ;
    
    CompletableFuture<Model.Attachment> addAttachment( Model.Page page, Model.Attachment attachment, java.io.InputStream source ) ;

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
                                String.format("cannot find parent page [%s] in space [%s]", parentPageTitle, spaceKey))) )
                .thenCombine( getPage(spaceKey, title), Tuple2::of)
                .thenCompose( tuple -> {
                    return ( tuple.getValue2().isPresent() ) ?
                        CompletableFuture.completedFuture(tuple.getValue2().get()) :
                        createPage(tuple.getValue1(), title);
                })
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
     * @see https://gist.github.com/gitplaneta/5065bbba980b2858a55f
     */
    default <T> CompletableFuture<T> retry( int times, 
                                            long delay, 
                                            TimeUnit timeUnit,
                                            Optional<CompletableFuture<T>> resultHandler,
                                            Supplier<CompletableFuture<T>> action) {
        val future = resultHandler.orElseGet( () -> new CompletableFuture<>() );
        action.get()
                .thenAccept(a -> future.complete(a))
                .exceptionally(ex -> {
                    if (times <= 0) {
                        future.completeExceptionally(ex);
                    } else {
                            try {
                                timeUnit.sleep(delay);
                                retry( times - 1, delay, timeUnit, Optional.of(future), action);       
                            } catch (InterruptedException e) {
                                future.completeExceptionally(e);
                            }
                    }
                    return null;
                });

        return future;
    }  
    
  }
