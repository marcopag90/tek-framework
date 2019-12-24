package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageResponse
import it.jbot.core.JBotResponseEntity
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.LoggerDelegate
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
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
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource
) : RoleService {

    private val log by LoggerDelegate()

    override fun read(name: String): ResponseEntity<JBotResponseEntity<Role>> {
        log.debug("Fetching data from repository: $roleRepository")

        roleRepository.findByName(RoleName.valueOf(name))?.let {
            return ResponseEntity(
                JBotResponseEntity(HttpStatus.OK, it),
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

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Role>> {
        log.debug("Fetching data from repository: $roleRepository")

        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, roleRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, roleRepository.findAll(pageable)), HttpStatus.OK)
    }
}