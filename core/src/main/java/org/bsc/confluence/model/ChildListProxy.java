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
public class ChildListProxy<T extends Site.IPageContainer> implements InvocationHandler {
    
    final java.util.List<Site.Page> children; 
    final T parent;

    /**
     *
     * @param parent
     * @param <T>
     * @return
     */

    public static <T extends Site.IPageContainer> java.util.List<Site.Page> newInstance(final T parent )
    {
        return (java.util.List<Site.Page>)Proxy.newProxyInstance(
                ChildListProxy.class.getClassLoader(), 
                new Class[]{java.util.List.class}, 
                new ChildListProxy<T>(parent));
    }

    public ChildListProxy(final T parent) {
        this.children = new java.util.ArrayList<>();
        this.parent = parent;
    }
    
    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {

        if( method.getName().equals("add")) {
            final Site.Page child = (Site.Page)os[ os.length - 1];
            child.setParent(parent);
        }
        
        return method.invoke(children, os);
    }
}

