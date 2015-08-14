package org.bsc.maven.plugin.confluence;

import org.bsc.markdown.ToConfluenceSerializer;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.Visitor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author softphone
 */
public class PegdownTest {
    
    private static final String FILES[] = { "README.md", "TEST1.md" };
    
    private char[] loadResource( String name ) throws IOException {
        
        final ClassLoader cl = PegdownTest.class.getClassLoader();

        final java.io.InputStream is = cl.getResourceAsStream(name);
        try {
            
            java.io.CharArrayWriter caw = new java.io.CharArrayWriter();
            
            for( int c = is.read(); c!=-1; c = is.read() ) {
                caw.write( c );
            }
            
            return caw.toCharArray();
            
        }
        finally {
            IOUtils.closeQuietly(is);
        }
        
    }
    
    @Test
    public void parseTest() throws IOException {
                
        InvocationHandler handler = new InvocationHandler() {
        
            int indent = 0;
            
            protected void visitChildren(Object proxy, Node node ) {
                    for (Node child : node.getChildren()) {
                        child.accept((Visitor) proxy);
                    }
            }
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                
                for( int i = 0; i <indent ; ++i ) System.out.print('\t');               
                final Object n = args[0];

                System.out.printf( "[%s]\n", n );
                
                
                if( n instanceof Node ) {
                    ++indent;
                    visitChildren(proxy, (Node)args[0]);
                    --indent;
                }
                return null;
            }
            
        };
        
        final ClassLoader cl = PegdownTest.class.getClassLoader();
        
        final Visitor proxy = (Visitor) Proxy.newProxyInstance(
                            cl,
                            new Class[] { Visitor.class },
                            handler);
        
        
        final PegDownProcessor p = new PegDownProcessor();
        
        final RootNode root = p.parseMarkdown(loadResource(FILES[1]));
        
        root.accept(proxy);
        
    }
    
    @Test
    public void serializerTest() throws IOException {
                
        final ClassLoader cl = PegdownTest.class.getClassLoader();
        
        final PegDownProcessor p = new PegDownProcessor();
        
        final RootNode root = p.parseMarkdown(loadResource(FILES[1]));
        
        ToConfluenceSerializer ser =  new ToConfluenceSerializer();
        
        root.accept( ser );
        
        System.out.println( ser.toString() );
        
    }
}
