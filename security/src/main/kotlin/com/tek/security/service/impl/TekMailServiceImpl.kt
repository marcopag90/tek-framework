package com.tek.security.service.impl

import com.tek.core.util.LoggerDelegate
import com.tek.security.service.TekMailService
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*

@Suppress("UNUSED")
@Service
class TekMailServiceImpl(
    private val mailSender: JavaMailSender
) : TekMailService {

    private val log by LoggerDelegate()

    override fun sendSimpleMessage(to: Array<String>, subject: String, text: String) {

        log.debug("Sending email to: $${to.contentToString()}...")

        SimpleMailMessage().apply {
            this.setTo(*to)
            this.setSubject(subject)
            this.setText(text)
        }.let { mailSender.send(it) }

        log.debug("Email sent!")
    }
}