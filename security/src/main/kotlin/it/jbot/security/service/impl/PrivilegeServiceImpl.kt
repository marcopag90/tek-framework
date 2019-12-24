package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageResponse
import it.jbot.core.JBotResponseEntity
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.LoggerDelegate
import it.jbot.security.i18n.SecurityMessageSource
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
    private val privilegeRepository: PrivilegeRepository,
    private val messageSource: SecurityMessageSource
) : PrivilegeService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Privilege>> {
        log.debug("Fetching data from repository: $privilegeRepository")

        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, privilegeRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(
            JBotPageResponse(HttpStatus.OK, privilegeRepository.findAll(pageable)),
            HttpStatus.OK
        )
    }

    override fun read(name: String): ResponseEntity<JBotResponseEntity<Privilege>> {
        log.debug("Fetching data from repository: $privilegeRepository")

        privilegeRepository.findByName(PrivilegeName.valueOf(name))?.let {
            return ResponseEntity(
                JBotResponseEntity(HttpStatus.OK, it),
                HttpStatus.OK
            )
        } ?: throw JBotServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = SecurityMessageSource.errorPrivilegeNotFound,
                parameters = arrayOf(name)
            ),
            httpStatus = HttpStatus.NOT_FOUND
        )
    }
}