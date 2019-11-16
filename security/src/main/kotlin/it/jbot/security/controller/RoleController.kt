package it.jbot.security.controller

import it.jbot.security.model.enums.RoleName
import it.jbot.security.port.RolePort
import it.jbot.security.repository.RoleRepository
import it.jbot.shared.exception.JBotServiceException
import it.jbot.shared.web.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
class RoleController(
    private val roleRepository: RoleRepository
) : RolePort {
    
    override fun list(pageable: Pageable): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK, roleRepository.findAll(pageable)),
            HttpStatus.OK
        )
    }
    
    override fun getOne(@RequestParam("name") name: String): ResponseEntity<JBotResponse> {
        
        roleRepository.findByName(RoleName.fromString(name))?.let {
            return ResponseEntity(
                JBotResponse(HttpStatus.OK, it),
                HttpStatus.OK
            )
        } ?: throw JBotServiceException(
            "RoleName: $name not found!",
            HttpStatus.NOT_FOUND
        )
        
        
    }
}