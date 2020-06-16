package com.tek.security.common.service.impl

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.i18n.SecurityMessageBundle
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.service.TekProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TekProfileServiceImpl : TekProfileService {

    @Autowired
    @Qualifier(SECURITY_MESSAGE_SOURCE)
    lateinit var messageSource: MessageSource

    @Autowired
    lateinit var repository: TekProfileRepository

    override fun checkIfExists(profile: TekProfile): Boolean {
        if (!repository.existsById(profile.id!!)) {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageBundle.errorNotFoundProfile,
                    parameters = arrayOf(profile.name!!)
                ),
                httpStatus = HttpStatus.NOT_FOUND
            )
        } else
            return true
    }
}