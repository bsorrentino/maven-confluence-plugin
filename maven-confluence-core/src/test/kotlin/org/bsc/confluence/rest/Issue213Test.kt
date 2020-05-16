package org.bsc.confluence.rest

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


class Issue213Test {
    @Test
    fun bigIdWithInt() {

        assertThrows( NumberFormatException::class.java) {
            val pageId = "2182288832"
            val result = Integer.valueOf(pageId)
        }
    }

    @Test
    fun bigIdWithLong() {
        val pageId = "2182288832"
        val result = java.lang.Long.valueOf(pageId)

        assertEquals( 2182288832, result )
    }
}