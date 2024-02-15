package org.bsc.reporting.model;

import org.bsc.confluence.model.Site;
import org.apache.maven.plugins.annotations.Parameter;

import static java.lang.String.format;

/**
 * 
 * @author bsorrentino
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
        //return format("child:name=[%s] location=[%s]", getName(), getSource());
        return format("child:name=[%s]", getName());
    }

//    @Override
//    public final java.io.File getSource() {
//        return source;
//    }

    public final void setSource(java.io.File location) {
        this.source = location;
    }

    
}
