package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TekNotification
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface TekNotificationRepository : TekRepository<TekNotification, Long> {

    fun findAllByIsRead(pageable: Pageable, isRead: Boolean): Page<TekNotification>
}