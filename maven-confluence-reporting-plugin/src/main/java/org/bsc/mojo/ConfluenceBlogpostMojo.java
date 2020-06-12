package org.bsc.mojo;

import lombok.Value;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;


@Value
class BlogInputInfo {
    String title;
    java.io.File content;
    int version = 0;
}

/**
 * Publish a blog post
 *
 * @since 6.9
 */
@Mojo(name = "blogpost", threadSafe = true)
public class ConfluenceBlogpostMojo extends AbstractBaseConfluenceMojo {


    /**
     * ScrollVersions addon configuration. Below the template
     *
     * <pre>
     * &lt;blogInfo>
     *  &lt;title>blog title</title>  &lt;!-- mandatory -->
     *  &lt;content>path of blog content</content>  &lt;!-- mandatory -->
     *  &lt;version>version number</title>  &lt;!-- optional -->
     * &lt;/blogInfo>
     * </pre>
     *
     */
    @Parameter( name = "blogInfo")
    private BlogInputInfo blogInfo;

    @Override
    public void execute(ConfluenceService confluenceService) throws Exception {


    }
}
