package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.util.LoggerDelegate
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.model.Privilege
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.service.PrivilegeService
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

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Privilege>> {
        log.debug("Fetching data from repository: $privilegeRepository")

        predicate?.let {
            return ResponseEntity(
                TekPageResponse(
                    HttpStatus.OK,
                    privilegeRepository.findAll(predicate, pageable)
                ),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(
            TekPageResponse(HttpStatus.OK, privilegeRepository.findAll(pageable)),
            HttpStatus.OK
        )
    }

    override fun read(name: String): ResponseEntity<TekResponseEntity<Privilege>> {
        log.debug("Fetching data from repository: $privilegeRepository")

        privilegeRepository.findByName(PrivilegeName.valueOf(name))?.let {
            return ResponseEntity(
                TekResponseEntity(HttpStatus.OK, it),
                HttpStatus.OK
            )
        } ?: throw TekServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = SecurityMessageSource.errorPrivilegeNotFound,
                parameters = arrayOf(name)
            ),
            httpStatus = HttpStatus.NOT_FOUND
        )
    }
}