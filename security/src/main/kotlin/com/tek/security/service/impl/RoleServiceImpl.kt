package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.util.LoggerDelegate
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import com.tek.security.model.Role
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.service.RoleService
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

    override fun read(name: String): ResponseEntity<TekResponseEntity<Role>> {
        log.debug("Fetching data from repository: $roleRepository")

        roleRepository.findByName(RoleName.valueOf(name))?.let {
            return ResponseEntity(
                TekResponseEntity(HttpStatus.OK, it),
                HttpStatus.OK
            )
        } ?: throw TekServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = errorRoleNotFound,
                parameters = arrayOf(name)
            ),
            httpStatus = HttpStatus.NOT_FOUND
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Role>> {
        log.debug("Fetching data from repository: $roleRepository")

        predicate?.let {
            return ResponseEntity(
                TekPageResponse(
                    HttpStatus.OK,
                    roleRepository.findAll(predicate, pageable)
                ),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(
            TekPageResponse(
                HttpStatus.OK,
                roleRepository.findAll(pageable)
            ), HttpStatus.OK)
    }
}