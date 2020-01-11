package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.business.Notification
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface NotificationRepository : TekRepository<Notification, Long> {

    fun findAllByIsRead(pageable: Pageable, isRead: Boolean): Page<Notification>
}