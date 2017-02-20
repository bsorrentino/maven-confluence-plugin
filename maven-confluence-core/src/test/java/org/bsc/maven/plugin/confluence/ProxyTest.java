/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.plugin.confluence;

import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author bsorrentino
 */
public class ProxyTest {
    
    
    @Test
    public void parseTest() {

        System.setProperty("http.proxyHost", "myproxy");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("http.nonProxyHosts", "127.0.0.1|localhost|*.company.com");
   
        {
        final List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("http://localhost:8080/confluence/xmlrpc"));      
        
        Assert.assertThat( proxyList.isEmpty(), Is.is(false));
        Assert.assertThat( proxyList.size(), Is.is(1));
        
        final Proxy p = proxyList.get(0);
        
        System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());
        
        Assert.assertThat( p.type(), Is.is(Type.DIRECT));
        Assert.assertThat( p.address(), IsNull.nullValue());
        }
        
        {
        final List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("http://my.company.com:8033/confluence/xmlrpc"));      
        
        
        Assert.assertThat( proxyList.isEmpty(), Is.is(false));
        Assert.assertThat( proxyList.size(), Is.is(1));
        
        final Proxy p = proxyList.get(0);
        
        System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());
        
        Assert.assertThat( p.type(), Is.is(Type.DIRECT));
        Assert.assertThat( p.address(), IsNull.nullValue());
        }
        
        {
        final List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("http://www.google.com/confluence/xmlrpc"));      
        
        Assert.assertThat( proxyList.isEmpty(), Is.is(false));
        Assert.assertThat( proxyList.size(), Is.is(1));
        
        final Proxy p = proxyList.get(0);
        
        System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());

        Assert.assertThat( p.type(), Is.is(Type.HTTP));
        Assert.assertThat( p.address(), IsNull.notNullValue());
        }
    }
}
