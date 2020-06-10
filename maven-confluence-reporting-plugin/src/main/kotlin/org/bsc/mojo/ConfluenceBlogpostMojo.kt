package org.bsc.mojo

import org.apache.maven.plugins.annotations.Mojo
import org.bsc.confluence.ConfluenceService


@Mojo(name = "blogpost", threadSafe = true)
class ConfluenceBlogpostMojo : AbstractBaseConfluenceMojo() {

    /**
     *
     */
    override fun execute( confluence:ConfluenceService ) {

    }
}