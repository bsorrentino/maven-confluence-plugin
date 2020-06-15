package org.bsc.mojo;


import org.bsc.confluence.ConfluenceService.Storage.Representation;

import java.io.File;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class BlogInputInfo {
    private String title = null;
    private java.io.File content = null;
    private int version = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new StringBuilder("BlogInputInfo {\n")
                    .append( format( "title='%s';\n", title  ))
                    .append( format( "content=%s;\n", valueOf(content)  ))
                    .append( format( "version=%d;\n", version  ))
                    .append("}\n")
                .toString();
    }
}
