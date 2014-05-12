package org.bsc.commands;

import java.io.PrintStream;

import javax.inject.Inject;

import org.apache.maven.settings.Proxy;
import org.bsc.core.Fe;
import org.bsc.core.MavenHelper;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.ConfluenceFactory;
import org.codehaus.swizzle.confluence.Page;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class DownloadPage extends AbstractProjectCommand implements Constants {

	@Inject @WithAttributes(label = "Username", required = true )
	UIInput<String> username;

	@Inject @WithAttributes(label = "Password", required = true, type = InputType.SECRET )
	UIInput<String> password;

	@Inject
	@WithAttributes(label = "Target", required = true, type = InputType.DIRECTORY_PICKER)
	private UIInput<DirectoryResource> target;

	@Inject
	ProjectFactory projectFactory;

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(DownloadPage.class)
				.name("confluence-downloadPage")
				.category(Categories.create("Confluence"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		final Project project = Projects.getSelectedProject(getProjectFactory(),
				builder.getUIContext());
		
		target.setDefaultValue((DirectoryResource) project.getRoot());
		
		builder.add(username);
		builder.add(password);
		builder.add(target);
		
	}

	@Override
	public Result execute(UIExecutionContext context) {

		final UIPrompt prompt = context.getPrompt();
		final PrintStream out = context.getUIContext().getProvider().getOutput().out();

		final Project project = super.getSelectedProject(context);

		final MavenFacet facet = project.getFacet(MavenFacet.class);

		final CoordinateBuilder confluencePluginDep = CoordinateBuilder.create(PLUGIN_KEY_3);

		final MavenPluginFacet pluginFacet = project.getFacet(MavenPluginFacet.class);

		if (!pluginFacet.hasPlugin(confluencePluginDep)) {
			throw new IllegalStateException(String.format(
					"Project hasn't defined Plugin [%s]", PLUGIN_KEY_3));
		}

		final MavenPlugin plugin = pluginFacet.getPlugin(confluencePluginDep);

		final Configuration conf = plugin.getConfig();

		final String endPoint = conf.getConfigurationElement(CFGELEM_ENDPOINT)
				.getText();
		final String space = conf.getConfigurationElement(CFGELEM_SPACEKEY)
				.getText();

		final String title = conf.getConfigurationElement("title").getText();

		confluenceExecute(facet.resolveProperties(endPoint), 
							username.getValue(),
							password.getValue(), 
				new Fe<Confluence, Void>() {

					@Override
					public Void f(Confluence c) throws Exception {

						final Page page = c.getPage(space, title);

						out.println();
						out.print(page.getContent());
						out.println();

						return null;
					}

				});
		
		return Results.success("completed!");

	}

	/**
	 * 
	 * @param endpoint
	 * @param username
	 * @param password
	 * @param task
	 */
	protected void confluenceExecute(String endpoint, String username,
			String password, Fe<Confluence, Void> task) {

		Confluence confluence = null;

		try {

			Confluence.ProxyInfo proxyInfo = null;

			final Proxy activeProxy = MavenHelper.getSettings()
					.getActiveProxy();

			if (activeProxy != null) {

				proxyInfo = new Confluence.ProxyInfo(activeProxy.getHost(),
						activeProxy.getPort(), activeProxy.getUsername(),
						activeProxy.getPassword());
			}

			confluence = ConfluenceFactory.createInstanceDetectingVersion(
					endpoint, proxyInfo, username, password);

			// getLog().info(ConfluenceUtils.getVersion(confluence));

			task.f(confluence);

		} catch (Exception e) {

			// getLog().error("has been imposssible connect to confluence due exception",
			// e);

			throw new Error(
					"has been imposssible connect to confluence due exception",
					e);
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
				// getLog().warn("confluence logout has failed!");
			}
		} catch (Exception e) {
			// getLog().warn("confluence logout has failed due exception ", e);
		}

	}

}