package com.tek.security.common.service

import com.tek.security.common.model.TekNotification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TekNotificationService {

    fun saveNotification(content: String): TekNotification

    fun listNotificationsByPrivilege(pageable: Pageable, isRead: Boolean?): Page<TekNotification>

    fun setNotificationRead(id: Long): Boolean

    fun delete(id: Long): Long
}