package org.bsc.markdown.commonmark;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.hamcrest.core.IsEqual.equalTo;

public class CheatSheetTest {


    private String parse( String name ){
        try( final java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(format( "cheatsheet/%s.md", name)) ) {

            return ConfluenceWikiVisitor.parser().parse(IOUtils.toString(is));

        }
        catch( Exception e) {
            Assert.fail( e.getMessage() );
            return null;
        }

    }

    @Test
    public void parseHeaders() {

        final String wiki  = parse( "headers");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                .filter( l -> l.length() > 0)
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;
        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("h1. H1") );
        Assert.assertThat( lines.get(l++), equalTo("h2. H2") );
        Assert.assertThat( lines.get(l++), equalTo("h3. H3") );
        Assert.assertThat( lines.get(l++), equalTo("h4. H4") );
        Assert.assertThat( lines.get(l++), equalTo("h5. H5") );
        Assert.assertThat( lines.get(l++), equalTo("h6. H6") );
        Assert.assertThat( lines.get(l++), equalTo("h1. Alt-H1") );
        Assert.assertThat( lines.get(l++), equalTo("h2. Alt-H2") );

    }

    @Test
    public void parseEmphasis() {

        final String wiki  = parse( "emphasis");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                .filter( l -> l.length() > 0)
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("Emphasis, aka italics, with _asterisks_ or _underscores_.") );
        Assert.assertThat( lines.get(l++), equalTo("Strong emphasis, aka bold, with *asterisks* or *underscores*.") );
        Assert.assertThat( lines.get(l++), equalTo("Combined emphasis with *asterisks and _underscores_*.") ); // WARN: THIS IS NOT CORRECT
        Assert.assertThat( lines.get(l++), equalTo("Strikethrough uses two tildes. -Scratch this.-") );

    }

    @Test
    public void parseLists() {

        final String wiki  = parse( "lists");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                .filter( l -> l.length() > 0)
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;
        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("#  First ordered list item") );
        Assert.assertThat( lines.get(l++), equalTo("#  Another item") );
        Assert.assertThat( lines.get(l++), equalTo("#*  Unordered sub-list.") );
        Assert.assertThat( lines.get(l++), equalTo("#  Actual numbers don't matter, just that it's a number") );
        Assert.assertThat( lines.get(l++), equalTo("##  Ordered sub-list") );
        Assert.assertThat( lines.get(l++), equalTo("#  And another item.") );
        Assert.assertThat( lines.get(l++), equalTo(" You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).") );
        Assert.assertThat( lines.get(l++), equalTo(" To have a line break without a paragraph, you will need to use two trailing spaces.<<SLB>><<SLB>>") );
        Assert.assertThat( lines.get(l++), equalTo(" Note that this line is separate, but within the same paragraph.<<SLB>><<SLB>>") );
        Assert.assertThat( lines.get(l++), equalTo(" (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)") );
        Assert.assertThat( lines.get(l++), equalTo("*  Unordered list can use asterisks") );
        Assert.assertThat( lines.get(l++), equalTo("*  Or minuses") );
        Assert.assertThat( lines.get(l++), equalTo("*  Or pluses") );

    }

}
