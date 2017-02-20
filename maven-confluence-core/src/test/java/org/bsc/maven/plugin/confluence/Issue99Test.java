/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.confluence;

import org.bsc.confluence.ConfluenceHtmlListUtils;
import java.util.regex.*;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author bsorrentino
 */
public class Issue99Test {

    @Ignore @Test
    public void dummy() {}

    public void tokenizeUsingRegex() {
        String line = "\"hello world\" Alexandros Alex \"I Am\" Something";
        Pattern pattern = Pattern.compile("\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b");
        Matcher matcher = pattern.matcher(line);

        int i = 0;
        while (matcher.find()) {
            System.out.println(matcher.group(0));
            ++i;
        }

        Assert.assertThat( i, IsEqual.equalTo(5));
    }

    @Test
    public void tokenizeHtmlUsingRegex() {
        final String line = new StringBuilder("this is an example of comment")
                .append( "<ol>").append('\n')
                .append("<li>item1</li>").append('\n')
                .append("<li>item2</li>").append('\n')
                .append("<li>item3</li>").append('\n')
                .append("</ol>").append('\n')
                .append( "another example")
                .append( "<ul>").append('\n')
                .append("<li>item1</li>").append('\n')
                .append("<li>item2</li>").append('\n')
                .append("<li>item3</li>").append('\n')
                .append("</ul>").append('\n')
                .append( "end example")
                .toString()
                ;

                final String result = ConfluenceHtmlListUtils.replaceHtmlList(line);

                final String expect = new StringBuilder()
                        .append("this is an example of comment")
                        .append('\n')
                        .append( "# item1" ).append('\n')
                        .append( "# item2" ).append('\n')
                        .append( "# item3" ).append('\n')
                        .append('\n')
                        .append( "another example")
                        .append('\n')
                        .append( "* item1" ).append('\n')
                        .append( "* item2" ).append('\n')
                        .append( "* item3" ).append('\n')
                        .append('\n')
                        .append( "end example")
                        .toString()
                        ;
                Assert.assertThat( result, IsEqual.equalTo(expect));


    }

    @Test
    public void tokenizeHtmlUsingRegex1() {
        final String line = new StringBuilder()
                .append( "<ul>" ).append('\n')
                .append("<li><code>&#42;&#42;/&#42;.?ar</code></li>" ).append('\n')
                .append( "<li><code>&#42;&#42;/&#42;.dll</code></li>" ).append('\n')
                .append( "</ul>" ).append('\n')
                .toString()
                ;

        final String result = ConfluenceHtmlListUtils.replaceHtmlList(line);

        final String expect = new StringBuilder()
                .append('\n')
                .append( "* <code>&#42;&#42;/&#42;.?ar</code>" ).append('\n')
                .append( "* <code>&#42;&#42;/&#42;.dll</code>" ).append('\n')
                .append('\n')
                .toString()
                ;
        Assert.assertThat( result, IsEqual.equalTo(expect));


    }

    @Test
    public void tokenizeHtmlUsingRegex2() {
        final String line = new StringBuilder()
                .append("<b>this is an example of bold</b>" ).append('\n')
                .toString()
                ;

        //final String result = line.replaceAll("<[Bb]>|</[Bb]>", "*");
        final String result = line.replaceAll("</?[Bb]>", "*");

        final String expect = new StringBuilder()
                .append( "*this is an example of bold*" ).append('\n')
                .toString()
                ;
        Assert.assertThat( result, IsEqual.equalTo(expect));


    }

    @Test
    public void tokenizeHtmlUsingRegex3() {
        final String input = "this is an example of comment";
        final String line = new StringBuilder(input)
                .toString()
                ;

        final String result =  ConfluenceHtmlListUtils.replaceHtmlList(line);

        Assert.assertThat( result , IsEqual.equalTo(input));

    }

}
