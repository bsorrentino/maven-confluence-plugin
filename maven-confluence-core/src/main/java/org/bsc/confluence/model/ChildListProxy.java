/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * @author bsorrentino
 */
public class ChildListProxy implements InvocationHandler {
    
    final java.util.List<Site.Page> children; 
    final Site.Page parent;

    public static java.util.List<Site.Page> newInstance( final Site.Page parent ) {
        return (java.util.List<Site.Page>)Proxy.newProxyInstance(
                ChildListProxy.class.getClassLoader(), 
                new Class[]{java.util.List.class}, 
                new ChildListProxy(parent));
    }
    public ChildListProxy(Site.Page parent) {
        this.children = new java.util.ArrayList<Site.Page>();
        this.parent = parent;
    }
    
    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {

        if( method.getName().equals("add")) {
            
            if( os.length == 1) {
                
                Site.Page child = (Site.Page) os[0];
                child.setParent(parent);
            }
            else {
                Site.Page child = (Site.Page) os[1];
                child.setParent(parent);
                
            }
        }
        
        return method.invoke(children, os);
    }
}

