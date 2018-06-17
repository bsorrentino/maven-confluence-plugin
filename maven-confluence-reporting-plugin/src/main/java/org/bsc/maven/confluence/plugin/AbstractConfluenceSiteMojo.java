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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.model.Site;
import org.bsc.confluence.model.SiteFactory;

import static java.util.concurrent.CompletableFuture.completedFuture;
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

    private CompletableFuture<Model.Attachment> updateAttachmentData(
            final ConfluenceService confluence, 
            final Site site, 
            final java.net.URI uri,
            final Model.Page confluencePage, 
            final Model.Attachment attachment ) 
    {
        
        return site.processUri(uri, ( err, is ) -> {
            
            if( err.isPresent() ) {
                CompletableFuture<Model.Attachment> result = new CompletableFuture<>();
                result.completeExceptionally(err.get());
                return result;
            }
            return ( is.isPresent() ) ?
                    confluence.addAttachment(confluencePage, attachment, is.get() ) :
                    completedFuture(attachment) ;
        });     
        
    }
    /**
     *
     * @param confluence
     * @param confluencePage
     * @param attachment
     */
    private void generateAttachment( 
            final ConfluenceService confluence, 
            final Site site, 
            final Model.Page confluencePage, 
            final Site.Attachment attachment) 
    {
        
        getLog().debug(format("generateAttachment\n\tpageId:[%s]\n\ttitle:[%s]\n\tfile:[%s]", 
                confluencePage.getId(), 
                confluencePage.getTitle(), 
                getPrintableStringForResource(attachment.getUri()) ));
      
        final java.net.URI uri = attachment.getUri();
        
        confluence.getAttachment(confluencePage.getId(), attachment.getName(), attachment.getVersion())
        .exceptionally( e -> {
            getLog().debug(format("Error getting attachment [%s] from confluence: [%s]", attachment.getName(), e.getMessage()));
            return Optional.empty();
        })
        .thenCompose( att -> {
            
            if (!att.isPresent()) {             
                getLog().debug(format("Creating new attachment for [%s]", attachment.getName()));
                Model.Attachment result = confluence.createAttachment();
                result.setFileName(attachment.getName());
                result.setContentType(attachment.getContentType());        
                result.setComment( attachment.getComment());
                return resetUpdateStatusForResource(uri)
                        .thenCompose( reset -> completedFuture(result));          
            }
            
            Model.Attachment result = att.get();       
            result.setContentType(attachment.getContentType());        
            result.setComment( attachment.getComment());
            return completedFuture(result);
            
        })
        .thenCompose( finalAttachment -> 
            canProceedToUpdateResource(uri)
            .thenCompose( updated -> updated ? 
                    updateAttachmentData(confluence, site, uri, confluencePage, finalAttachment) :
                    completedFuture(finalAttachment)) 
        )
        ;
 
        
    }
    
    
    /**
     * 
     * @param confluence
     * @param parentPage
     * @param confluenceParentPage
     * @param confluenceParentPage
     */
    protected void generateChildren(final ConfluenceService confluence,
                                    final Site site,
                                    final Site.Page parentPage,
                                    final Model.Page confluenceParentPage,
                                    final Map<String, Model.Page> varsToParentPageMap)
    {

        getLog().debug(format("generateChildren # [%d]", parentPage.getChildren().size()));
        
        generateAttachments( confluence, site, parentPage, confluenceParentPage);
        
        for( Site.Page child : parentPage.getChildren() ) {

            final Model.Page confluencePage = generateChild(site, confluence, child, confluenceParentPage);

            for (Site.Page.Generated generated : child.getGenerateds()) {
                varsToParentPageMap.put(generated.getRef(), confluencePage);
            }

            if( confluencePage != null  ) {

                generateChildren( confluence, site, child, confluencePage, varsToParentPageMap );
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
