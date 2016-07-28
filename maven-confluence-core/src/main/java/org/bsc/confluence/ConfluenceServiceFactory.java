/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.bsc.confluence.ConfluenceService.Credentials;
import org.bsc.confluence.rest.RESTConfluenceServiceImpl;
import org.bsc.functional.P1;
import org.codehaus.swizzle.confluence.XMLRPCConfluenceServiceImpl;
import static org.codehaus.swizzle.confluence.XMLRPCConfluenceServiceImpl.createInstanceDetectingVersion;

/**
 *
 * @author softphone
 */
public class ConfluenceServiceFactory {

    public static class MixedConfluenceService implements ConfluenceService {
        final XMLRPCConfluenceServiceImpl   xmlrpcService;
        final RESTConfluenceServiceImpl     restService;

        public MixedConfluenceService(String endPoint, Credentials credentials, ConfluenceProxy proxyInfo) throws Exception {
            
            this.xmlrpcService = createInstanceDetectingVersion(endPoint, credentials, proxyInfo);
            this.restService = new RESTConfluenceServiceImpl(endPoint, credentials);
        }
        
        public Credentials getCredentials() {
            return xmlrpcService.getCredentials();
        }

        public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
            return xmlrpcService.findPageByTitle(parentPageId, title);
        }

        public boolean removePage(Model.Page parentPage, String title) throws Exception {
            return xmlrpcService.removePage(parentPage, title);
        }

        public Model.Page getOrCreatePage(String spaceKey, String parentPageTitle, String title) throws Exception {
            return xmlrpcService.getOrCreatePage(spaceKey, parentPageTitle, title);
        }

        public Model.Page getOrCreatePage(Model.Page parentPage, String title) throws Exception {
            return xmlrpcService.getOrCreatePage(parentPage, title);
        }

        public Model.Attachment addAttchment(Model.Page page, Model.Attachment attachment, InputStream source) throws Exception {
            return xmlrpcService.addAttchment(page, attachment, source);
        }

        public Model.Page storePage(Model.Page page) throws Exception {
            return xmlrpcService.storePage(page);
        }

        public Model.Page storePage(Model.Page page, Storage content) throws Exception {
            if( Storage.Representation.STORAGE == content.rapresentation ) {
                return restService.storePage(page, content);
            }
            return xmlrpcService.storePage(page, content);
        }

        public boolean addLabelByName(String label, long id) throws Exception {
            return xmlrpcService.addLabelByName(label, id);
        }

        public Model.Attachment createAttachment() {
            return xmlrpcService.createAttachment();
        }

        public Model.Attachment getAttachment(String pageId, String name, String version) throws Exception {
            return xmlrpcService.getAttachment(pageId, name, version);
        }

        public Model.Page getPage(String spaceKey, String pageTitle) throws Exception {
            return xmlrpcService.getPage(spaceKey, pageTitle);
        }

        public Model.Page getPage(String pageId) throws Exception {
            return xmlrpcService.getPage(pageId);
        }

        public String getVersion() {
            return xmlrpcService.getVersion();
        }

        public void call(P1<ConfluenceService> task) throws Exception {
            xmlrpcService.call(task);
        }

        public List<Model.PageSummary> getDescendents(String pageId) throws Exception {
            return xmlrpcService.getDescendents(pageId);
        }

        public void removePage(String pageId) throws Exception {
            xmlrpcService.removePage(pageId);
        }

        public void exportPage(String url, String spaceKey, String pageTitle, ExportFormat exfmt, File outputFile) throws Exception {
            xmlrpcService.exportPage(url, spaceKey, pageTitle, exfmt, outputFile);
        }
        
    }
    public static ConfluenceService createInstance(String endPoint, Credentials credentials, ConfluenceProxy proxyInfo) throws Exception {
        
        return new MixedConfluenceService(endPoint, credentials, proxyInfo);
    }
    
}
