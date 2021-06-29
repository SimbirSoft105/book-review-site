package com.simbirsoft.practice.bookreviewsite.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfirmMailGenerator {

    @Autowired
    private Configuration configuration;

    @Value("${server.url}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    public String generateConfirmMail(String confirmCode, String name) {

        Template template;
        try {
            template = configuration.getTemplate("confirm_mail.ftlh");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Map<String, String> model = new HashMap<>();
        model.put("name", name);
        model.put("link", serverAddress + ":" + serverPort + "/sign_up/confirm_email/" +
                confirmCode);

        StringWriter stringWriter = new StringWriter();

        try {
            template.process(model, stringWriter);
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException(e);
        }

        return stringWriter.toString();

    }

}
