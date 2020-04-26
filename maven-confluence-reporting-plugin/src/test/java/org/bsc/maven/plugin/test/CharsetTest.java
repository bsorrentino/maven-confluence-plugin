package org.bsc.maven.plugin.test;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

import static java.lang.String.format;

public class CharsetTest {

    @Test
    public void encodingConversion() {

        Assert.assertNotNull(  Charset.defaultCharset() );

        val charsets = Charset.availableCharsets().values();

        val value = "ÉìÅÇÕµ≠»¢";
        charsets.stream()
                .filter( c -> c!= Charset.defaultCharset() )
                .forEach( c -> {
                    Assert.assertTrue( c != Charset.defaultCharset() );

                    val encodedString = new String(value.getBytes(Charset.defaultCharset()), c);

                    System.out.printf( "%s in %s converted in %s is %s\n", value, Charset.defaultCharset(), c, encodedString );

                });



    }
}

