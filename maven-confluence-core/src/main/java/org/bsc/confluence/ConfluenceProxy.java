/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence;

import lombok.Value;

/**
 *
 * @author bsorrentino
 */
@Value(staticConstructor="of")
public class ConfluenceProxy {

    public String host;
    public int port;

    public String userName;
    public String password;
    public String nonProxyHosts;

}
