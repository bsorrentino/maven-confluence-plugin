/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;


/**
 *
 * @author bsorrentino
 */
public class ConfluenceProxy {

    final public String host;
    final public int port;

    final public String userName;
    final public String password;
    final public String nonProxyHosts;

    private ConfluenceProxy( String host, int port, String userName, String password, String nonProxyHosts) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.nonProxyHosts = nonProxyHosts;
    }

    public static ConfluenceProxy of( String host, int port, String userName, String password, String nonProxyHosts) {
        return new ConfluenceProxy( host, port, userName, password, nonProxyHosts);
    }
}
