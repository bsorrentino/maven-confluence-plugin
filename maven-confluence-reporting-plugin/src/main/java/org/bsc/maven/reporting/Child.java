package org.bsc.maven.reporting;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 
 * @author softphone
 *
 */
public class Child {

	private String name;
	
	@Parameter(defaultValue="${basedir}/src/site/confluence")
	private java.io.File source;
	
	@Override
	public String toString() {
		return String.format( "child:name=[%s] location=[%s]", getName(), getSource() );
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final java.io.File getSource() {
		return source;
	}
	public final void setSource(java.io.File location) {
		this.source = location;
	}
	
	public java.io.File getSource( MavenProject project, String ext ) {
		
		if( source==null ) {
			if( project==null ) throw new IllegalArgumentException("project is null");
			if( name==null ) throw new IllegalStateException("name is null");
			
			final String path = String.format("src/site/confluence/%s.%s", getName(), ext);
			
			source = new java.io.File( project.getBasedir(), path);
		}
		
		return source;
	}
}
