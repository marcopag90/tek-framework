package com.tek.security.common.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.errorNotFoundResource
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.orNull
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.service.TekProfileService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class TekProfileServiceImpl(
    private val profileRepository: TekProfileRepository,
    private val coreMessageSource: CoreMessageSource
) : TekProfileService {

    private val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekProfile> {
        log.debug("Fetching data from repository: $profileRepository")
        predicate?.let {
            return profileRepository.findAll(predicate, pageable)
        } ?: return profileRepository.findAll(pageable)
    }

    override fun readOne(id: Long): TekProfile {
        log.debug("Accessing $profileRepository for entity: ${TekProfile::class.java.name} with id:$id")
        profileRepository.findById(id).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
    }

    override fun readOneByName(name: String): TekProfile {
        log.debug("Accessing $profileRepository for entity: ${TekProfile::class.java.name} with name:$name")
        profileRepository.findByName(name).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(name)
                )
            )
    }
}