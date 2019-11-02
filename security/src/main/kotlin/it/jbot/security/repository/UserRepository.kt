package it.jbot.security.repository

import it.jbot.security.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface UserRepository : JpaRepository<User, Long> {
    
    fun findByUserName(userName: String): User?
}