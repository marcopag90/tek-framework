package it.jbot.security.service.impl

import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotEntityResponse
import it.jbot.core.service.JBotCrudService
import it.jbot.core.util.notSupported
import it.jbot.core.validation.EntityValidator
import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.repository.PrivilegeRepository
import it.jbot.security.service.PrivilegeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PrivilegeCrudService(
    private val privilegeRepository: PrivilegeRepository,
    validator: EntityValidator
) : PrivilegeService, JBotCrudService<Privilege, Long, PrivilegeRepository>(privilegeRepository, validator) {

    override fun read(name: String): ResponseEntity<JBotBaseResponse> {
        return ResponseEntity(
            JBotBaseResponse(HttpStatus.OK, privilegeRepository.findByName(PrivilegeName.valueOf(name))),
            HttpStatus.OK
        )
    }

    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<JBotEntityResponse<Privilege>> =
        notSupported()
}