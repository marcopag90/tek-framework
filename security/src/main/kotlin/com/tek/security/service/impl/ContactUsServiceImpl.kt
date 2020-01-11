package com.tek.security.service.impl

import com.tek.core.util.LoggerDelegate
import com.tek.security.form.ContactForm
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.service.ContactUsService
import com.tek.security.service.NotificationService
import com.tek.security.service.TekMailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Suppress("UNUSED")
@Service
class ContactUsServiceImpl(
    private val notificationService: NotificationService,
    private val mailService: TekMailService,
    private val securityMessageSource: SecurityMessageSource
) : ContactUsService {

    private val log by LoggerDelegate()

    @Value("\${spring.mail.username}")
    lateinit var targetEmailHost: String

    @Transactional
    override fun sendContactUsNotification(contactForm: ContactForm): String {

        try {
            notificationService.saveNotification(contactForm.email)
            mailService.sendSimpleMessage(
                to = arrayOf(targetEmailHost),
                subject = createContactUsSubject(contactForm),
                text = contactForm.message
            )
        } catch (ex: Exception) {
            throw ex
        }

        return securityMessageSource.getSecuritySource()
            .getMessage(SecurityMessageSource.messageEmailSent, null, LocaleContextHolder.getLocale())
    }

    private fun createContactUsSubject(contactForm: ContactForm): String {

        return securityMessageSource.getSecuritySource().getMessage(
            SecurityMessageSource.messageEmailContactUs,
            arrayOf(contactForm.email),
            LocaleContextHolder.getLocale()
        )
    }

}