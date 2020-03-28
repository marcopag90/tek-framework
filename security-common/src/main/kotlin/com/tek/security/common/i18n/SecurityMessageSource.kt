package com.tek.security.common.i18n

import com.tek.core.i18n.TekMessageSource
import com.tek.security.common.SECURITY_MESSAGES
import com.tek.security.common.SECURITY_VALIDATOR
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class SecurityMessageSource : TekMessageSource {

    override fun getResource(): MessageSource = getSecuritySource()

    @Bean
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

    companion object {

        const val errorConflictUsername = "error.conflict.username"
        const val errorConflictEmail = "error.conflict.email"
        const val errorConflictPassword = "error.conflict.password"
        const val errorNotValidPassword = "error.notvalid.password"

        const val messageEmailContactUs = "message.email.contact.us"
        const val messageEmailSent = "message.email.sent"
    }
}