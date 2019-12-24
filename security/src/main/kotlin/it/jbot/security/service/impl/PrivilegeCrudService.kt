package it.jbot.security.service.impl

import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotEntityResponse
import it.jbot.core.service.JBotCrudService
import it.jbot.core.util.notSupported
import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.repository.PrivilegeRepository
import it.jbot.security.service.PrivilegeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.validation.Validator

@Service
class PrivilegeCrudService(
    private val privilegeRepository: PrivilegeRepository,
    validator: Validator
) : PrivilegeService, JBotCrudService<Privilege, Long, PrivilegeRepository, Nothing>(Privilege::class.java, privilegeRepository, validator) {

    override fun read(name: String): ResponseEntity<JBotBaseResponse> {
        return ResponseEntity(
            JBotBaseResponse(HttpStatus.OK, privilegeRepository.findByName(PrivilegeName.valueOf(name))),
            HttpStatus.OK
        )
    }

    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<JBotEntityResponse<Privilege>> =
        notSupported()
}