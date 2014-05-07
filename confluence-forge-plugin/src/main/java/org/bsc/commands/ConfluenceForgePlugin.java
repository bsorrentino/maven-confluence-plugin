package org.bsc.commands;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Proxy;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElement;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.shell.Shell;
import org.jboss.forge.addon.ui.annotation.Command;
import org.jboss.forge.addon.ui.annotation.Option;

/**
 *
 */
public class ConfluenceForgePlugin  {

	public static final String PROP_CONFLUENCE_HOME = "confluence.home";
	public static final String CFGELEM_SERVERID = "serverId";
	public static final String CFGELEM_ENDPOINT = "endPoint";
	public static final String CFGELEM_SPACEKEY = "spaceKey";
	public static final String CFGELEM_PARENTPAGETITLE = "parentPageTitle";
	public static final String MSG_SETUP_INTERRUPTED = "setup interrupted!";
	public static final String MESG_FOLDER_CREATED = "folder created!";

	public static final String PLUGIN_GROUPID = "org.bsc.maven";
	public static final String PLUGIN_ARTIFACTID = "maven-confluence-reporting-plugin";
	public static final String PLUGIN_VERSION = "3.4.3";

	public static final String PLUGIN_KEY_2 = PLUGIN_GROUPID + ":"
			+ PLUGIN_ARTIFACTID;
	public static final String PLUGIN_KEY_3 = PLUGIN_GROUPID + ":"
			+ PLUGIN_ARTIFACTID + ":" + PLUGIN_VERSION;

	@Inject
	private Shell/* Prompt */prompt;

	@Inject
	private Project project;

	//@Inject
	//private org.jboss.forge.maven.facets.MavenContainer mavenContainer;

	// @Inject
	// private ResourceFactory resourceFactory;

