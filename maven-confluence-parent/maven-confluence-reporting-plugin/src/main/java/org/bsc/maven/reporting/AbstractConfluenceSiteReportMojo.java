/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting;

import biz.source_code.miniTemplator.MiniTemplator;
import java.io.File;
import java.io.FileFilter;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.bsc.maven.reporting.model.Child;
import org.bsc.maven.reporting.model.Site;
import org.bsc.maven.reporting.model.SiteFactory;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

/**
 *
 * @author softphone
 */
public abstract class AbstractConfluenceSiteReportMojo extends AbstractConfluenceReportMojo implements SiteFactory {

    
   protected void navigateChild(final java.io.File folder, final Site.Page parentChild ) /*throws MavenReportException*/ {

        getLog().info(String.format("generateChildrenFromChild [%s]", folder.getAbsolutePath()) );

        if (folder.exists() && folder.isDirectory()) {

            folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    getLog().info(String.format("generateChildrenFromChild\n\t process file [%s]", file.getPath()) );

                    if( file.isHidden() || file.getName().charAt(0)=='.') return false ;
                    
                    if( file.isDirectory() ) {
                        Child child = new Child();

                        child.setName(file.getName());
                        child.setSource( new java.io.File(file,templateWiki.getName()));

 
                       navigateChild( file, child );    
                       
                       return true;
                    }
                    
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith( getFileExt() ) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Child child = new Child();
                    final int extensionLen = getFileExt().length();

                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    child.setSource(file);
                    

                    return false;

                }
            });
        }

    }
   
    public Site createFromFolder() {
        
        final String spaceKey = super.getSpaceKey();
        
        final Site result = new Site();
        
        result.setHome( new Site.Page() );
        
        result.getHome().getChildren().addAll( super.getChildren() );

        if (getChildrenFolder().exists() && getChildrenFolder().isDirectory()) {

            getChildrenFolder().listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {


                    if( file.isHidden() || file.getName().charAt(0)=='.') return false ;

                    if( file.isDirectory() ) {
                       
                        Site.Page parentChild = new Site.Page();

                        parentChild.setName(file.getName());
                        parentChild.setUri( new java.io.File(file,templateWiki.getName()).toURI());

                        navigateChild( file, parentChild );    
                        
                        return false;
                    }
                     
                    final String fileName = file.getName();

                    if (!file.isFile() || !file.canRead() || !fileName.endsWith(getFileExt()) || fileName.equals(templateWiki.getName())) {
                        return false;
                    }

                    Site.Page child = new Site.Page();
                    
                    final int extensionLen = getFileExt().length();
                    
                    child.setName(fileName.substring(0, fileName.length() - extensionLen));
                    child.setUri(file.toURI());

                    return false;

                }
            });
        }
        
        return result;
    }

    public Site createFromModel() {
        return null;
    }
    
}
