package com.tek.security.common.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.tek.core.CORE_MESSAGE_SOURCE
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.i18n.SecurityMessageSource
import com.tek.security.common.model.TekNotification
import com.tek.security.common.repository.TekNotificationRepository
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekNotificationService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Suppress("unused")
@Service
class TekNotificationServiceImpl(
    private val notificationRepository: TekNotificationRepository,
    private val authService: TekAuthService,
    @Qualifier(CORE_MESSAGE_SOURCE) private val coreMessageSource: MessageSource,
    @Qualifier(SECURITY_MESSAGE_SOURCE) private val securityMessageSource: MessageSource,
    private val objectMapper: ObjectMapper,
    private val mailSender: JavaMailSender
) : TekNotificationService {

    private val log by LoggerDelegate()

    @Transactional
    override fun saveNotification(content: String): TekNotification {
        log.debug(
            "Accessing {} for entity: {} with content: {}",
            notificationRepository,
            TekNotification::class.java.name,
            content
        )
        return notificationRepository.save(TekNotification(content))
    }

    override fun listNotificationsByPrivilege(
        pageable: Pageable,
        isRead: Boolean?
    ): Page<TekNotification> {
        log.debug("Retrieving current user authentication")
        authService.getCurrentUser()?.let { userDetails ->
            log.debug("User found: {}", userDetails.username)

            //TODO change role name
            userDetails.authorities.singleOrNull() { it.authority == "NOTIFICATION_READ" }
                ?: return Page.empty()

            isRead?.let { return notificationRepository.findAllByIsRead(pageable, isRead) }
                ?: return notificationRepository.findAll(pageable)
        }
        return Page.empty()
    }

    @Transactional
    override fun setNotificationRead(id: Long): Boolean {
        log.debug(
            "Accessing {} for entity: {} with id: {}",
            notificationRepository,
            TekNotification::class.java.name,
            id
        )
        val optional = notificationRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        return optional.get().apply { this.isRead = true }.isRead
    }

    @Transactional
    override fun delete(id: Long) {
        log.debug(
            "Accessing {} for entity: {} with id: {}",
            notificationRepository,
            notificationRepository,
            id
        )
        val optional = notificationRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        notificationRepository.deleteById(id)
    }
}