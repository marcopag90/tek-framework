package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.security.model.Privilege
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.service.PrivilegeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("UNUSED")
@Service
class PrivilegeServiceImpl(
    private val privilegeRepository: PrivilegeRepository,
    private val coreMessageSource: CoreMessageSource
) : PrivilegeService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<Privilege> {
        log.debug("Fetching data from repository: $privilegeRepository")
        predicate?.let {
            return privilegeRepository.findAll(predicate, pageable)
        } ?: return privilegeRepository.findAll(pageable)
    }

    override fun readOne(id: Long): Privilege {
        log.debug("Accessing $privilegeRepository for entity: ${Privilege::class.java.name} with id:$id")

        val optional = privilegeRepository.findById(id)
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