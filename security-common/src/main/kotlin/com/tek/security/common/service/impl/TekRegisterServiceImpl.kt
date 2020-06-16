package com.tek.security.common.service.impl

import com.tek.core.CORE_MESSAGE_SOURCE
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.core.util.orNull
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.TekSecurityProperties
import com.tek.security.common.form.AbstractRegisterForm
import com.tek.security.common.i18n.SecurityMessageBundle
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekProfileService
import com.tek.security.common.service.TekRegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class TekRegisterServiceImpl(
    private val authService: TekAuthService,
    private val repository: TekUserRepository,
    private val profileRepository: TekProfileRepository
) : TekRegisterService {

    protected val log by LoggerDelegate()

    @Autowired
    @Qualifier(CORE_MESSAGE_SOURCE)
    protected lateinit var coreMessageSource: MessageSource

    @Autowired
    @Qualifier(SECURITY_MESSAGE_SOURCE)
    protected lateinit var securityMessageSource: MessageSource

    override fun isValidPassword(password: String) {
        log.info("Checking if given [password] is valid: {}...", password)
        val message = securityMessageSource.getMessage(
            SecurityMessageBundle.errorNotValidPassword,
            null,
            LocaleContextHolder.getLocale()
        )
        authService.isValidPassword(password).isFalse {
            throw TekValidationException(mutableMapOf(AbstractRegisterForm::password.name to message))
        }
        log.info("Success!")
    }

    override fun isUsernameAlreadyTaken(username: String) {
        log.info("Checking if given [username] is taken: {}...", username)
        repository.existsByUsername(username).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageBundle.errorConflictUsername,
                    parameters = arrayOf(username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }
        log.info("Success!")
    }

    override fun isEmailAlreadyTaken(email: String) {
        log.info("Checking if given [email] is taken: {}...", email)
        repository.existsByEmail(email).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageBundle.errorConflictEmail,
                    parameters = arrayOf(email)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }
        log.info("Success!")
    }

    override fun getUserWithDefaultProfile(username: String, profile: String): TekUser {
        log.info(
            "Adding default user profile: [{}] ...",
            profile
        )
        val registerProfile =
            profileRepository.findByName(profile).orNull()
        if (registerProfile != null) {
            val user = repository.findByUsername(username).orNull()
            if (user != null) {
                user.profiles.add(registerProfile)
                return user
            } else {
                throw Error("User with username: $username not found!")
            }
        } else {
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(profile)
                )
            )
        }
    }
}