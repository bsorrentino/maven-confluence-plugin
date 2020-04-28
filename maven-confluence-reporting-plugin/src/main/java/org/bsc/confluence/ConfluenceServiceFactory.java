/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import org.bsc.confluence.ConfluenceService.Credentials;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.confluence.rest.model.Page;
import org.bsc.confluence.rest.scrollversions.ScrollVersionsConfiguration;
import org.bsc.confluence.rest.scrollversions.ScrollVersionsRESTConfluenceService;
import org.bsc.confluence.xmlrpc.XMLRPCConfluenceServiceImpl;
import org.bsc.ssl.SSLCertificateInfo;

import javax.json.JsonObjectBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static org.bsc.confluence.xmlrpc.XMLRPCConfluenceServiceImpl.createInstanceDetectingVersion;
/**
 *
 * @author bsorrentino
 */
public class ConfluenceServiceFactory {

    private static class MixedConfluenceService implements ConfluenceService {
        final XMLRPCConfluenceServiceImpl   xmlrpcService;
        final RESTConfluenceServiceImpl     restService;

        public MixedConfluenceService(String endpoint, Credentials credentials, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo) throws Exception {
            
            this.xmlrpcService = createInstanceDetectingVersion(endpoint, credentials, proxyInfo, sslInfo);
            
            final String restEndpoint = new StringBuilder()
            		.append(ConfluenceService.Protocol.XMLRPC.removeFrom(endpoint))
            		.append(ConfluenceService.Protocol.REST.path())
            		.toString();
            
            this.restService = new RESTConfluenceServiceImpl(restEndpoint, credentials, sslInfo);
        }
        
        @Override
        public Credentials getCredentials() {
            return xmlrpcService.getCredentials();
        }

        @Override
        public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
            return xmlrpcService.findPageByTitle(parentPageId, title);
        }

        @Override
        public CompletableFuture<Boolean> removePage(Model.Page parentPage, String title) {
            return xmlrpcService.removePage(parentPage, title);
        }

        @Override
        public CompletableFuture<Model.Page> createPage(Model.Page parentPage, String title)  {
            return xmlrpcService.createPage(parentPage, title);
        }

        @Override
        public CompletableFuture<Model.Attachment> addAttachment(Model.Page page, Model.Attachment attachment, InputStream source)  {
            return xmlrpcService.addAttachment(page, attachment, source);
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
                            restService.jsonForCreatingPage(page.getSpace(), 
                                                            page.getParentId(),
                                                            page.getTitle());
                    restService.jsonAddBody(inputData, content);
                    
                    return CompletableFuture.supplyAsync( () -> 
                        restService.createPage(inputData.build()).map(Page::new).get() );
                    
                }

                return restService.storePage(page, content);
            }
            return xmlrpcService.storePage(page, content);
        }

        @Override
        public CompletableFuture<Void> addLabelsByName(long id, String[] labels) {
            return xmlrpcService.addLabelsByName(id, labels);
        }

        @Override
        public Model.Attachment createAttachment() {
            return xmlrpcService.createAttachment();
        }

        @Override
        public CompletableFuture<Optional<Model.Attachment>> getAttachment(String pageId, String name, String version) {
            return xmlrpcService.getAttachment(pageId, name, version);
        }

        @Override
        public CompletableFuture<Optional<Model.Page>> getPage(String spaceKey, String pageTitle) {       
            return xmlrpcService.getPage(spaceKey, pageTitle);
        }

        @Override
        public CompletableFuture<Optional<Model.Page>> getPage(String pageId) {
            return xmlrpcService.getPage(pageId);
        }

        @Override
        public String toString() {
            return xmlrpcService.toString();
        }

        @Override
        public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
            return xmlrpcService.getDescendents(pageId);
        }

        @Override
        public CompletableFuture<Boolean> removePage(String pageId) {
            return xmlrpcService.removePage(pageId);
        }

        @Override
        public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
            xmlrpcService.exportPage(url, spaceKey, pageTitle, exfmt, outputFile);
        }

        /**
         * 
         */
        @Override
        public void close() throws IOException {
            xmlrpcService.logout();
            
        }
        
    }
    
    /**
     * return XMLRPC based Confluence services
     * 
     * @param endpoint
     * @param credentials
     * @param proxyInfo
     * @param sslInfo
     * @return XMLRPC based Confluence services
     * @throws Exception
     */
    public static ConfluenceService createInstance(	String endpoint, 
    													Credentials credentials, 
    													ConfluenceProxy proxyInfo, 
    													SSLCertificateInfo sslInfo) throws Exception 
    {
        return createInstance(endpoint, credentials, proxyInfo, sslInfo, Optional.empty());
    }
    
    /**
     * return XMLRPC based Confluence services
     * 
     * @param endpoint
     * @param credentials
     * @param proxyInfo
     * @param sslInfo
     * @param sv scroll versions addon configuration
     * @return XMLRPC based Confluence services
     * @throws Exception
     */
    public static ConfluenceService createInstance( String endpoint, 
                                                        Credentials credentials, 
                                                        ConfluenceProxy proxyInfo, 
                                                        SSLCertificateInfo sslInfo,
                                                        Optional<ScrollVersionsConfiguration> sv ) throws Exception 
    {
            if( ConfluenceService.Protocol.XMLRPC.match(endpoint)) {
                return new MixedConfluenceService(endpoint, credentials, proxyInfo, sslInfo);               
            }
            if( ConfluenceService.Protocol.REST.match(endpoint)) {
                return sv.map( config -> (ConfluenceService)new ScrollVersionsRESTConfluenceService(endpoint, config.getVersion(), credentials, sslInfo) )
                         .orElseGet( () -> new RESTConfluenceServiceImpl(endpoint, credentials /*, proxyInfo*/, sslInfo))
                         ;               
            }
            
            throw new IllegalArgumentException( 
                    format("endpoint doesn't contain a valid API protocol\nIt must be '%s' or '%s'",
                            ConfluenceService.Protocol.XMLRPC.path(),
                            ConfluenceService.Protocol.REST.path()) 
                    );


    }
     
}
