package org.bsc.maven.reporting;

import org.apache.maven.project.MavenProject;
import org.bsc.maven.reporting.model.Site;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 * 
 * @author softphone
 *
 */
public class Child extends Site.Page {

    @MojoParameter(defaultValue = "${basedir}/src/site/confluence")
    private java.io.File source;

    @Override
    public String toString() {
        return String.format("child:name=[%s] location=[%s]", getName(), getSource());
    }

    @Override
    public final java.io.File getSource() {
        return source;
    }

    public final void setSource(java.io.File location) {
        this.source = location;
    }

    
}
