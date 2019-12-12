package it.jbot.security.repository

import it.jbot.security.model.JBotUser
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
@JaversSpringDataAuditable
interface UserRepository : JpaRepository<JBotUser, Long>, QuerydslPredicateExecutor<JBotUser> {

    fun findByUsername(username: String): JBotUser?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}