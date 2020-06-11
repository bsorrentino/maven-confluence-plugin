package org.bsc.mojo

import org.apache.maven.plugins.annotations.Mojo
import org.bsc.confluence.ConfluenceService

data class BlogEntryInput(var author: String, var title: String, var url: String, var version: Int)

@Mojo(name = "blogpost", threadSafe = true)
class ConfluenceBlogpostMojo : AbstractBaseConfluenceMojo() {

    /**
     *
     */
    override fun execute( confluence:ConfluenceService ) {

    }
}