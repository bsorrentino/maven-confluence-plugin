package org.bsc.confluence;

import static java.util.Optional.ofNullable;

import java.nio.file.Path;

public enum FileExtension {

    MARKDOWN(".md"),
    XML(".xml"),
    XHTML(".xhml") 
   ;
    
    private String pattern;

    FileExtension(String pattern) {
        this.pattern = pattern;
    }
    
    public boolean isExentionOf( String path ) {
        return ofNullable(path)
                .map( p -> p.toLowerCase().endsWith(pattern))
                .orElse(false);
    }
    
    public boolean isExentionOf( Path path ) {
        return ofNullable(path)
                .map( p -> p.getFileName().toString().toLowerCase())
                .map( p -> p.endsWith(pattern))
                .orElse(false);
    }
    
}
