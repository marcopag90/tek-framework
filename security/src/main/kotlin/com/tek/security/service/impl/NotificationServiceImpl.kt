package com.tek.security.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.tek.core.util.LoggerDelegate
import com.tek.security.form.ContactForm
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.i18n.SecurityMessageSource.Companion.messageContactUs
import com.tek.security.i18n.SecurityMessageSource.Companion.messageNotificationSent
import com.tek.security.model.Notification
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.repository.NotificationRepository
import com.tek.security.service.AuthService
import com.tek.security.service.NotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class NotificationServiceImpl(
    private val repository: NotificationRepository,
    private val authService: AuthService,
    private val objectMapper: ObjectMapper,
    private val securityMessageSource: SecurityMessageSource,
    private val mailSender: JavaMailSender
) : NotificationService {

    private val log by LoggerDelegate()

    @Value("\${spring.mail.username}")
    lateinit var targetEmailHost: String

    @Transactional
    override fun saveContactUsNotification(contactForm: ContactForm): String {
        log.debug("Accessing $repository for entity: ${Notification::class.java.name} with parameters: $contactForm")

        repository.save(Notification().apply {
            message = objectMapper.writeValueAsString(contactForm)
        })

        sendContactUsEmail(contactForm)

        return securityMessageSource.getSecuritySource()
            .getMessage(messageNotificationSent, null, LocaleContextHolder.getLocale())
    }

    override fun listNotificationsByPrivilege(pageable: Pageable): Page<Notification> {
        log.debug("Retrieving current user authentication")

        authService.getCurrentUser()?.let { userDetails ->
            log.debug("User found: ${userDetails.username}")

            userDetails.authorities.singleOrNull() { it.authority == PrivilegeName.NOTIFICATION_READ.name }
                ?: return Page.empty()
            return repository.findAllByRead(pageable, false)
        }
        return Page.empty()
    }

    private fun sendContactUsEmail(contactForm: ContactForm) {
        SimpleMailMessage().apply {
            this.setTo(targetEmailHost)
            this.setSubject(createContactUsNotification(contactForm))
            this.setText(contactForm.message)
        }.let { mailSender.send(it) }
    }

    private fun createContactUsNotification(contactForm: ContactForm): String {
        val source = securityMessageSource.getSecuritySource()
        return source.getMessage(messageContactUs, arrayOf(contactForm.email), LocaleContextHolder.getLocale())
    }
}