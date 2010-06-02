package org.bsc.maven.reporting;

import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import biz.source_code.miniTemplator.MiniTemplator;

public abstract class AbstractConfluenceReportMojo extends AbstractMavenReport {
	/**
	 * Confluence end point url 
	 */
	@MojoParameter(expression="${confluence.endPoint}", defaultValue="http://localhost:8080/rpc/xmlrpc")
	private String endPoint;

	/**
	 * Confluence target confluence's spaceKey 
	 */
	@MojoParameter(expression="${confluence.spaceKey}", required=true)
	private String spaceKey;

	/**
	 * Confluence target confluence's spaceKey 
	 */
	@MojoParameter(expression="${confluence.parentPage}",defaultValue="Home")
	private String parentPageTitle;
	
	/**
	 * Confluence username 
	 */
	@MojoParameter(expression="${confluence.userName}",defaultValue="admin")
	private String username;

	/**
	 * Confluence password 
	 */
	@MojoParameter(expression="${confluence.password}")
	private String password;
	
	@MojoParameter( expression="${project}", readonly=true, required=true)
	protected MavenProject project;

	@MojoParameter(defaultValue="${basedir}/src/site/confluence/template.wiki", description="MiniTemplator source. Default location is ${basedir}/src/site/confluence")
	protected java.io.File templateWiki;
	
	@MojoParameter(description="child pages - &lt;child&gt;&lt;name/&gt;[&lt;source/&gt]&lt;/child&gt")
	private java.util.List<Child> children;
	
	
	
	public AbstractConfluenceReportMojo() {
		children = Collections.emptyList();
	}

	public final String getEndPoint() {
		return endPoint;
	}

	public final String getSpaceKey() {
		return spaceKey;
	}

	public final String getParentPageTitle() {
		return parentPageTitle;
	}

	public final String getUsername() {
		return username;
	}

	public final String getPassword() {
		return password;
	}

	@Override
	public MavenProject getProject() {
		return project;
	}

	/**
	 * 
	 * 
	 */
	protected void generateChildren(Confluence confluence, String spaceKey, String parentPageTitle, String titlePrefix ) /*throws MavenReportException*/ {
		
		getLog().info( String.format( "generateChildren # [%d]", children.size()) );

		for( Child child : (java.util.List<Child>)children ) {
		
			java.io.File source = child.getSource(getProject());
			
			getLog().info( child.toString() );
			
			try {
				
				final MiniTemplator t = new MiniTemplator(new java.io.FileReader(source));
				
	            Page p = ConfluenceUtils.getOrCreatePage( confluence, spaceKey, parentPageTitle, String.format("%s - %s", titlePrefix, child.getName())  );
	            
	            p.setContent(t.generateOutput());
	            
	            confluence.storePage(p);

				
			} catch (Exception e) {
				final String msg = "error loading template";
				getLog().error(msg,e);
				//throw new MavenReportException(msg, e);
			}
		}
		
	}
	
}
