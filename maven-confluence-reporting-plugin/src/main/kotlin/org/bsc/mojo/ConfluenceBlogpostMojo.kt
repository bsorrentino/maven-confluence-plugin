package org.bsc.mojo

import org.apache.maven.plugins.annotations.Mojo
import org.bsc.confluence.ConfluenceService

data class BlogpostInput(var title: String, var content:java.io.File, var version: Int = 0)

@Mojo(name = "blogpost", threadSafe = true)
class ConfluenceBlogpostMojo : AbstractBaseConfluenceMojo() {

    /**
     *
     */
    override fun execute( confluence:ConfluenceService ) {

    }
}