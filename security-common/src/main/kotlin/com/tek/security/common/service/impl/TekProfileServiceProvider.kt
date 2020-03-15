package com.tek.security.common.service.impl

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.errorNotFoundResource
import com.tek.core.service.TekCrudEntityService
import com.tek.core.util.orNull
import com.tek.security.common.form.ProfileForm
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekProfileRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import javax.validation.Validator

@Suppress("unused")
@Service
class TekProfileServiceProvider(
    coreMessageSource: CoreMessageSource,
    validator: Validator,
    repository: TekProfileRepository
) : TekCrudEntityService<TekProfile, Long, TekProfileRepository, ProfileForm>(
    TekProfile::class.java,
    repository,
    validator,
    coreMessageSource
) {

    fun readOneByName(name: String): TekProfile {
        log.debug("Accessing $repository for entity: ${TekProfile::class.java.name} with name:$name")
        repository.findByName(name).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(name)
                )
            )
    }
}