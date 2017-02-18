/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * @author bsorrentino
 */
public class ConfluenceHtmlListUtils {
    private static final String LIST_TAGS_PATTERN = "<ol>|<ul>|</ul>|</ol>";
    private static final String LI_TAG_PATTERN = "<li>(.+)</li>";

    static class HtmlList {
        String name;
        int start;
        int end;

    }

    protected ConfluenceHtmlListUtils() {
    }

    public static String replaceHtmlList( String line ) {
        if( line == null ) {
            throw new IllegalArgumentException("parameter source is null");
        }

        final java.io.StringWriter sw = new java.io.StringWriter(line.length());

        final java.io.PrintWriter out = new java.io.PrintWriter(sw);

        final Pattern patternLIST = Pattern.compile(LIST_TAGS_PATTERN,Pattern.CASE_INSENSITIVE );
        final Matcher matcherLIST = patternLIST.matcher(line);

        final java.util.Stack<HtmlList> stack = new java.util.Stack<HtmlList>();
        int prevEnd = 0;

        while (matcherLIST.find()) {

            final String tagName = matcherLIST.group(0);

            final boolean isOpen = !tagName.startsWith("</");

            if( isOpen ) {

                final HtmlList tag = new HtmlList();

                tag.name = tagName.toLowerCase();
                tag.start = matcherLIST.start();

                stack.push(tag);

                if( prevEnd < tag.start) out.print( line.substring(prevEnd, tag.start));
            }
            else {

                final HtmlList tag = stack.pop();

                prevEnd = tag.end = matcherLIST.end();

                if( tagName.equalsIgnoreCase("</ul>")) {

                    final String l = line.substring(tag.start, tag.end)
                                        .replaceAll("</?[Uu][Ll]>", "")
                                        .replaceAll(LI_TAG_PATTERN,"* $1")
                                         ;
                    out.print( l );
                }
                else if( tagName.equalsIgnoreCase("</ol>")) {

                    final String l = line.substring(tag.start, tag.end)
                                         .replaceAll("</?[Oo][Ll]>", "")
                                         .replaceAll(LI_TAG_PATTERN,"# $1")
                                         ;
                    out.print( l );
                }

            }
        }

        out.print( line.substring(prevEnd));

        out.flush();

        return sw.toString();

    }
}
