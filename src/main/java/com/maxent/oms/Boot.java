package com.maxent.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;



/**
 * Created by haiquanli on 15/12/7.
 */

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = Boot.class)
@ImportResource("classpath:beans.xml")
@Configuration
public class Boot extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Boot.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }


    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8081);
    }
}