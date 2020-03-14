package com.tek.security.common.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.orNull
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.service.TekRoleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class TekRoleServiceImpl(
    private val roleRepository: TekRoleRepository,
    private val coreMessageSource: CoreMessageSource
) : TekRoleService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekRole> {
        log.debug("Fetching data from repository: $roleRepository")
        predicate?.let {
            return roleRepository.findAll(predicate, pageable)
        } ?: return roleRepository.findAll(pageable)
    }

    override fun readOne(id: Long): TekRole {
        log.debug("Accessing $roleRepository for entity: ${TekRole::class.java.name} with id:$id")
        roleRepository.findById(id).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
    }
}