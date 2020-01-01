package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.errorNotFoundResource
import com.tek.core.util.LoggerDelegate
import com.tek.security.model.Role
import com.tek.security.repository.RoleRepository
import com.tek.security.service.RoleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val coreMessageSource: CoreMessageSource
) : RoleService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<Role> {
        log.debug("Fetching data from repository: $roleRepository")
        predicate?.let {
            return roleRepository.findAll(predicate, pageable)
        } ?: return roleRepository.findAll(pageable)
    }

    override fun readOne(id: Long): Role {
        log.debug("Accessing $roleRepository for entity: ${Role::class.java.name} with id:$id")

        val optional = roleRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        return optional.get()
    }


}