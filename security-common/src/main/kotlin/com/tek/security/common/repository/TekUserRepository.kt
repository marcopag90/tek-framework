package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TEK_USER_FULL
import com.tek.security.common.model.TekUser
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
@JaversSpringDataAuditable
interface TekUserRepository : TekRepository<TekUser, Long> {

    @EntityGraph(value = TEK_USER_FULL)
    fun findByUsername(username: String): TekUser?

    fun findPreferencesById(id: Long): TekUser?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun existsByEmailAndUsername(email: String, username: String): Boolean
}