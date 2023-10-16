package org.bsc.confluence.preprocessor.freemarker;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.bsc.preprocessor.freemarker.FreemarkerPreprocessorImpl;
import org.junit.jupiter.api.Test;

public class FreemarkerPreprocessorImplTest {

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
        final var p = new FreemarkerPreprocessorImpl();
        final var readInput = read(resourceName + ".input");
        final Map<String,Object> varInput = singletonMap("key", singletonMap("innerKey", "value1"));
        final var readOutput = read(resourceName + ".output");
        assertEquals(readOutput, p.process(readInput, varInput).join() );
    }

    private String read(String inputName) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("preprocessor/" + inputName).toURI())), StandardCharsets.UTF_8);
    }

}