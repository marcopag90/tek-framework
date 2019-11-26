package it.jbot.security.port.controller

import it.jbot.core.JBotResponse
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import it.jbot.security.model.enums.RoleName
import it.jbot.security.port.RolePort
import it.jbot.security.repository.RoleRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
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

    override fun read(name: String): ResponseEntity<JBotResponse> {

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