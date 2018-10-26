package org.bsc.confluence;

import org.bsc.confluence.DeployStateManager.Parameters;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class DeployStateManagerTest {

    private final java.io.File basedir = new java.io.File( System.getProperty("java.io.tmpdir") );
    private final Path stateFile = Paths.get( basedir.toString(), DeployStateManager.STORAGE_NAME);

    @Test(expected = NullPointerException.class)
    public void testLoadNullParams() {
        @SuppressWarnings("unused")
        DeployStateManager manager = DeployStateManager.load("endpoint", null);
    }

    public static Parameters createParametersFromStateFilePath(Path file) throws IOException {
        final Parameters parameters = new Parameters();
        Files.deleteIfExists(file);
        parameters.setOutdir( file.getParent().toFile() );
        return parameters;
    }

    @Test
    public void testIsUpdatedForNewFile() throws IOException {
        Parameters parameters =  createParametersFromStateFilePath(stateFile);
        assertThat( stateFile.toFile().exists(), equalTo(false));
        assertThat( stateFile.toFile().isFile(), equalTo(false));
        final DeployStateManager dsm = DeployStateManager.load( "http://localhost:8090/confluence", parameters );
        assertThat( stateFile.toFile().exists(), equalTo(true));
        assertThat( stateFile.toFile().isFile(), equalTo(true));
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(true));
    }

    @Test
    public void testIsNotUpdatedForExistingFile() throws IOException {
        final java.io.File tmpDir = new java.io.File( System.getProperty("java.io.tmpdir") );
        final Path stateFile = Paths.get( tmpDir.toString(), DeployStateManager.STORAGE_NAME);
        Parameters parameters =  createParametersFromStateFilePath(stateFile);
        final DeployStateManager dsm = DeployStateManager.load( "http://localhost:8090/confluence", parameters );
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(true));
        final DeployStateManager dsm2 = DeployStateManager.load( "http://localhost:8090/confluence", parameters );
        assertThat( dsm2.isUpdated(Paths.get("pom.xml")), equalTo(false));
    }

    @Test
    public void testIsUpdatedForChangingContent() throws IOException {
        final java.io.File tmpDir = new java.io.File( System.getProperty("java.io.tmpdir") );
        final Path stateFile = Paths.get( tmpDir.toString(), DeployStateManager.STORAGE_NAME);
        Parameters parameters =  createParametersFromStateFilePath(stateFile);
        File tmpFile = File.createTempFile("test-confluence-plugin-", ".tmp");

        FileWriter writer = new FileWriter(tmpFile);
        writer.write("My initial content");
        writer.close();

        final DeployStateManager dsm = DeployStateManager.load( "http://localhost:8090/confluence", parameters );
        assertThat( dsm.isUpdated(tmpFile.toPath()), equalTo(true));
        assertThat( dsm.isUpdated(tmpFile.toPath()), equalTo(false));

        writer = new FileWriter(tmpFile);
        writer.write("My changed content");
        writer.close();

        assertThat( dsm.isUpdated(tmpFile.toPath()), equalTo(true));

        //clean-up
        tmpFile.delete();
    }

    @Test
    public void resetState() throws IOException {

        final java.io.File tmpDir = new java.io.File( System.getProperty("java.io.tmpdir") );
        final Path stateFile = Paths.get( tmpDir.toString(), DeployStateManager.STORAGE_NAME);
        Parameters parameters =  createParametersFromStateFilePath(stateFile);

        final DeployStateManager dsm = DeployStateManager.load( "http://localhost:8090/confluence", parameters );
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(true));
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(false));
        dsm.resetState(Paths.get("pom.xml"));
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(true));
        assertThat( dsm.isUpdated(Paths.get("pom.xml")), equalTo(false));
    }

}