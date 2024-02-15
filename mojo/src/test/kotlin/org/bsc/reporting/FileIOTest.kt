/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.reporting

import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.Charset

/**
 *
 * @author bsorrentino
 */
class FileIOTest {
    @Test
    @Throws(Exception::class)
    fun forceMkdir() {
        FileUtils.deleteDirectory(File("target/test"))

        val outputFile = File("target/test/io", "test.txt")
        val folder = File(outputFile.parent)

        assertFalse(folder.exists())

        FileUtils.forceMkdir(folder)

        assertTrue(folder.exists())
        assertTrue(folder.isDirectory)

        FileUtils.writeStringToFile(outputFile, "this is test", Charset.defaultCharset(), false)
        assertTrue(outputFile.exists())
        assertTrue(outputFile.isFile)
    }
}