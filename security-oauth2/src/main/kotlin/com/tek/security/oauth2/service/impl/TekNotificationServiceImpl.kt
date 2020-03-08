package com.tek.security.oauth2.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.i18n.SecurityMessageSource
import com.tek.security.common.model.TekNotification
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.repository.TekNotificationRepository
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekNotificationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Suppress("UNUSED")
@Service
class TekNotificationServiceImpl(
    private val tekNotificationRepository: TekNotificationRepository,
    private val tekAuthService: TekAuthService,
    private val coreMessageSource: CoreMessageSource,
    private val securityMessageSource: SecurityMessageSource,
    private val objectMapper: ObjectMapper,
    private val mailSender: JavaMailSender
) : TekNotificationService {

    private val log by LoggerDelegate()

    @Transactional
    override fun saveNotification(content: String): TekNotification {
        log.debug("Accessing $tekNotificationRepository for entity: ${TekNotification::class.java.name} with content: $content")
        return tekNotificationRepository.save(TekNotification(content))
    }

    override fun listNotificationsByPrivilege(
        pageable: Pageable,
        isRead: Boolean?
    ): Page<TekNotification> {
        log.debug("Retrieving current user authentication")

        tekAuthService.getCurrentUser()?.let { userDetails ->
            log.debug("User found: ${userDetails.username}")

            userDetails.authorities.singleOrNull() { it.authority == PrivilegeName.NOTIFICATION_READ.name }
                ?: return Page.empty()

            isRead?.let { return tekNotificationRepository.findAllByIsRead(pageable, isRead) }
                ?: return tekNotificationRepository.findAll(pageable)
        }
        return Page.empty()
    }

    @Transactional
    override fun setNotificationRead(id: Long): Boolean {

        log.debug("Accessing $tekNotificationRepository for entity: ${TekNotification::class.java.name} with id: $id")

        val optional = tekNotificationRepository.findById(id)
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
    override fun delete(id: Long): Long {

        log.debug("Accessing $tekNotificationRepository for entity: ${TekNotification::class.java.name} with id: $id")

        val optional = tekNotificationRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        tekNotificationRepository.deleteById(id)
        return id
    }
}