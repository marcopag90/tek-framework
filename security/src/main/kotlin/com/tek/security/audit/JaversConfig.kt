package com.tek.security.audit

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
class JaversAuthorConf(
    private val auditorAware: SecurityAuditorAware,
    private val servletRequestHolder: ServletRequestHolder
) {

    companion object {
        const val REMOTE_ADDRESS = "remote-address"
        const val AUTHOR_ID = "author-id"
    }

    @Bean
    fun commitPropertiesProvider(): CommitPropertiesProvider {

        return object : CommitPropertiesProvider {

            //TODO i18n Javers properties
            override fun provideForCommittedObject(domainObject: Any?): MutableMap<String, String> {

                return mutableMapOf<String, String>().apply {
                    this[REMOTE_ADDRESS] = getRemoteAddress()
                    this[AUTHOR_ID] = getAuthorId()
                }
            }

            private fun getRemoteAddress(): String {
                val requestAttributes = servletRequestHolder.getRequestAttributes() as ServletRequestAttributes?
                var remoteAddress = "unavailable"
                if (requestAttributes?.request?.remoteAddr != null)
                    remoteAddress = requestAttributes.request.remoteAddr
                return remoteAddress
            }

            private fun getAuthorId(): String {
                var authorId = "unauthenticated"
                if (auditorAware.currentAuditor.isPresent)
                    authorId = auditorAware.currentAuditor.get().toString()
                return authorId
            }
        }
    }
}