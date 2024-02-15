package org.bsc.reporting

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class CharsetTest {
    @Test
    fun encodingConversion() {

        assertNotNull(Charset.defaultCharset())

        val charsets: Collection<Charset> = Charset.availableCharsets().values
        val value = "ÉìÅÇÕµ≠»¢"

        charsets.stream()
                .filter { c: Charset -> c !== Charset.defaultCharset() }
                .forEach { c: Charset ->
                    assertTrue(c !== Charset.defaultCharset())

                    val encodedString = String(value.toByteArray(Charset.defaultCharset()), c)

                    print("$value in ${Charset.defaultCharset()} converted in $c is $encodedString\n")
                }
    }
}