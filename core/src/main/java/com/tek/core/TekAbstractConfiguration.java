package com.tek.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;

/**
 * Abstract class to extend in order to create a Tek Module Configuration setup.
 *
 * @author MarcoPagan
 */
@Slf4j
public abstract class TekAbstractConfiguration {

    @Autowired
    protected ConfigurableEnvironment environment;

    private final Class<?> configuration;

    public TekAbstractConfiguration(Class<?> configuration) {
        this.configuration = configuration;
    }

    public abstract void checkModuleConfiguration() throws ConfigurationException;

    @PostConstruct
    private void postConstruct() throws ConfigurationException {
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
