package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : TekRepository<Notification, Long> {

    fun findAllByRead(pageable: Pageable, read: Boolean): Page<Notification>
}