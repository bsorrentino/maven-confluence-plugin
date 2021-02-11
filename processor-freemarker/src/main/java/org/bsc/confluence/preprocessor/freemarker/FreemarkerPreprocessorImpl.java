package org.bsc.confluence.preprocessor.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import org.bsc.confluence.preprocessor.SiteProcessorService;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2019
 * 
 * updated by bsorrentino
 */
//@MetaInfServices(org.bsc.confluence.preprocessor.SiteProcessorService.class)
public class FreemarkerPreprocessorImpl implements SiteProcessorService {

    private static final Version VERSION = Configuration.VERSION_2_3_29;

    private final Configuration cfg;

    public FreemarkerPreprocessorImpl() {
        cfg = new Configuration(VERSION);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    @Override
    public String getName() {
        return "freemarker";
    }

    @Override
    public CompletableFuture<String> process(String input, Map<String, Object> variables) {
        
        final CompletableFuture<String> result = new CompletableFuture<>();
        try {
            Template t = new Template(UUID.randomUUID().toString(), new StringReader(input), cfg);
            StringWriter out = new StringWriter();
            t.process(getModel(variables), out);
            result.complete( out.toString() );
        } catch (IOException | TemplateException e) {          
            result.completeExceptionally( new IllegalArgumentException("Error while processing the template!", e));
        }
        
        return result;
    }

    private Map<String, Object> getModel(Map<String, Object> variables) throws TemplateModelException {
        Map<String, Object> model = new HashMap<>(variables);
        BeansWrapper beans = new BeansWrapper(VERSION);
        model.put("enums", beans.getEnumModels());
        model.put("statics", beans.getStaticModels());
        model.put("Paths", beans.getStaticModels().get(Paths.class.getName()));
        model.put("Files", beans.getStaticModels().get(Files.class.getName()));
        return model;
    }

}
