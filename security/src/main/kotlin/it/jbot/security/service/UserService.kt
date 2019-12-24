package it.jbot.security.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import it.jbot.security.form.auth.RegisterForm
import it.jbot.security.model.JBotUser
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface UserService {

    fun register(registerForm: RegisterForm): JBotUser

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<JBotUser>>

    fun readOne(id: Long): ResponseEntity<JBotResponseEntity<JBotUser>>

    fun update()

    fun delete(id: Long): ResponseEntity<JBotResponseEntity<Long>>

}