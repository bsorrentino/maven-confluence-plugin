package org.bsc.confluence.preprocessor;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PreprocessorImplTest {

    @Test
    public void noMarkupTest() throws IOException, URISyntaxException {
        testInternal("noMarkup");
    }

    @Test
    public void basicMarkupTest() throws IOException, URISyntaxException {
        testInternal("basicMarkup");
    }

    @Test
    public void fileExistsTest() throws IOException, URISyntaxException {
        testInternal("fileExists");
    }

    @Test
    public void variableTest() throws IOException, URISyntaxException {
        testInternal("variable");
    }

    private void testInternal(String resourceName) throws URISyntaxException, IOException {
        assertThat(Preprocessor.INSTANCE.preprocess(read(resourceName + ".input"), singletonMap("key", singletonMap("innerKey", "value1"))), is(read(resourceName + ".output")));
    }

    private String read(String inputName) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("preprocessor/" + inputName).toURI())), StandardCharsets.UTF_8);
    }

}