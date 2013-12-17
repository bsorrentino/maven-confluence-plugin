/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.maven.plugin.test;

import org.apache.commons.io.FileUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author bsorrentino
 */
public class FileIOTest {
    
    @Test
    public void forceMkdir() throws Exception  {
        
        FileUtils.deleteDirectory( new java.io.File("target"));
        
        java.io.File outputFile = new java.io.File( "target/io", "test.txt");
        
        java.io.File folder = new java.io.File(outputFile.getParent());

        Assert.assertThat( folder.exists() , Is.is(false));
        
        FileUtils.forceMkdir( folder );

        Assert.assertThat( folder.exists() , Is.is(true));
        Assert.assertThat( folder.isDirectory() , Is.is(true));

        FileUtils.writeStringToFile(outputFile, "this is test");
        
        Assert.assertThat( outputFile.exists() , Is.is(true));
        Assert.assertThat( outputFile.isFile() , Is.is(true));
    }
}
