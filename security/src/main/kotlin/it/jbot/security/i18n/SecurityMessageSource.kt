package it.jbot.security.i18n

import it.jbot.core.JBotMessageSource
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class SecurityMessageSource : JBotMessageSource {

    override fun getResource(): MessageSource = getSecuritySource()

    @Bean
    fun getSecuritySource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/security_messages")
            setDefaultEncoding("UTF-8")
        }

    @Bean
    fun securityValidator(): LocalValidatorFactoryBean =
        LocalValidatorFactoryBean().apply { setValidationMessageSource(getSecuritySource()) }

    companion object {

        const val validationNotBlankUsername = "validation.NotBlank.username"
        const val validationSizeUsername = "validation.Size.username"
        const val validationNotBlankPassword = "validation.NotBlank.password"
        const val validationEmail = "validation.Email.email"
        const val validationNotBlankEmail = "validation.NotBlank.email"
        const val validationSizeEmail = "validation.Size.email"

        const val errorConflictUsername = "error.conflict.username"
        const val errorConflictEmail = "error.conflict.email"
        const val errorEmptyRole = "error.empty.role"
        const val errorNotValidPassword = "error.notvalid.password"
        const val errorRoleNotFound = "error.notfound.role"
        const val errorUnknownProperty = "error.unknown.property"
    }
}