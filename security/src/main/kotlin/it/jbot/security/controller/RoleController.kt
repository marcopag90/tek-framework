package it.jbot.security.controller

import it.jbot.security.model.Role
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.shared.exception.JBotServiceException
import it.jbot.shared.web.JBotResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/role")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class RoleController(
    private val roleRepository: RoleRepository
) {

    @GetMapping("/list")
    fun getRoles(pageable: Pageable): ResponseEntity<JBotResponse> {

        return ResponseEntity<JBotResponse>(
            JBotResponse(HttpStatus.OK).apply {
                result = roleRepository.findAll(pageable)
            },
            HttpStatus.OK
        )
    }

    //TODO better role factoring! (method by enum)
    @GetMapping
    fun getRole(@RequestParam("name") name: String): ResponseEntity<JBotResponse> {

        var role: Role? = null

        when (name) {
            RoleName.ROLE_ADMIN.name ->
                roleRepository.findByName(RoleName.ROLE_ADMIN)?.let {
                    role = it
                } ?: throw JBotServiceException("Role: $name not found!", HttpStatus.NOT_FOUND)

            RoleName.ROLE_USER.name ->
                roleRepository.findByName(RoleName.ROLE_USER)?.let {
                    role = it
                } ?: throw JBotServiceException("Role: $name not found!", HttpStatus.NOT_FOUND)

            else -> throw JBotServiceException("Role: $name not found!", HttpStatus.NOT_FOUND)
        }

        assert(role != null)

        return ResponseEntity<JBotResponse>(
            JBotResponse(HttpStatus.OK).apply {
                result = role
            },
            HttpStatus.OK
        )
    }



}