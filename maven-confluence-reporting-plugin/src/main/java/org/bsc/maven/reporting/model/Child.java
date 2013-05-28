package org.bsc.maven.reporting.model;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * 
 * @author softphone
 *
 */
public class Child extends Site.Page {

    /**
     * 
     */
    //@MojoParameter(defaultValue = "${basedir}/src/site/confluence")
    @Parameter(defaultValue = "${basedir}/src/site/confluence")
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
