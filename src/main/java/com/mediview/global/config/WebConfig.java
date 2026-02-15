package com.mediview.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebConfig {

    @Bean
    public SpringTemplateEngine te() {
        SpringTemplateEngine te = new SpringTemplateEngine();

        ClassLoaderTemplateResolver tr = new ClassLoaderTemplateResolver();
        tr.setPrefix("templates/");
        tr.setSuffix(".html");
        tr.setCharacterEncoding("UTF-8");

        te.setTemplateResolver(tr);
        return te;
    }
}
