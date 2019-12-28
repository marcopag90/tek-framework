package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.TekUser
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
@JaversSpringDataAuditable
interface UserRepository : TekRepository<TekUser, Long> {

    fun findByUsername(username: String): TekUser?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}