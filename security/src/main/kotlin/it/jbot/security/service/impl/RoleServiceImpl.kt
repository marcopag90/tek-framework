package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.util.notSupported
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.Role
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.service.RoleService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {

    override fun read(name: String): ResponseEntity<JBotBaseResponse> {
        return ResponseEntity(
            JBotBaseResponse(HttpStatus.OK, roleRepository.findByName(RoleName.valueOf(name))),
            HttpStatus.OK
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Role>> {
        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, roleRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, roleRepository.findAll(pageable)), HttpStatus.OK)
    }

    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<JBotEntityResponse<Role>> =
        notSupported()
}