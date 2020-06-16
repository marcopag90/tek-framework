package com.tek.security.common.i18n

import com.tek.security.common.SECURITY_MESSAGES
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.SECURITY_VALIDATOR
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class SecurityMessageSource {

    @Bean(name = [SECURITY_MESSAGE_SOURCE])
    fun getSecuritySource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasenames(SECURITY_MESSAGES)
            setDefaultEncoding("UTF-8")
        }

    @Bean(name = [SECURITY_VALIDATOR])
    fun securityValidator(): LocalValidatorFactoryBean =
        LocalValidatorFactoryBean().apply {
            setValidationMessageSource(getSecuritySource())
        }
}