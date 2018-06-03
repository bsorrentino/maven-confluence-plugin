/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.confluence.plugin;

import static java.lang.String.format;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;

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
    
    private DirectoryStream<Path> newDirectoryStream( Path attachmentPath, Site.Attachment attachment ) throws IOException {
        
        if( StringUtils.isNotBlank(attachment.getName())) {
            return Files.newDirectoryStream(attachmentPath, attachment.getName());
        }

        final DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {             
           @Override
           public boolean accept(Path entry) throws IOException {

               return !(   Files.isDirectory(entry)     || 
                           Files.isHidden(entry)        || 
                           Files.isSymbolicLink(entry)  ||
                           (!Files.isReadable(entry))
                       );
           }                
       };
       return Files.newDirectoryStream(attachmentPath, filter );
    }
    
    /**
     * 
     * @param page
     * @param confluence
     * @param confluencePage 
     */
    private void generateAttachments( final ConfluenceService confluence, Site site, Site.Page page,  final Model.Page confluencePage) /*throws MavenReportException*/ {

        getLog().debug(format("generateAttachments pageId [%s] title [%s]", confluencePage.getId(), confluencePage.getTitle()));

        for( final Site.Attachment attachment : page.getAttachments() ) {

            final Path attachmentPath = Paths.get(attachment.getUri());
            
            if( !Files.isDirectory(attachmentPath) ) {
                generateAttachment(confluence, site, confluencePage, attachment);            
            }
            else {    
                try( final DirectoryStream<Path> dirStream = newDirectoryStream(attachmentPath, attachment) ) {
                    
                    for( Path p : dirStream ) {
                        
                        final Site.Attachment fileAttachment = new Site.Attachment();
                        
                        fileAttachment.setName(p.getFileName().toString());
                        fileAttachment.setUri(p.toUri());

                        fileAttachment.setComment(attachment.getComment());
                        fileAttachment.setVersion(attachment.getVersion());

                        if( StringUtils.isNotEmpty(attachment.getContentType()) ) {
                            fileAttachment.setContentType(attachment.getContentType());
                        }
                        
                        generateAttachment(confluence, site, confluencePage, fileAttachment);
                        
                    }
                    
                } catch (IOException ex) {
                    getLog().warn(format( "error reading directory [%s]", attachmentPath), ex);
                }
            }
        }
    }

    /**
     *
     * @param confluence
     * @param confluencePage
     * @param attachment
     */
    private void generateAttachment(ConfluenceService confluence, Site site, Model.Page confluencePage, Site.Attachment attachment) {
        
        getLog().debug(format("generateAttachment\n\tpageId:[%s]\n\ttitle:[%s]\n\tfile:[%s]", 
                confluencePage.getId(), 
                confluencePage.getTitle(), 
                getPrintableStringForResource(attachment.getUri()) ));

       
        Model.Attachment confluenceAttachment = null;

        try {
            confluenceAttachment = confluence.getAttachment(confluencePage.getId(), attachment.getName(), attachment.getVersion());
        } catch (Exception e) {
            getLog().debug(format("Error getting attachment [%s] from confluence: [%s]", attachment.getName(), e.getMessage()));
        }
            
        if (confluenceAttachment == null) {
            
            getLog().debug(format("Creating new attachment for [%s]", attachment.getName()));
            confluenceAttachment = confluence.createAttachment();
            confluenceAttachment.setFileName(attachment.getName());
            confluenceAttachment.setContentType(attachment.getContentType());

        } else {
            /*
            java.util.Date date = confluenceAttachment.getCreated();

            if (date == null) {
                getLog().warn(format("creation date of attachments [%s] is undefined. It will be replaced! ", confluenceAttachment.getFileName()));
            } else {
                if (attachment.hasBeenUpdatedFrom(date)) {
                    getLog().info(format("attachment [%s] is more recent than the remote one. It will be replaced! ", confluenceAttachment.getFileName()));
                } else {
                    getLog().info(format("attachment [%s] skipped! no updated detected", confluenceAttachment.getFileName()));
                    return;
                }
            }
            */
        }
        
        confluenceAttachment.setFileName(attachment.getName());
        confluenceAttachment.setContentType(attachment.getContentType());        
        confluenceAttachment.setComment( attachment.getComment());

        final Model.Attachment finalAttachment = confluenceAttachment;
        
        site.processUri(attachment.getUri(), ( err, is ) -> {
            
            if( err.isPresent() ) {
                if( err.get().getCause() != null ) throw new RuntimeException(err.get());
                getLog().info( err.get().getMessage());
                return finalAttachment;
            }
            try {
                return ( is.isPresent() ) ?
                        confluence.addAttachment(confluencePage, finalAttachment, is.get() ) :
                            finalAttachment ;
            } catch (Exception ex) {
                final String msg = format("error adding attachment page [%s]", finalAttachment.getFileName());
                throw new RuntimeException(msg, ex);
                
            }    
        });
        
        /*
        try( java.io.InputStream is = attachment.getUri().toURL().openStream()) {
            confluence.addAttachment(confluencePage, confluenceAttachment, is );

        } catch (Exception e) {
            final String msg = format("Error uploading attachment [%s] ", attachment.getName());
            //getLog().error(msg);
            throw new RuntimeException(msg,e);

        }
        */
    }
    
    
    /**
     * 
     * @param confluence
     * @param parentPage
     * @param confluenceParentPage
     * @param titlePrefix
     */
    protected void generateChildren(final ConfluenceService confluence,
                                    final Site site,
                                    final Site.Page parentPage,
                                    final Model.Page confluenceParentPage,
                                    final String titlePrefix,
                                    final Map<String, Model.Page> varsToParentPageMap)
    {

        getLog().debug(format("generateChildren # [%d]", parentPage.getChildren().size()));
        
        generateAttachments( confluence, site, parentPage, confluenceParentPage);
        
        for( Site.Page child : parentPage.getChildren() ) {

            final Model.Page confluencePage = generateChild(site, confluence, child, confluenceParentPage.getSpace(), parentPage.getName(), titlePrefix);

            for (Site.Page.Generated generated : child.getGenerateds()) {
                varsToParentPageMap.put(generated.getRef(), confluencePage);
            }

            if( confluencePage != null  ) {

                generateChildren( confluence, site, child, confluencePage, titlePrefix, varsToParentPageMap );
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
