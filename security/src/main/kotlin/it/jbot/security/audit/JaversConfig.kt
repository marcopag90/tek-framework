package it.jbot.security.audit

import org.javers.spring.auditable.AuthorProvider
import org.javers.spring.auditable.CommitPropertiesProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class ServletRequestHolder {

    fun getRequestAttributes(): RequestAttributes? {
        if (RequestContextHolder.getRequestAttributes() == null)
            return null
        return RequestContextHolder.currentRequestAttributes()
    }
}

@Configuration
class JaversConf(
    private val securityAuditorAware: SecurityAuditorAware,
    private val servletRequestHolder: ServletRequestHolder
) {

    @Bean
    fun provideJaversAuthor(): AuthorProvider =
        SecurityAuthorProvider(securityAuditorAware)

    @Bean
    fun commitPropertiesProvider(): CommitPropertiesProvider {

        return object : CommitPropertiesProvider {

            override fun provideForCommittedObject(domainObject: Any?): MutableMap<String, String> {

                val additionalInfo = mutableMapOf<String, String>()

                val requestAttributes =
                    servletRequestHolder.getRequestAttributes() as ServletRequestAttributes?
                requestAttributes?.let {
                    additionalInfo["remote-address"] = it.request.remoteAddr
                }

                return additionalInfo
            }
        }
    }
}