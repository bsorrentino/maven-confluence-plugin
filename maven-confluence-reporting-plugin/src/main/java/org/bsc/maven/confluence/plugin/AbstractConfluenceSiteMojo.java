/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.confluence.plugin;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;

import static java.lang.String.format;

/**
 *
 * @author bsorrentino
 */
public abstract class AbstractConfluenceSiteMojo extends AbstractConfluenceMojo implements SiteFactory {

    /**
     * site xml descriptor
     * @since 3.3.0
     */
    @Parameter(defaultValue = "${basedir}/src/site/confluence/site.xml")
    protected java.io.File siteDescriptor;

    /**
     * 
     * @return 
     */
    public File getSiteDescriptor() {
        return siteDescriptor;
    }
    
    
    protected boolean isSiteDescriptorValid() {
        return ( siteDescriptor!=null  && siteDescriptor.exists() && siteDescriptor.isFile());   
    }
    
    /**
     * 
     * @param page
     * @param source 
     */
    private void setPageUriFormFile( Site.Page page, java.io.File source ) {
        if( page == null ) {
            throw new IllegalArgumentException( "page is null!");
        }
        
        if (source != null && source.exists() && source.isFile() && source.canRead() ) {
            page.setUri(source.toURI());
        }
        else {
            try {
                java.net.URL sourceUrl = getClass().getClassLoader().getResource("defaultTemplate.confluence");
                page.setUri( sourceUrl.toURI() );
            } catch (URISyntaxException ex) {
                // TODO log
            }
        }
        
    }
    
    /**
     * 
     * @param page
     * @param confluence
     * @param confluencePage 
     */
    private void generateAttachments( Site.Page page,  ConfluenceService confluence, Model.Page confluencePage) /*throws MavenReportException*/ {

        getLog().info(format("generateAttachments pageId [%s] title [%s]", confluencePage.getId(), confluencePage.getTitle()));

        for( Site.Attachment attachment : page.getAttachments() ) {

            Model.Attachment confluenceAttachment = null;

            try {
                confluenceAttachment = confluence.getAttachment(confluencePage.getId(), attachment.getName(), attachment.getVersion());
            } catch (Exception e) {
                getLog().debug(format("Error getting attachment [%s] from confluence: [%s]", attachment.getName(), e.getMessage()));
            }

            if (confluenceAttachment != null) {

                java.util.Date date = confluenceAttachment.getCreated();

                if (date == null) {
                    getLog().warn(format("creation date of attachments [%s] is undefined. It will be replaced! ", confluenceAttachment.getFileName()));
                } else {
                    if (attachment.hasBeenUpdatedFrom(date)) {
                        getLog().info(format("attachment [%s] is more recent than the remote one. It will be replaced! ", confluenceAttachment.getFileName()));
                    } else {
                        getLog().info(format("attachment [%s] skipped! no updated detected", confluenceAttachment.getFileName()));
                        continue;

                    }
                }
            } else {
                getLog().info(format("Creating new attachment for [%s]", attachment.getName()));
                confluenceAttachment = confluence.createAttachment();
                confluenceAttachment.setFileName(attachment.getName());
                confluenceAttachment.setContentType(attachment.getContentType());

            }

            confluenceAttachment.setComment( attachment.getComment());
            
            try( java.io.InputStream is = attachment.getUri().toURL().openStream()) {
                confluence.addAttchment(confluencePage, confluenceAttachment, is );
            } catch (Exception e) {
                final String msg = format("Error uploading attachment [%s] ", attachment.getName());
                //getLog().error(msg);
                throw new RuntimeException(msg,e);

            }

        }

    }
    
    
    /**
     * 
     * @param confluence
     * @param parentPage
     * @param confluenceParentPage
     * @param titlePrefix
     */
    protected void generateChildren(    final ConfluenceService confluence,
                                        final Site.Page parentPage,
                                        final Model.Page confluenceParentPage,
                                        final String titlePrefix,
                                        final Map<String, Model.Page> varsToParentPageMap)
    {

        getLog().info(format("generateChildren # [%d]", parentPage.getChildren().size()));

        
        generateAttachments(parentPage, confluence, confluenceParentPage);
        
        for( Site.Page child : parentPage.getChildren() ) {

            final Model.Page confluencePage = generateChild(confluence, child, confluenceParentPage.getSpace(), parentPage.getName(), titlePrefix);

            for (Site.Page.Generated generated : child.getGenerateds()) {
                varsToParentPageMap.put(generated.getRef(), confluencePage);
            }

            if( confluencePage != null  ) {

                generateChildren(confluence, child, confluencePage, titlePrefix, varsToParentPageMap );
            }
            
        }
 
    }

