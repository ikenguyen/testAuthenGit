package com.coolmind.ordertracker.web.config;


import com.coolmind.ordertracker.core.init.DevelopmentConfiguration;
import com.coolmind.ordertracker.core.init.TestConfiguration;
import com.coolmind.ordertracker.web.config.root.AppSecurityConfig;
import com.coolmind.ordertracker.web.config.root.RootContextConfig;
import com.coolmind.ordertracker.web.config.servlet.ServletContextConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * Replacement for most of the content of web.xml, sets up the root and the servlet context config.
 *
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootContextConfig.class, DevelopmentConfiguration.class, TestConfiguration.class,
                AppSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {ServletContextConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }




}


