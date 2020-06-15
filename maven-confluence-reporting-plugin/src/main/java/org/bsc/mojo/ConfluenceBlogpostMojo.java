package org.bsc.mojo;

import com.google.common.io.Files;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bsc.confluence.ConfluenceService;
import org.bsc.markdown.MarkdownProcessorInfo;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;

import static java.lang.String.format;
import static org.bsc.confluence.ConfluenceService.Model;
import static org.bsc.confluence.ConfluenceService.Storage;
import static org.bsc.confluence.FileExtension.*;


/**
 * Publish a blog post
 *
 * @since 6.9
 */
@Mojo(name = "blogpost", requiresProject = false, threadSafe = true)
public class ConfluenceBlogpostMojo extends AbstractBaseConfluenceMojo {

    /**
     * The file encoding of the source files.
     *
     */
    @Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
    private String encoding;

    /**
     * Markdown processor Info<br>
     * <pre>
     *   &lt;markdownProcessor>
     *     &lt;name>processor name&lt;/name> &lt;git branch!-- default: pegdown -->
     *   &lt;/markdownProcessor>
     * </pre>
     *
     * @since 6.8
     */
    @Parameter( alias="markdownProcessor" )
    private MarkdownProcessorInfo markdownProcessorInfo = new MarkdownProcessorInfo();

    /**
     *
     * @return
     */
    private final Charset getCharset() {

        if (encoding == null) {
            getLog().debug("encoding is null! default charset will be used");
            return Charset.defaultCharset();
        }

        try {
            Charset result = Charset.forName(encoding);
            return result;

        } catch (UnsupportedCharsetException e) {
            getLog().warn(format("encoding [%s] is not valid! default charset will be used", encoding));
            return Charset.defaultCharset();

        }
    }

    @Parameter( property="blog.title" )
    private String title;

    @Parameter( property="blog.content" )
    private File content;

    @Parameter( property="blog.version", defaultValue = "0")
    private int version;

    @Override
    public void execute(ConfluenceService confluenceService) throws Exception {

        if(title==null) throw new IllegalArgumentException( "blogTitle cannot be null!. Check configuration");

        if(content==null) throw new IllegalArgumentException( "blogInfo.content cannot be null!. Check configuration");
        if( !content.exists() ) throw new IllegalArgumentException( format("blogContent [%s] doesn't exists!. Check configuration", content) );
        if( !content.isFile() ) throw new IllegalArgumentException( format("blogContent [%s] isn't a file!. Check configuration", content) );

        final Path path = content.toPath();

        final boolean isMarkdown =  MARKDOWN.isExentionOf(path);
        final boolean isStorage = XML.isExentionOf(path) || XHTML.isExentionOf(path);

        final Storage.Representation representation = ( isStorage ) ? Storage.Representation.STORAGE :  Storage.Representation.WIKI;
        getLog().debug( format("Blog representation = %s", representation ));

        final String contentData = Files.toString(content, getCharset());

        final Storage storage = Storage.of(
                                    ( isMarkdown )
                                        ? MarkdownProcessorInfo.LoadProcessor().processMarkdown(contentData)
                                        : contentData,
                                    representation);

        final Model.Blogpost blogpost = confluenceService.createBlogpost( getSpaceKey(), title, storage, version);

        confluenceService.addBlogpost( blogpost ).thenAccept( result -> {
            getLog().info( format("Blog [%s] succesfully posted!", result.getTitle()) );
        })
        .join();
    }
}
