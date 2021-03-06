/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.markdown.pegdown;

import java.io.IOException;

/**
 *
 * @author bsorrentino
 */
public class CheatsheetPergdownParseTest extends PegdownParse {

        
    private static final String FILE = "cheatsheet.md";

    @Override
    protected char[] loadResource() throws IOException {
        return super.loadResource(FILE);
    }
    
    public static void main( String args[] ) throws Exception  {
        
        final CheatsheetPergdownParseTest test = new CheatsheetPergdownParseTest();
        
        final String result = test.serializeToString( test.createPage( "TEST", "./test.md") );
        
        System.out.println( result );
    }
    
}
