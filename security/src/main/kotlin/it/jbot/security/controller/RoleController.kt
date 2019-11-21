package it.jbot.security.controller

import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import it.jbot.security.model.enums.RoleName
import it.jbot.security.port.RolePort
import it.jbot.security.repository.RoleRepository
import it.jbot.web.exception.JBotServiceException
import it.jbot.web.exception.ServiceExceptionData
import it.jbot.web.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
class RoleController(
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource = SecurityMessageSource()
) : RolePort {
    
    override fun list(pageable: Pageable): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK, roleRepository.findAll(pageable)),
            HttpStatus.OK
        )
    }
    
    override fun get(@RequestParam("name") name: String): ResponseEntity<JBotResponse> {
        
        roleRepository.findByName(RoleName.fromString(name))?.let {
            return ResponseEntity(
                JBotResponse(HttpStatus.OK, it),
                HttpStatus.OK
            )
        } ?: throw JBotServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = errorRoleNotFound,
                parameters = arrayOf(name)
            ),
            httpStatus = HttpStatus.NOT_FOUND
        )
    }
}