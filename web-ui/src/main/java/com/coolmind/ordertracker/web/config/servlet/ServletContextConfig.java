package com.coolmind.ordertracker.web.config.servlet;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * Spring MVC config for the servlet context in the application.
 *
 * The beans of this context are only visible inside the servlet context.
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.coolmind.ordertracker.web.app.controllers")
public class ServletContextConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/app/views/**").addResourceLocations("/app/views/");
        registry.addResourceHandler("/app/controllers/**").addResourceLocations("/app/controllers/");
        registry.addResourceHandler("/app/services/**").addResourceLocations("/app/services/");
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
    }
}
