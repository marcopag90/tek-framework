package it.jbot.security.repository

import it.jbot.core.repository.JBotRepository
import it.jbot.security.model.JBotUser
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
@JaversSpringDataAuditable
interface UserRepository : JBotRepository<JBotUser, Long> {

    fun findByUsername(username: String): JBotUser?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}