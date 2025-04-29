package com.epam.training.gen.ai.utility;

import com.epam.training.gen.ai.prompt.TemplateSpecification;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

public class LoadTemplates {


    public static String getSystemMessage(String input) {
        Handlebars handlebars = new Handlebars();
        InputStream inputStream = loadExternalResource(input);
        Yaml yaml = new Yaml(new Constructor(TemplateSpecification.class, new LoaderOptions()));
        TemplateSpecification templateSpecification = yaml.loadAs(inputStream, TemplateSpecification.class);
        Template template;
        try {
            template = handlebars.compileInline(templateSpecification.getTemplate());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return template.text();
    }

    private static InputStream loadExternalResource(String input) {
        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(input).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

}
