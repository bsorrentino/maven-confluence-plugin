package org.bsc.confluence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Optional.empty

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
        assertTrue(dsm.isUpdated(Paths.get("pom.xml"), empty()))
    }

    @Test
    fun testIsNotUpdatedForExistingFile() {
        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertTrue(dsm.isUpdated(Paths.get("pom.xml"), empty()))
        val dsm2 = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertFalse(dsm2.isUpdated(Paths.get("pom.xml"), empty()))
    }

    @Test
    fun testIsUpdatedForChangingContent() {
        val tmpFile = File.createTempFile("test-confluence-plugin-", ".tmp")

        FileWriter(tmpFile).use {  it.write("My initial content") }

        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)

        assertTrue(dsm.isUpdated(tmpFile.toPath(),empty()))
        assertFalse(dsm.isUpdated(tmpFile.toPath(),empty()))

        FileWriter(tmpFile).use { it.write("My changed content") }

        assertTrue(dsm.isUpdated(tmpFile.toPath(),empty()))

        //clean-up
        tmpFile.delete()
    }

    @Test
    fun resetState() {
        val dsm = DeployStateManager.load("http://localhost:8090/confluence", basedir)
        assertTrue(dsm.isUpdated(Paths.get("pom.xml"),empty()))
        assertFalse(dsm.isUpdated(Paths.get("pom.xml"),empty()))
        dsm.removeState(Paths.get("pom.xml"))
        assertTrue(dsm.isUpdated(Paths.get("pom.xml"),empty()))
        assertFalse(dsm.isUpdated(Paths.get("pom.xml"),empty()))
    }
}