/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maven.plugin.confluence;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.Proxy
import java.net.ProxySelector
import java.net.URI

/**
 *
 * @author bsorrentino
 */
class ProxyTest {
    
    
    @Test
    fun parseTest() {

        System.setProperty("http.proxyHost", "myproxy");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("http.nonProxyHosts", "127.0.0.1|localhost|*.company.com");
   
        run  {
            val proxyList = ProxySelector.getDefault().select(URI.create("http://localhost:8080/confluence/xmlrpc"));

            assertFalse( proxyList.isEmpty() );
            assertEquals( 1, proxyList.size );

            val p = proxyList[0];

            System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());

            assertEquals( Proxy.Type.DIRECT, p.type() );
            assertNull( p.address() );
        }
        
        run {
            val proxyList = ProxySelector.getDefault().select(URI.create("http://my.company.com:8033/confluence/xmlrpc"));

            assertFalse( proxyList.isEmpty() );
            assertEquals( 1, proxyList.size );

            val p = proxyList[0];

            System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());

            assertEquals( Proxy.Type.DIRECT, p.type() );
            assertNull( p.address() );
        }
        
        run {
            val proxyList = ProxySelector.getDefault().select(URI.create("http://www.google.com/confluence/xmlrpc"));

            assertFalse(proxyList.isEmpty());
            assertEquals(1, proxyList.size);

            val p = proxyList[0];

            System.out.printf( "type=[%s] address=[%s]\n", p.type(), p.address());

            assertEquals( java.net.Proxy.Type.HTTP, p.type());
            assertNotNull( p.address());
        }
    }
}
