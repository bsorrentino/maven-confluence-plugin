package org.bsc.markdown.pegdown;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue226Test extends PegdownParse {


    @Override
    protected char[] loadResource() throws IOException {
        return super.loadResource("issue226.md");
    }

    @Test
    public void parse() throws Exception {
        final String [] lines = serializeToString( createPage("home", "./home.md") ).split( "\n" );

        Arrays.asList(lines).forEach( l -> System.out.printf( "-%s\n",l) );

        int i=2;
        assertEquals( "h2.Properties", lines[i++]);
        i++;
        assertEquals( "||name ||description ||type ||default values ||since ||", lines[i++]);
        //assertEquals( "|packagePatterns |regular expression |[String\\[\\]|https://checkstyle.sourceforge.io/property_types.html#String.5B.5D] |{{[]}}|1.2.0 |", lines[i++]);
        //assertEquals( "|annotationNames |full qualified class name or simple class name of an annotation |[String\\[\\]|https://checkstyle.sourceforge.io/property_types.html#String.5B.5D]|{{[]}} |1.0.0 |", lines[i++]);

    }
}
