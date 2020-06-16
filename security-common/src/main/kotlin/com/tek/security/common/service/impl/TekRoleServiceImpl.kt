package com.tek.security.common.service.impl

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.i18n.SecurityMessageBundle
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.service.TekRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TekRoleServiceImpl : TekRoleService {

    @Autowired
    @Qualifier(SECURITY_MESSAGE_SOURCE)
    lateinit var messageSource: MessageSource

    @Autowired
    lateinit var repository: TekRoleRepository

    override fun checkIfExists(role: TekRole): Boolean {
        if (!repository.existsById(role.id!!)) {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageBundle.errorNotFoundRole,
                    parameters = arrayOf(role.name)
                ),
                httpStatus = HttpStatus.NOT_FOUND
            )
        } else
            return true
    }
}