	public void setup() {

		final MavenCoreFacet facet = project.getFacet(MavenCoreFacet.class);

		final MavenProject mProject = facet.getMavenProject();

		final String siteDir = String.format("%s/src/site/confluence", project
				.getProjectRoot().getFullyQualifiedName());

		java.io.File siteDirFile = new java.io.File(siteDir);

		// final DirectoryResource root = project.getProjectRoot();
		// final DirectoryResource siteDirRes = root.createFrom(siteDirFile);
		while (!siteDirFile.exists()) {

			final boolean createFolder = prompt.promptBoolean(String.format(
					"Do you want create missing folder [%s]: ",
					siteDirFile.getPath()), true);

			if (createFolder) {

				final boolean success = siteDirFile.mkdirs();

				if (success) {
					out.println(MESG_FOLDER_CREATED);
					break;
				} else {
					out.println(ShellColor.RED, String.format(
							"error creating folder [%s]!", siteDir));
					return;
				}
			} else {

				final String newFolder = prompt
						.prompt("Please, give me the site folder relative to ${basedir}. press enter to abort: ");

				if (newFolder == null || newFolder.isEmpty()) {
					out.println(MSG_SETUP_INTERRUPTED);
					return;
				}
				siteDirFile = new java.io.File(mProject.getBasedir(), newFolder);

			}
		}

		// DirectoryResource siteDirRes = (DirectoryResource)
		// resourceFactory.getResourceFrom(siteDirFile);
		try {
			final java.io.File f = new java.io.File(siteDirFile,
					"home.confluence");

			if (!f.exists()) {

				final java.io.InputStream confluenceTemplatePage = getClass()
						.getClassLoader().getResourceAsStream(
								"template.confluence");
				final java.io.Writer confluenceHomePage = new FileWriter(f);

				IOUtil.copy(confluenceTemplatePage, confluenceHomePage);
			}

		} catch (IOException ex) {

			out.println(ShellColor.RED,
					"error copying home page template ....! Set VERBOSE for details");

			printVerbose(ex);

			Logger.getLogger(ConfluenceForgePlugin.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		try {
			final java.io.File f = new java.io.File(siteDirFile, "site.xml");

			if (!f.exists()) {
				final java.io.InputStream siteTemplatePage = getClass()
						.getClassLoader().getResourceAsStream("site.xml");
				final java.io.Writer sitePage = new FileWriter(f);

				IOUtil.copy(siteTemplatePage, sitePage);
			}

		} catch (IOException ex) {

			out.println(ShellColor.RED,
					"error copying site template ....! Set VERBOSE for details");

			printVerbose(ex);

			Logger.getLogger(ConfluenceForgePlugin.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		updateOrCreateConfluenceMavenPlugin(mProject);
	}

	private void updateOrCreateConfluenceMavenPlugin(final MavenProject mProject) {

		final DependencyBuilder confluencePluginDep = DependencyBuilder
				.create(PLUGIN_KEY_3);

		final MavenPluginFacet pluginFacet = project
				.getFacet(MavenPluginFacet.class);

		final MavenPlugin _plugin;
		final MavenPluginBuilder pb;
		final ConfigurationBuilder cb;

		if (pluginFacet.hasPlugin(confluencePluginDep)) {

			_plugin = pluginFacet.getPlugin(confluencePluginDep);
			pb = MavenPluginBuilder.create(_plugin);
			cb = ConfigurationBuilder.create(_plugin.getConfig(), pb);

		} else {
			_plugin = null;
			pb = MavenPluginBuilder.create().setDependency(confluencePluginDep);
			cb = ConfigurationBuilder.create(pb);
		}

		final F<ConfigurationElement, String> _getter = new F<ConfigurationElement, String>() {
			@Override
			public String f(ConfigurationElement param) {
				return param.getText();
			}
		};

		final ShellPromptBuilder<String> spsb = new ShellPromptStringBuilder()
				.setInterruptMessage(MSG_SETUP_INTERRUPTED);

		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					cb, CFGELEM_SERVERID);

			_elem.setText(new ShellPromptServersBuilder(MavenHelper.getSettings())
					.setGetter(_getter)
					.setMessage(
							"Please, give me the serverId for access to confluence")
					.input(prompt, _elem));

		}
		{
			final ConfigurationElementBuilder _elem = MavenHelper
					.getOrCreateConfigurationElement(cb, CFGELEM_ENDPOINT);

			_elem.setText(spsb
					.setMessage("Please, give me the confluence's 'URL'")
					.setGetter(new F<ConfigurationElementBuilder, String>() {

						@Override
						public String f(ConfigurationElementBuilder param) {

							String result = mProject.getProperties()
									.getProperty(PROP_CONFLUENCE_HOME);

							if (result == null) {
								result = param.getText();
							}
							return result;
						}
					}).setTransformer(new F<String, String>() {
						@Override
						public String f(String param) {

							if (param.trim().endsWith("/")) {
								param = param.substring(0, param.length() - 1);
							}

							setMavenProjectProperty(project,
									PROP_CONFLUENCE_HOME, param);

							return String.format("${%s}/rpc/xmlrpc",
									PROP_CONFLUENCE_HOME);
						}
					}).input(prompt, _elem));

		}
		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					cb, CFGELEM_SPACEKEY);

			_elem.setText(spsb.setGetter(_getter)
					.setMessage("Please, give me the confluence's 'SPACE KEY'")
					.input(prompt, _elem));
		}
		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					cb, CFGELEM_PARENTPAGETITLE);

			_elem.setText(spsb
					.setGetter(_getter)
					.setMessage(
							"Please, give me the confluence's 'PARENT PAGE NAME'")
					.setDefaultValue("Home").input(prompt, _elem));
		}

		getOrCreateConfigurationElement(cb, "wikiFilesExt").setText(
				".confluence");
		getOrCreateConfigurationElement(cb, "properties");

		pb.setConfiguration(cb);

		if (_plugin != null) {
			pluginFacet.updatePlugin(pb);
		} else {
			pluginFacet.addPlugin(pb);

		}

	}

	private void printVerbose(Throwable t) {

		java.io.StringWriter sw = new java.io.StringWriter(4096);
		java.io.PrintWriter w = new java.io.PrintWriter(sw);
		t.printStackTrace(w);

		prompt.printlnVerbose(ShellColor.RED, sw.toString());
	}

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
            
            throw new AbortedException( "has been imposssible connect to confluence due exception",e);
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
