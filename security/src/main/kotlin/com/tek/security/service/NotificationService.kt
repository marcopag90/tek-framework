package com.tek.security.service

import com.tek.security.model.business.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NotificationService {

    fun saveNotification(content: String) : Notification

    fun listNotificationsByPrivilege(pageable: Pageable): Page<Notification>

    fun setNotificationRead(id: Long): Boolean
}