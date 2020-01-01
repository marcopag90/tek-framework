package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.core.util.LoggerDelegate
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import com.tek.security.model.Role
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.service.RoleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource
) : RoleService {

    private val log by LoggerDelegate()

    override fun read(name: String): Role {
        log.debug("Fetching data from repository: $roleRepository")

        roleRepository.findByName(RoleName.valueOf(name))?.let {
            return it
        } ?: throw TekServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = errorRoleNotFound,
                parameters = arrayOf(name)
            ),
            httpStatus = HttpStatus.NOT_FOUND
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): Page<Role> {
        log.debug("Fetching data from repository: $roleRepository")
        predicate?.let {
            return roleRepository.findAll(predicate, pageable)
        } ?: return roleRepository.findAll(pageable)
    }
}