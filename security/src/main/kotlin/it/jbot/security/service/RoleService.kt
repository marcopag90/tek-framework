package it.jbot.security.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.JBotResponseEntity
import it.jbot.security.model.Role
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface RoleService {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Role>>

    fun read(name: String): ResponseEntity<JBotResponseEntity<Role>>
}