package org.bsc.commands;

import static org.bsc.core.MavenHelper.getOrCreateConfigurationElement;
import static org.bsc.core.MavenHelper.setMavenProjectProperty;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.apache.maven.settings.Server;
import org.bsc.core.MavenHelper;
import org.codehaus.plexus.util.IOUtil;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class Setup extends AbstractProjectCommand implements Constants
{

	@Inject @WithAttributes(label = "ServerId", required = true )
	UISelectOne<Server> serverIds;
	
	@Inject @WithAttributes(label = "Confluence EndPoint", required = true )
	UIInput<String> endPoint;
	
	@Inject @WithAttributes(label = "Space Key Home", required = true )
	UIInput<String> spaceKey;
	
	@Inject @WithAttributes(label = "Parent page title", required = true, defaultValue = "Home" )
	UIInput<String> parentPageTitle;
	
	@Inject ProjectFactory projectFactory;
	
   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.forCommand(Setup.class).name("confluence-setup")
            .category(Categories.create("Confluence"));
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
	   
		final java.util.List<Server> serverList = MavenHelper.getSettings().getServers();
		
		//Collections.sort(serverList);

		serverIds.setValueChoices(serverList);
		serverIds.setItemLabelConverter( new Converter<Server, String>() {
			
			@Override
			public String convert(Server server) {
				if( server== null ) return "";
				return server.getId();
			}
		});

		final Project project = Projects.getSelectedProject(getProjectFactory(),
				builder.getUIContext());
		
		MavenFacet mavenFacet = project.getFacet( MavenFacet.class );
		
		String result = mavenFacet.getModel().getProperties()
				.getProperty(PROP_CONFLUENCE_HOME);
		
		if( result != null ) {
			endPoint.setValue(result);
		}
		
		builder.add(serverIds);
		builder.add(endPoint);
		builder.add(spaceKey);
		builder.add(parentPageTitle);
		

   }

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

   @Override
   public Result execute(UIExecutionContext context)
   {
	   
		final UIPrompt prompt = context.getPrompt();
		final PrintStream out = context.getUIContext().getProvider().getOutput().out();
		final Project project = super.getSelectedProject(context);
		
		final MavenFacet facet = project.getFacet(MavenFacet.class);

		//final MavenProject mProject = facet.getMavenProject();
		final Model mProject = facet.getModel();

		final String siteDir = 
				String.format("%s/src/site/confluence", 
						project.getRoot().getFullyQualifiedName());

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
					final String msg = String.format("error creating folder [%s]!", siteDir);
					out.printf(msg);
					return Results.fail( msg);
				}
			} else {

				final String newFolder = prompt
						.prompt("Please, give me the site folder relative to ${basedir}. press enter to abort: ");

				if (newFolder == null || newFolder.isEmpty()) {
					out.println(MSG_SETUP_INTERRUPTED);
					return Results.fail( MSG_SETUP_INTERRUPTED);
				}
				siteDirFile = new java.io.File(mProject.getProjectDirectory(), newFolder);

			}
		}

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

			out.println(
					"error copying home page template ....! Set VERBOSE for details");

			Logger.getLogger(Setup.class.getName()).log(
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

			out.println(
					"error copying site template ....! Set VERBOSE for details");

			Logger.getLogger(Setup.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		updateOrCreateConfluenceMavenPlugin(project, mProject);
	   
      return Results.success("completed!");
   }
   
	private void updateOrCreateConfluenceMavenPlugin(final Project project, final Model mProject) {

		final CoordinateBuilder confluencePluginDep = CoordinateBuilder.create(PLUGIN_KEY_3);

		final MavenPluginFacet pluginFacet = project.getFacet(MavenPluginFacet.class);

		final MavenPlugin _plugin;
		final MavenPluginBuilder pluginBuilder;
		final ConfigurationBuilder configurationBuilder;
		

		if (pluginFacet.hasPlugin(confluencePluginDep)) {

			_plugin = pluginFacet.getPlugin(confluencePluginDep);
			pluginBuilder = MavenPluginBuilder.create(_plugin);
			configurationBuilder = ConfigurationBuilder.create(pluginBuilder.getConfig(), pluginBuilder);

		} else {
			_plugin = null;
			pluginBuilder = MavenPluginBuilder.create().setCoordinate(confluencePluginDep);
			configurationBuilder = pluginBuilder.createConfiguration();
		}

		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					configurationBuilder, CFGELEM_SERVERID);

			_elem.setText(serverIds.getValue().getId());
			
		}
		{
			final ConfigurationElementBuilder _elem = MavenHelper
					.getOrCreateConfigurationElement(configurationBuilder, CFGELEM_ENDPOINT);

			String param = endPoint.getValue();
			
			if (param.trim().endsWith("/")) {
				param = param.substring(0, param.length() - 1);
			}
			setMavenProjectProperty(project,
					PROP_CONFLUENCE_HOME, param);

			_elem.setText(String.format("${%s}/rpc/xmlrpc",
					PROP_CONFLUENCE_HOME));

		}
		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					configurationBuilder, CFGELEM_SPACEKEY);

			_elem.setText( spaceKey.getValue() );
			
		}
		{
			final ConfigurationElementBuilder _elem = getOrCreateConfigurationElement(
					configurationBuilder, CFGELEM_PARENTPAGETITLE);

			_elem.setText( parentPageTitle.getValue() );
		}

		getOrCreateConfigurationElement(configurationBuilder, "wikiFilesExt").setText(
				".confluence");
		getOrCreateConfigurationElement(configurationBuilder, "properties");

		pluginBuilder.setConfiguration(configurationBuilder);

		if (_plugin != null) {
			pluginFacet.updatePlugin(pluginBuilder);
		} else {
			pluginFacet.addPlugin(pluginBuilder);

		}
	}
  
}