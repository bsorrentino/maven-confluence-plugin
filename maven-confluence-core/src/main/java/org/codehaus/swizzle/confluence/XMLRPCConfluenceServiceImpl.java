/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codehaus.swizzle.confluence;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.bsc.confluence.ConfluenceService;
import org.bsc.functional.P1;
import static java.lang.String.format;
import java.util.Collections;
import org.bsc.confluence.ExportFormat;

/**
 *
 * @author softphone
 */
public class XMLRPCConfluenceServiceImpl implements ConfluenceService {

    public final Confluence connection;

    /**
     * 
     * @param confluence 
     */
    protected XMLRPCConfluenceServiceImpl(Confluence confluence ) {
        if( confluence==null ) {
            throw new IllegalArgumentException("confluence argument is null!");
        } 
        this.connection = confluence;
    }
    
    /**
     * 
     * @param parentPageId
     * @param title
     * @return
     * @throws Exception 
     */
    @Override
    public Model.PageSummary findPageByTitle(String parentPageId, String title) throws Exception {
        if( parentPageId == null ) {
            throw new IllegalArgumentException("parentPageId argument is null!");
        }
        if( title == null ) {
            throw new IllegalArgumentException("title argument is null!");
        }
        
        final List<PageSummary> children = connection.getChildren(parentPageId);

        for (PageSummary pageSummary : children ) {

        	if( title.equals(pageSummary.getTitle())) {
        		return (Model.PageSummary) pageSummary;
        	}
        }

        return null;
    }

    private Page cast( Model.Page page ) {
        if( page == null ) {
            throw new IllegalArgumentException("page argument is null!");
        }
        if( !(page instanceof Page) ) {
            throw new IllegalArgumentException("page argument is not right type!");
        }
        return (Page)page;

    }
    private Attachment cast( Model.Attachment attachment ) {
        if( attachment == null ) {
            throw new IllegalArgumentException("attachment argument is null!");
        }
        if( !(attachment instanceof Attachment) ) {
            throw new IllegalArgumentException("page argument is not right type!");
        }
        return (Attachment)attachment;

    }

    @Override
    public boolean removePage(Model.Page parentPage, String title) throws Exception {
        if( null==parentPage ) {
            throw new IllegalArgumentException("parentPage is null");
        }

        final Model.PageSummary pageSummary = findPageByTitle( parentPage.getId(), title);

        if( pageSummary!=null ) {
            connection.removePage(pageSummary.getId());
            return true;
        }

        return false;
    }

    @Override
    public Model.Page getOrCreatePage(String spaceKey, String parentPageTitle, String title) throws Exception {

            Page parentPage = connection.getPage(spaceKey, parentPageTitle);

            Model.PageSummary pageSummary = findPageByTitle(parentPage.getId(), title);

            Page result;

            if (null != pageSummary) {
                result = connection.getPage(pageSummary.getId());
            } else {
                result = new Page(Collections.emptyMap());
                result.setSpace(parentPage.getSpace());
                result.setParentId(parentPage.getId());
                result.setTitle(title);

            }

            return result;
    }

    @Override
    public Model.Page getOrCreatePage(Model.Page parentPage, String title) throws Exception {
        throw new UnsupportedOperationException("getOrCreatePage Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model.Attachment addAttchment(Model.Page page, Model.Attachment attachment, InputStream source) throws Exception {
        final Page p = cast(page);
        
        if( p.getId() == null ) {
            throw new IllegalStateException("PageId is null. Attachment cannot be added!");
        }
        
        final Attachment a = cast(attachment);
        
        BufferedInputStream fis = new BufferedInputStream(source, 4096 );
        ByteArrayOutputStream baos = new ByteArrayOutputStream( );

        byte [] readbuf = new byte[4096];

        int len;

        while( (len=fis.read(readbuf))==readbuf.length ) {
                baos.write(readbuf, 0, len);
        }
        if( len> 0 ) baos.write(readbuf, 0, len);

        a.setPageId( page.getId() );

        return connection.addAttachment( new Long(page.getId()), a, baos.toByteArray() );     
        
    }


    @Override
    public Model.Page storePage(Model.Page page) throws Exception {
        final Page p = cast(page);
        
        return (Model.Page) connection.storePage(p);
    }

   @Override
    public Model.Page storePage(Model.Page page, String content) throws Exception {
        if( content == null ) {
            throw new IllegalArgumentException("content argument is null!");
        }
        
        final Page p = cast(page);
        
        p.setContent(content);
        
        return (Model.Page) connection.storePage(p);
    }
    
    /**
     *
     * @param confluence
     */
    protected boolean logout() {

        try {
            if (!connection.logout()) {
                //log.error("connection logout has failed!", null);
                return false;
            }
        } catch (Exception e) {
            //log.error("connection logout has failed due exception ", e);
            return false;
        }
    
        return true;

    }

    /**
     * 
     * @param label
     * @param id
     * @return
     * @throws Exception 
     */
    @Override
    public boolean addLabelByName(String label, long id) throws Exception {
        return connection.addLabelByName(label, id);
    }
    

    @Override
    public Model.Attachment createAttachment() {
        return new Attachment();
    }

    @Override
    public Model.Attachment getAttachment(String pageId, String name, String version) throws Exception {     
        return connection.getAttachment(pageId, name, version);
    }

    @Override
    public Model.Page getPage(String spaceKey, String pageTitle) throws Exception {
        return connection.getPage(spaceKey, pageTitle);
    }

    @Override
    public Model.Page getPage(String pageId) throws Exception {
        return connection.getPage(pageId);
    }

    @Override
    public String getVersion() {
        try {
            final ServerInfo si = connection.getServerInfo();
            
            return format("Confluence version [%d.%d.%d-%s] development version [%b]",  
                            si.getMajorVersion(),                                                                                        
                            si.getMinorVersion(), 
                            si.getPatchLevel(), 
                            si.getBuildId(), 
                            si.isDevelopmentBuild());
            
        } catch (Exception ex) {
            // TODO LOG
            return ex.getMessage();
        }
    }

    @Override
    public void call(P1<ConfluenceService> task) throws Exception {
        
        try {
            task.call(this);
        }
        finally {
            logout();
        }
    }

    @Override
    public List<Model.PageSummary> getDescendents(String pageId) throws Exception {        
        return connection.getDescendents(pageId);
    }

    @Override
    public void removePage(String pageId) throws Exception {
        connection.removePage(pageId);
    }

    @Override
    public void exportPage( String url, 
                            String username, 
                            String password, 
                            String spaceKey, 
                            String pageTitle, 
                            ExportFormat exfmt, 
                            File outputFile) throws Exception 
    {
            final ConfluenceExportDecorator exporter = 
                new ConfluenceExportDecorator(  connection, 
                                                url, 
                                                username, 
                                                password);

            exporter.exportPage(spaceKey, 
                                pageTitle, 
                                exfmt, 
                                outputFile);

    }
    
}
