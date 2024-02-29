/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bsc.preprocessor.SiteProcessorService;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * @author bsorrentino
 */
public interface SiteFactory {

    final class LogHolder {

        private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogHolder.class.getName());
    }

    interface Folder {

        Site createSiteFromFolder();

    }

    interface Model {

        Site createSiteFromModel(Map<String, Object> variables);

        /**
         * @param siteDescriptor
         * @param variables
         * @return
         * @throws Exception
         */
        default Site createFrom(java.io.File siteDescriptor, Map<String, Object> variables) throws Exception {

            if (variables == null)
                throw new java.lang.IllegalArgumentException("variables is null!");

            final String ext =
                    Optional.ofNullable(FilenameUtils.getExtension(siteDescriptor.getName()))
                            .map(v -> v.toLowerCase())
                            .orElse("");

            // _createSite lambda function
            final Function<String, CompletableFuture<Site>> _createSite = (String preprocessedDescriptor) -> {

                final CompletableFuture<Site> result = new CompletableFuture<>();

                switch (ext) {
                    case "xml": {
                        try {
                            final ObjectMapper mapper = new ObjectMapper(new XmlFactory());
                            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                            result.complete(mapper.readValue(preprocessedDescriptor, Site.class));
                        } catch (Exception e) {
                            result.completeExceptionally(e);
                        }

                        break;
                    }
                    case "yml":
                    case "yaml": {
                        try {

                            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                            result.complete(mapper.readValue(preprocessedDescriptor, Site.class));
                        } catch (Exception e) {
                            result.completeExceptionally(e);
                        }

                        break;
                    }
                    case "json":
                        try {
                            final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                            result.complete(mapper.readValue(preprocessedDescriptor, Site.class));
                        } catch (Exception e) {
                            result.completeExceptionally(e);
                        }
                    default:
                        result.completeExceptionally(new IllegalArgumentException(
                                format("file extension [%s] not supported! Currently only '.xml', '.yaml' and '.json'" +
                                        "are valid", ext)));
                }

                return result;
            }; // end lambda function

            byte[] siteDescriptorBytes;
            try (java.io.InputStream is = new FileInputStream(siteDescriptor.toPath().toFile())) {
                siteDescriptorBytes = IOUtils.toByteArray(is);
            }
            // JAVA 11
            // siteDescriptorBytes = Files.readAllBytes(siteDescriptor.toPath());

            final String content = new String(siteDescriptorBytes, StandardCharsets.UTF_8);

            final Optional<SiteProcessorService> siteProcessor = SiteProcessorService.getDefaultPreprocessorService();

            final CompletableFuture<Site> future =
                    siteProcessor.map(p -> p.process(content, variables)
                                            .thenCompose(_createSite)
                                    // uncomment if you want process source content ignoring preprocess exception
                                    //.exceptionally( e -> _createSite.apply(content).join() )
                            )
                            .orElseGet(() -> {
                                LogHolder.log.fine(format("a Preprocessor service is not configurated"));
                                return _createSite.apply(content);
                            });

            return future.join();


        }
    }
}
