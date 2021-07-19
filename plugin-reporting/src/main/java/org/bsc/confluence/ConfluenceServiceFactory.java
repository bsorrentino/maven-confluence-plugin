package org.bsc.confluence;

import org.bsc.confluence.ConfluenceService.Credentials;
import org.bsc.confluence.rest.RESTConfluenceService;
import org.bsc.confluence.rest.model.Page;
import org.bsc.confluence.rest.scrollversions.ScrollVersionsConfluenceService;
import org.bsc.confluence.xmlrpc.XMLRPCConfluenceService;
import org.bsc.mojo.configuration.ScrollVersionsInfo;
import org.bsc.ssl.SSLCertificateInfo;

import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.bsc.confluence.xmlrpc.XMLRPCConfluenceService.createInstanceDetectingVersion;

/**
 *
 * @author bsorrentino
 */
public class ConfluenceServiceFactory {

    private static class MixedConfluenceService implements ConfluenceService {
        final XMLRPCConfluenceService xmlrpcService;
        final RESTConfluenceService restService;

        public MixedConfluenceService(String endpoint, Credentials credentials, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo) throws Exception {
            
            this.xmlrpcService = createInstanceDetectingVersion(endpoint, credentials, proxyInfo, sslInfo);
            
            final String restEndpoint = new StringBuilder()
            		.append(ConfluenceService.Protocol.XMLRPC.removeFrom(endpoint))
            		.append(ConfluenceService.Protocol.REST.path())
            		.toString();
            
            this.restService = new RESTConfluenceService(restEndpoint, credentials, sslInfo);
        }
        
        @Override
        public Credentials getCredentials() {
            return xmlrpcService.getCredentials();
        }

        @Override
        public Model.Page newPage(Model.ID id, String space, String title, int version) {
            return xmlrpcService.newPage(id, space, title, version);
        }

        @Override
        public CompletableFuture<Optional<? extends Model.PageSummary>> getPageByTitle(Model.ID parentPageId, String title)  {
            return xmlrpcService.getPageByTitle(parentPageId, title);
        }

        @Override
        public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {
            return xmlrpcService.getPage(spaceKey, pageTitle);
        }

        @Override
        public CompletableFuture<Optional<Model.Page>> getPage(Model.ID pageId) {
            return xmlrpcService.getPage(pageId);
        }

        @Override
        public CompletableFuture<List<Model.PageSummary>> getDescendents(Model.ID pageId)  {
            return xmlrpcService.getDescendents(pageId);
        }

        @Override
        public CompletableFuture<Boolean> removePage(Model.ID pageId) {
            return xmlrpcService.removePage(pageId);
        }

        @Override
        public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
            return xmlrpcService.removePage(parentPage, title);
        }

        @Override
        public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title, Storage content)  {
            return xmlrpcService.createPage(parentPage, title, content);
        }
        @Override
        public CompletableFuture<Model.Page> storePage(Model.Page page)  {
            return xmlrpcService.storePage(page);
        }

        @Override
        public CompletableFuture<Model.Page> storePage(Model.Page page, Storage content)  {
            
            if( Storage.Representation.STORAGE == content.rapresentation ) {
                
                if( page.getId()==null ) { 
                    final JsonObjectBuilder inputData = 
                            restService.jsonForCreatingContent( RESTConfluenceService.ContentType.page,
                                                                page.getSpace(),
                                                                page.getParentId().getValue(),
                                                                page.getTitle(),
                                                                content);
                    return restService.createPage(inputData.build())
                            .thenApply( p -> p.map(Page::new).get() );
                    
                }

                return restService.storePage(page, content);
            }
            return xmlrpcService.storePage(page, content);
        }

        @Override
        public CompletableFuture<Void> addLabelsByName(Model.ID id, String[] labels) {
            return xmlrpcService.addLabelsByName(id, labels);
        }

        @Override
        public Model.Attachment newAttachment() {
            return xmlrpcService.newAttachment();
        }

        @Override
        public CompletableFuture<Optional<Model.Attachment>> getAttachment(Model.ID pageId, String name, String version) {
            return xmlrpcService.getAttachment(pageId, name, version);
        }

        @Override
        public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source)  {
            return xmlrpcService.addAttachment(page, attachment, source);
        }

        /**
         * factory method
         *
         * @param space   space id
         * @param title   post's title
         * @param content post's content
         * @return
         */
        @Override
        public Model.Blogpost createBlogpost(String space, String title, Storage content, int version) {
            return xmlrpcService.createBlogpost(space, title, content, version);
        }

        @Override
        public CompletableFuture<Model.Blogpost> addBlogpost(Model.Blogpost blogpost )  {
            return xmlrpcService.addBlogpost(blogpost);
        }

        @Override
        public String toString() {
            return xmlrpcService.toString();
        }

        /**
         * 
         */
        @Override
        public void close() throws IOException {
            xmlrpcService.logout();
            
        }
        
    }

    private static Optional<ConfluenceService> _tryCeateInstance( String endpoint,
                                                        Credentials credentials, 
                                                        ConfluenceProxy proxyInfo, 
                                                        SSLCertificateInfo sslInfo,
                                                        ScrollVersionsInfo svi ) throws Exception
    {

        ConfluenceService result = null;
        if( ConfluenceService.Protocol.XMLRPC.match(endpoint)) {
            result = new MixedConfluenceService(endpoint, credentials, proxyInfo, sslInfo);
        }
        else if( ConfluenceService.Protocol.REST.match(endpoint)) {
            result =  svi.optVersion()
                    .map( version -> (ConfluenceService)new ScrollVersionsConfluenceService(endpoint, version, credentials, sslInfo) )
                    .orElseGet( () -> new RESTConfluenceService(endpoint, credentials /*, proxyInfo*/, sslInfo) )
                     ;
        }
        return Optional.ofNullable(result);

    }

    private static final java.util.logging.Logger log =
            java.util.logging.Logger.getLogger(ConfluenceServiceFactory.class.getName());

    /**
     * return XMLRPC based Confluence services
     *
     * @param endpoint
     * @param credentials
     * @param proxyInfo
     * @param sslInfo
     * @param svi scroll versions addon parameters
     * @return XMLRPC based Confluence services
     * @throws Exception
     */
    public static ConfluenceService createInstance( String endpoint,
                                                    Credentials credentials,
                                                    ConfluenceProxy proxyInfo,
                                                    SSLCertificateInfo sslInfo,
                                                    ScrollVersionsInfo svi ) throws Exception
    {


        final ConfluenceService result =  _tryCeateInstance( endpoint, credentials, proxyInfo, sslInfo, svi )
                .orElseThrow( () -> new IllegalArgumentException(
                        format("endpoint doesn't contain a valid API protocol\nIt must be '%s' or '%s'",
                                ConfluenceService.Protocol.XMLRPC.path(),
                                ConfluenceService.Protocol.REST.path())
                ));

        if( log.isLoggable(Level.FINE) ) {

            final InvocationHandler handler = (Object proxy, Method method, Object[] args ) -> {

                final String arguments = (args!=null && args.length > 0 ) ?
                        Arrays.stream(args)
                                .map(String::valueOf)
                                .collect(Collectors.joining("\n", "\narguments:\n", "----\n")) : "";

                log.fine( format( "ConfluenceService.%s()%s", method.getName(), arguments ) );

               return method.invoke(result, args);
            };

            return (ConfluenceService) Proxy.newProxyInstance( ConfluenceServiceFactory.class.getClassLoader(),
                                            new Class[] { ConfluenceService.class },
                                            handler
                                            );
        }

        return result;

    }

}
