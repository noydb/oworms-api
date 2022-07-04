package com.oworms.mail.config;

import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.dto.NewBnaDTO;
import com.oworms.mail.dto.NewWordEmailDTO;
import com.oworms.mail.dto.UpdatedWordEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(NewBnaDTO message, String templateName) {
        Context context = new Context();
        context.setVariable("message", message);

        return templateEngine.process(templateName, context);
    }

    public String build(NewWordEmailDTO message, String templateName) {
        Context context = new Context();
        context.setVariable("message", message);

        return templateEngine.process(templateName, context);
    }

    public String build(UpdatedWordEmailDTO message, String templateName) {
        Context context = new Context();
        context.setVariable("message", message);

        return templateEngine.process(templateName, context);
    }

    public String build(BucketOverflowDTO message, String templateName) {
        Context context = new Context();
        context.setVariable("message", message);

        return templateEngine.process(templateName, context);
    }
}