    /**
     * 
     * @param folder
     * @param page
     * @return 
     */
    protected boolean navigateAttachments( java.io.File folder,  Site.Page page) /*throws MavenReportException*/ {

        if (folder.exists() && folder.isDirectory()) {

            java.io.File[] files = folder.listFiles();

            if (files != null && files.length > 0) {

                for (java.io.File f : files) {

                    if (f.isDirectory() || f.isHidden()) {
                        continue;
                    }

                    Site.Attachment a = new Site.Attachment();

                    a.setName(f.getName());
                    a.setUri(f.toURI());

                    page.getAttachments().add(a);
                }
            }

            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * @param level
     * @param folder
     * @param parentChild 
     */
   protected void navigateChild( final int level, final java.io.File folder, final Site.Page parentChild ) /*throws MavenReportException*/ {

        if (folder.exists() && folder.isDirectory()) {

            folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    if( file.isHidden() || file.getName().charAt(0)=='.') {
                        return false ;
                    }

                    
                    if( file.isDirectory() ) {
                    
                        if( navigateAttachments(file, parentChild)) {
                            return false;
                        }
            
                        Site.Page child = new Site.Page();

                        child.setName(file.getName());
                        setPageUriFormFile(child, new java.io.File(file,templateWiki.getName()) );
 
                        parentChild.getChildren().add(child);
 
                        navigateChild( level+1, file, child );    
                       
                       return true;
                    }
                    
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith( getFileExt() ) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Site.Page child = new Site.Page();
                    final int extensionLen = getFileExt().length();

                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    setPageUriFormFile(child, file );
                    
                    parentChild.getChildren().add(child);
                    
                    return false;

                }
            });
        }

    }
   
    @Override
    public Site createFromFolder() {
        
        final Site result = new Site();
        
        result.getLabels().addAll( super.getLabels());
        
        final Site.Page home = new Site.Page();
        
        
        home.setName(getTitle());
        
        setPageUriFormFile(home, templateWiki);
        
        result.setHome( home );
        

        navigateAttachments(getAttachmentFolder(), home);
        
        if (getChildrenFolder().exists() && getChildrenFolder().isDirectory()) {

            getChildrenFolder().listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {


                    if( file.isHidden() || file.getName().charAt(0)=='.') return false ;

                    if( file.isDirectory() ) {
                       
                        Site.Page parentChild = new Site.Page();

                        parentChild.setName(file.getName());
                        setPageUriFormFile(parentChild, new java.io.File(file,templateWiki.getName()) );

                        result.getHome().getChildren().add(parentChild);

                        navigateChild( 1, file, parentChild );    
                        
                        return false;
                    }
                     
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith(getFileExt()) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Site.Page child = new Site.Page();
                    
                    final int extensionLen = getFileExt().length();
                    
                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    setPageUriFormFile(child, file );

                    result.getHome().getChildren().add(child);

                    return false;

                }
            });
        }
        
        return result;
    }

    /**
     * 
     * @return 
     */
    @Override
    public Site createFromModel() {
        
        Site site = null;
        
        if( !isSiteDescriptorValid() ) {
        
            getLog().warn( "siteDescriptor is not valid!" );
            
        }
        else {
            try {

                JAXBContext jc = JAXBContext.newInstance(Site.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();

                site = (Site) unmarshaller.unmarshal( siteDescriptor );

            } catch (JAXBException ex) {
                getLog().error("error creating site from model!", ex);

            }
        }
        return site;
    }
    
}
