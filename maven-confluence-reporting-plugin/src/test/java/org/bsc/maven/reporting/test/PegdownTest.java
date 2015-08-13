package org.bsc.maven.reporting.test;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.Test;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SuperNode;
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
                System.out.printf( "[%s]\n", args[0].getClass().getSimpleName() );
                
                Object n = args[0];
                
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
        
        java.io.InputStream is = cl.getResourceAsStream("README.md");
                
        java.io.CharArrayWriter caw = new java.io.CharArrayWriter();
        
        for( int c = is.read(); c!=-1; c = is.read() ) {
            caw.write( c );
        }
        
        final PegDownProcessor p = new PegDownProcessor();
        
        final RootNode root = p.parseMarkdown(caw.toCharArray());
        
        root.accept(proxy);
        
    }
}
