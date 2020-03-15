package com.tek.security.common.repository

import com.tek.security.common.model.TekNotification
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface TekNotificationRepository : JpaRepository<TekNotification, Long>,
    QuerydslPredicateExecutor<TekNotification> {

    fun findAllByIsRead(pageable: Pageable, isRead: Boolean): Page<TekNotification>
}