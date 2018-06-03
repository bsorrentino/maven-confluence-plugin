package org.bsc.confluence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class ConfluenceUtilsTest {

    @Test
    public void decode() throws Exception {
        String input = "<coDe>[I][RE]'.*moon[^dab]+'</CODE> : <EM>all</Em> <u>implemented</U> <s>specifications</S> <DEL>having</del> <Strong>the</Strong> <i>RE<I> '.*moon[^dab]+'";
        String decoded = ConfluenceUtils.decode(input);
        assertThat(decoded, is("{{\\[I\\]\\[RE\\]'.\\*moon\\[^dab\\]\\+'}} : _all_ +implemented+ -specifications- -having- *the* _RE_ '.\\*moon\\[^dab\\]\\+'"));
    }

    @Test 
    public void pathTest() throws Exception {
    
        File f = new File(".");
        
        Path pp = Paths.get(  "http://test.xml");
        
        Assert.assertThat( Files.exists(pp), IsEqual.equalTo(false) );

        Path p1 = Paths.get(  "classpath:test.xml");
        
        Assert.assertThat( Files.exists(p1), IsEqual.equalTo(false) );
       
    }
    
    
    @Test
    public void stateManager() throws Exception {
        final DeployStateManager dsm = new DeployStateManager();

        final Path basedir = Paths.get( System.getProperty("java.io.tmpdir") );
        
        final Path file = Paths.get( basedir.toString(), dsm.getFileName());
        Files.deleteIfExists(file);
        
        
        dsm.setBasedir( basedir );
        dsm.setEndpoint( "http://localhost:8090/confluence" );
        
        dsm.load();
        
        Assert.assertThat( file.toFile().exists(), IsEqual.equalTo(true));
        Assert.assertThat( file.toFile().isFile(), IsEqual.equalTo(true));
        
        Assert.assertThat( dsm.isUpdated(Paths.get("pom.xml")), IsEqual.equalTo(true));;
        
        
    }
    
}