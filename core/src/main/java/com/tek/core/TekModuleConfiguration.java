package com.tek.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;

/**
 * Template class to create a Tek module configuration.
 *
 * @author MarcoPagan
 */
@Slf4j
public abstract class TekModuleConfiguration {

    @Autowired
    protected ConfigurableEnvironment environment;

    private final Class<?> configuration;

    protected TekModuleConfiguration(Class<?> configuration) {
        this.configuration = configuration;
    }

    public abstract void checkModuleConfiguration() throws ConfigurationException;

    @PostConstruct
    @SneakyThrows
    private void postConstruct() {
        log.info(
            "Checking Tek Module Configuration: [{}]",
            ClassUtils.getUserClass(configuration).getSimpleName()
        );
        checkModuleConfiguration();
        log.info(
            "[{}] success!",
            ClassUtils.getUserClass(configuration).getSimpleName()
        );
    }
}
