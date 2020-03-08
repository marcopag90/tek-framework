package com.tek.security.oauth2.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.model.TekPrivilege
import com.tek.security.common.repository.TekPrivilegeRepository
import com.tek.security.common.service.TekPrivilegeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class TekPrivilegeServiceImpl(
    private val tekPrivilegeRepository: TekPrivilegeRepository,
    private val coreMessageSource: CoreMessageSource
) : TekPrivilegeService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekPrivilege> {
        log.debug("Fetching data from repository: $tekPrivilegeRepository")
        predicate?.let {
            return tekPrivilegeRepository.findAll(predicate, pageable)
        } ?: return tekPrivilegeRepository.findAll(pageable)
    }

    override fun readOne(id: Long): TekPrivilege {
        log.debug("Accessing $tekPrivilegeRepository for entity: ${TekPrivilege::class.java.name} with id:$id")

        val optional = tekPrivilegeRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        return optional.get()
    }
}