package org.bsc.confluence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class DeployStateManagerTest {
    private val basedir = Paths.get(System.getProperty("java.io.tmpdir") );
    private val stateFile = Paths.get(basedir.toString(), DeployStateManager.STORAGE_NAME)

    @BeforeEach
    fun deleteStorage( ) { Files.deleteIfExists(stateFile) }

    @Test
    fun testLoadNullParams() = assertThrows(NullPointerException::class.java) {
        DeployStateManager.load("endpoint", null)
    }

    @Test
    fun testIsUpdatedForNewFile() {

        assertFalse(stateFile.toFile().exists())
        assertFalse(stateFile.toFile().isFile)
        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertTrue(stateFile.toFile().exists())
        assertTrue(stateFile.toFile().isFile)
        assertTrue(dsm.isUpdated(Paths.get("pom.xml")))
    }

    @Test
    fun testIsNotUpdatedForExistingFile() {
        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertTrue(dsm.isUpdated(Paths.get("pom.xml")))
        val dsm2 = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertFalse(dsm2.isUpdated(Paths.get("pom.xml")))
    }

    @Test
    fun testIsUpdatedForChangingContent() {
        val tmpFile = File.createTempFile("test-confluence-plugin-", ".tmp")

        FileWriter(tmpFile).use {  it.write("My initial content") }

        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)

        assertTrue(dsm.isUpdated(tmpFile.toPath()))
        assertFalse(dsm.isUpdated(tmpFile.toPath()))

        FileWriter(tmpFile).use { it.write("My changed content") }

        assertTrue(dsm.isUpdated(tmpFile.toPath()))

        //clean-up
        tmpFile.delete()
    }

    @Test
    fun resetState() {
        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertTrue(dsm.isUpdated(Paths.get("pom.xml")))
        assertFalse(dsm.isUpdated(Paths.get("pom.xml")))
        dsm.resetState(Paths.get("pom.xml"))
        assertTrue(dsm.isUpdated(Paths.get("pom.xml")))
        assertFalse(dsm.isUpdated(Paths.get("pom.xml")))
    }
}