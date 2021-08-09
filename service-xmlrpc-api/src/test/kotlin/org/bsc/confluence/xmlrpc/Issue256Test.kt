package org.bsc.confluence.xmlrpc

import org.bsc.confluence.ConfluenceService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class Issue256Test {

    @Test
    fun timeoutConversion() {

        assertEquals( 10000, ConfluenceService.getConnectTimeout(TimeUnit.MILLISECONDS) )
        assertEquals( 10, ConfluenceService.getConnectTimeout(TimeUnit.SECONDS) )

        assertEquals( 10000, ConfluenceService.getReadTimeout(TimeUnit.MILLISECONDS) )
        assertEquals( 10, ConfluenceService.getReadTimeout(TimeUnit.SECONDS) )

        assertEquals( 10000, ConfluenceService.getWriteTimeout(TimeUnit.MILLISECONDS) )
        assertEquals( 10, ConfluenceService.getWriteTimeout(TimeUnit.SECONDS) )
    }

}