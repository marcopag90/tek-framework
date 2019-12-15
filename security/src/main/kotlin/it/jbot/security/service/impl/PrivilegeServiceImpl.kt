package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.util.notSupported
import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.repository.PrivilegeRepository
import it.jbot.security.service.PrivilegeService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PrivilegeServiceImpl(
    private val privilegeRepository: PrivilegeRepository
) : PrivilegeService {

    override fun read(name: String): ResponseEntity<JBotBaseResponse> {
        return ResponseEntity(
            JBotBaseResponse(HttpStatus.OK, privilegeRepository.findByName(PrivilegeName.valueOf(name))),
            HttpStatus.OK
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Privilege>> {
        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, privilegeRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, privilegeRepository.findAll(pageable)), HttpStatus.OK)
    }

    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<JBotEntityResponse<Privilege>> =
        notSupported()
}