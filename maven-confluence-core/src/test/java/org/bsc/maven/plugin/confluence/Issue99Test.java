/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.confluence;

import java.util.regex.*;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author softphone
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
                .append( "<ol><li>item1</li><li>item2</li><li>item3</li></ol>")
                .append( "another example")
                .append( "<ul><li>item4</li><li>item5</li><li>item6</li></ul>")
                .append( "end example")
                .toString()
                ;
        
        System.out.println( ConfluenceHtmlListUtils.replaceHtmlList(line));
        
        
    }
    
    @Test
    public void tokenizeHtmlUsingRegex2() {
        final String input = "this is an example of comment";
        final String line = new StringBuilder(input)
                .toString()
                ;
        
        final String result =  ConfluenceHtmlListUtils.replaceHtmlList(line);
     
        Assert.assertThat( result , IsEqual.equalTo(input));
        
    }

}
