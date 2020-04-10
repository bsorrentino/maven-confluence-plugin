package org.bsc.markdown.commonmark;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
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
    //@Ignore
    public void parseHeaders() {

        final String wiki  = parse( "headers");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
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
    //@Ignore
    public void parseEmphasis() {

        final String wiki  = parse( "emphasis");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("Emphasis, aka italics, with _asterisks_ or _underscores_.") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Strong emphasis, aka bold, with *asterisks* or *underscores*.") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Combined emphasis with *asterisks and _underscores_*.") ); // WARN: THIS IS NOT CORRECT
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Strikethrough uses two tildes. -Scratch this.-") );

    }

    @Test
    //@Ignore
    public void parseLists() {

        final String wiki  = parse( "lists");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
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
        Assert.assertThat( lines.get(l++), equalTo(" To have a line break without a paragraph, you will need to use two trailing spaces. Note that this line is separate, but within the same paragraph. (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)") );
        Assert.assertThat( lines.get(l++), equalTo("*  Unordered list can use asterisks") );
        Assert.assertThat( lines.get(l++), equalTo("*  Or minuses") );
        Assert.assertThat( lines.get(l++), equalTo("*  Or pluses") );

    }

    @Test
    //@Ignore
    public void parseImages() {

        final String wiki  = parse( "images");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        l+=2;
        Assert.assertThat( lines.get(l++), equalTo("Inline-style:!https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png!") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Reference-style:!https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png!") );

    }

    @Test
    //@Ignore
    public void parseBlockquote() {

        final String wiki  = parse( "blockquote");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("{quote}") );
        Assert.assertThat( lines.get(l++), equalTo("Blockquotes are very handy in email to emulate reply text.This line is part of the same quote.") );
        Assert.assertThat( lines.get(l++), equalTo("{quote}") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Quote break.") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("{quote}") );
        Assert.assertThat( lines.get(l++), equalTo("This is a very long line that will still be quoted properly when it wraps. Oh boy let's keep writing to make sure this is long enough to actually wrap for everyone. Oh, you can _put_ *Markdown* into a blockquote.") );
        Assert.assertThat( lines.get(l++), equalTo("{quote}") );

    }

    @Test
    //@Ignore
    public void parseLinks() {

        final String wiki  = parse( "links");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("[I'm an inline-style link|https://www.google.com]") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("[I'm an inline-style link with title|https://www.google.com|Google's Homepage]") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("[I'm a reference-style link|https://www.mozilla.org]") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("[I'm a relative reference to a repository file|../blob/master/LICENSE]") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("[You can use numbers for reference-style link definitions|http://slashdot.org]") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("Or leave it empty and use the [link text itself|http://www.reddit.com].") );
        l ++;
        Assert.assertThat( lines.get(l++), equalTo("URLs and URLs in angle brackets will automatically get turned into links.http://www.example.com or [http://www.example.com|http://www.example.com] and sometimesexample.com (but not on Github, for example).") );

    }

    @Test
    //@Ignore
    public void parseCode() {

        final String wiki  = parse( "code");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("Inline {{code}} has {{back-ticks around}} it.") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("{code:javascript}") );
        Assert.assertThat( lines.get(l++), equalTo("var s = \"JavaScript syntax highlighting\";") );
        Assert.assertThat( lines.get(l++), equalTo("alert(s);") );
        Assert.assertThat( lines.get(l++), equalTo("{code}") );
        Assert.assertThat( lines.get(l++), equalTo("{code:python}") );
        Assert.assertThat( lines.get(l++), equalTo("s = \"Python syntax highlighting\"") );
        Assert.assertThat( lines.get(l++), equalTo("print s") );
        Assert.assertThat( lines.get(l++), equalTo("{code}") );
        Assert.assertThat( lines.get(l++), equalTo("h3. Shell") );
        Assert.assertThat( lines.get(l++), equalTo("{code}") );
        Assert.assertThat( lines.get(l++), equalTo("No language indicated, so no syntax highlighting. ") );
        Assert.assertThat( lines.get(l++), equalTo("But let's throw in a <b>tag</b>.") );
        Assert.assertThat( lines.get(l++), equalTo("{code}") );

    }

    @Test
    //@Ignore
    public void parseHorizontalrule() {

        final String wiki  = parse( "horizontalrule");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 0;
        Assert.assertThat( lines.get(l++), equalTo("Three or more...") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("---") );
        Assert.assertThat( lines.get(l++), equalTo("Hyphens") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("---") );
        Assert.assertThat( lines.get(l++), equalTo("Asterisks") );
        l++;
        Assert.assertThat( lines.get(l++), equalTo("---") );
        Assert.assertThat( lines.get(l++), equalTo("Underscores" ) );

    }

    @Test
    //@Ignore
    public void parseTables() {

        final String wiki  = parse( "tables");

        final List<String> lines = Arrays.stream(wiki.split( "\n"))
                //.peek( System.out::println )
                .collect(Collectors.toList())
                ;

        int l = 2;
        Assert.assertThat( lines.get(l++), equalTo("||Tables||Are||Cool||") );
        Assert.assertThat( lines.get(l++), equalTo("|col 3 is|right-aligned|$1600|") );
        Assert.assertThat( lines.get(l++), equalTo("|col 2 is|centered|$12|") );
        Assert.assertThat( lines.get(l++), equalTo("|zebra stripes|are neat|$1|") );
        l+=2;
        Assert.assertThat( lines.get(l++), equalTo("||Markdown||Less||Pretty||") );
        Assert.assertThat( lines.get(l++), equalTo("|_Still_|{{renders}}|*nicely*|" ) );
        Assert.assertThat( lines.get(l++), equalTo("|1|2|3|" ) );

    }

}
