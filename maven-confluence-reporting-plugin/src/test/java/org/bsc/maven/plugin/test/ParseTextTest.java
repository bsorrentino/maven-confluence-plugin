/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.plugin.test;

import org.bsc.maven.plugin.confluence.ConfluenceUtils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author bsorrentino
 */
public class ParseTextTest {
    
    @Test
    public void parsePluginDescription() {
        
        final String text = 
        "if using a https url, configure if the plugin accepts every certifactes or respects hostnameVerifierClass and trustManagerClass (if set). Below the Template <pre> <sslCertificate> <ignore>true"+
        "false</ignore> // default true <hostnameVerifierClass>FQN</hostnameVerifierClass> //default null <trustManagerClass>FQN</trustManagerClass> // default null </sslCertificate> </Pre>";
    
        
        String result = ConfluenceUtils.decode(text);
        
        Assert.assertThat(result, IsNull.notNullValue());
        
        final String expected = 
        "if using a https url, configure if the plugin accepts every certifactes or respects hostnameVerifierClass and trustManagerClass (if set). Below the Template {noformat} <sslCertificate> <ignore>true"+
        "false</ignore> // default true <hostnameVerifierClass>FQN</hostnameVerifierClass> //default null <trustManagerClass>FQN</trustManagerClass> // default null </sslCertificate> {noformat}";

        Assert.assertThat(result, IsEqual.equalTo(expected));
       
    
    }
}
