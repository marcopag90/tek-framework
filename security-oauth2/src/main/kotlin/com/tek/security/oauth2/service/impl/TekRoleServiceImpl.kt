package com.tek.security.oauth2.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.errorNotFoundResource
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.service.TekRoleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("UNUSED")
@Service
class TekRoleServiceImpl(
    private val tekRoleRepository: TekRoleRepository,
    private val coreMessageSource: CoreMessageSource
) : TekRoleService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekRole> {
        log.debug("Fetching data from repository: $tekRoleRepository")
        predicate?.let {
            return tekRoleRepository.findAll(predicate, pageable)
        } ?: return tekRoleRepository.findAll(pageable)
    }

    override fun readOne(id: Long): TekRole {
        log.debug("Accessing $tekRoleRepository for entity: ${TekRole::class.java.name} with id:$id")

        val optional = tekRoleRepository.findById(id)
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