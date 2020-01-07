package com.tek.security.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.model.Notification
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.repository.NotificationRepository
import com.tek.security.service.AuthService
import com.tek.security.service.NotificationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Suppress("UNUSED")
@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val authService: AuthService,
    private val coreMessageSource: CoreMessageSource,
    private val securityMessageSource: SecurityMessageSource,
    private val objectMapper: ObjectMapper,
    private val mailSender: JavaMailSender
) : NotificationService {

    private val log by LoggerDelegate()

    @Transactional
    override fun saveNotification(content: String): Notification {
        log.debug("Accessing $notificationRepository for entity: ${Notification::class.java.name} with content: $content")
        return notificationRepository.save(Notification(content))
    }

    override fun listNotificationsByPrivilege(pageable: Pageable): Page<Notification> {
        log.debug("Retrieving current user authentication")

        authService.getCurrentUser()?.let { userDetails ->
            log.debug("User found: ${userDetails.username}")

            userDetails.authorities.singleOrNull() { it.authority == PrivilegeName.NOTIFICATION_READ.name }
                ?: return Page.empty()

            return notificationRepository.findAllByIsRead(pageable, false)
        }
        return Page.empty()
    }

    @Transactional
    override fun setNotificationRead(id: Long): Boolean {

        log.debug("Accessing $notificationRepository for entity: ${Notification::class.java.name} with id: $id")

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
}