package org.bsc.commands;

import org.apache.maven.settings.Proxy;
import org.bsc.core.Fe;
import org.bsc.core.MavenHelper;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

import java.lang.Override;
import java.lang.Exception;

public class DownloadPage extends AbstractUICommand
{

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.forCommand(DownloadPage.class).name("confluence-downloadPage")
            .category(Categories.create("Confluence"));
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
   }

   @Override
   public Result execute(UIExecutionContext context)
   {
      return Results.fail("Not implemented!");
   }
   
   /*	
   @Command(value="downloadPage", help="download page content")
   public void downloadPage(
           @Option(help="username") String username, 
           @Option(help="password") String password
           )
   {
	final MavenCoreFacet facet = project.getFacet(MavenCoreFacet.class);

	final MavenProject mProject = facet.getMavenProject(); 
           
           final DependencyBuilder confluencePluginDep = DependencyBuilder
			.create(PLUGIN_KEY_3);

	final MavenPluginFacet pluginFacet = project
			.getFacet(MavenPluginFacet.class);
           
           if( !pluginFacet.hasPlugin(confluencePluginDep)) {
               throw new IllegalStateException( 
                       String.format("Project hasn't defined Plugin [%s]", PLUGIN_KEY_3));
           }

          final  MavenPlugin plugin = pluginFacet.getPlugin(confluencePluginDep);
          
          
          final Configuration conf = plugin.getConfig();
                         
          final String endPoint = 
                  conf.getConfigurationElement(CFGELEM_ENDPOINT).getText();
          final String space = 
                  conf.getConfigurationElement(CFGELEM_SPACEKEY).getText();
          
          
          final String title = 
                  conf.getConfigurationElement("title").getText();
          
          if( username == null ) {
               username = prompt.prompt("username: ");
          }
          if( password == null ) {
           password = prompt.promptSecret("password: ");
          }
          
          confluenceExecute( facet.resolveProperties(endPoint), 
                                       username, 
                                       password, 
                                       new Fe<Confluence,Void>() {

                @Override
                public Void f(Confluence c) throws Exception {
                    
                    if( prompt.isVerbose() ) {
                       prompt.println( String.format("opening page\n\tspace=[%s] title=[%s]", space, title) );
                    }
                    
                    final Page page = c.getPage(space, title);
                    
                    System.err.println();
                    System.err.print( page.getContent() );
                    System.err.println();
                    
                    return null;             
                }
              
          });
          
          
   }

*/        
/**
* 
* @param endpoint
* @param username
* @param password
* @param task 
*/
protected void confluenceExecute(   String endpoint, 
                                   String username,
                                   String password,
                                   Fe<Confluence,Void> task  )  {
   
   Confluence confluence = null;

   try {
       
       Confluence.ProxyInfo proxyInfo = null;
       
       final Proxy activeProxy = MavenHelper.getSettings().getActiveProxy();

       if( activeProxy!=null ) {
           
           proxyInfo = 
                   new Confluence.ProxyInfo( 
                           activeProxy.getHost(),
                           activeProxy.getPort(), 
                           activeProxy.getUsername(), 
                           activeProxy.getPassword()
                           );
       }
       
       confluence = ConfluenceFactory.createInstanceDetectingVersion(
               endpoint, 
               proxyInfo, 
               username, 
               password);

       //getLog().info(ConfluenceUtils.getVersion(confluence));

       task.f(confluence);
       
   } catch (Exception e) {
       
       //getLog().error("has been imposssible connect to confluence due exception", e);
       
       throw new Error( "has been imposssible connect to confluence due exception",e);
   } finally {
       confluenceLogout(confluence);
   }
   
}

/**
* 
* @param confluence
*/
private void confluenceLogout(Confluence confluence) {

   if (null == confluence) {
       return;
   }

   try {
       if (!confluence.logout()) {
           //getLog().warn("confluence logout has failed!");
       }
   } catch (Exception e) {
       //getLog().warn("confluence logout has failed due exception ", e);
   }


}

   
}