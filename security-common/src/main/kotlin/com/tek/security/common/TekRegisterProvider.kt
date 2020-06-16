package com.tek.security.common

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.form.AbstractRegisterForm
import com.tek.security.common.i18n.SecurityMessageBundle
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekRegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Default provider to execute business logic for user registration
 */
@Component
abstract class TekRegisterProvider(
    private val authService: TekAuthService
) {

    protected val log by LoggerDelegate()

    lateinit var form: AbstractRegisterForm

    @Autowired
    lateinit var authSecurityProperties: TekSecurityProperties
    @Autowired
    lateinit var registerService: TekRegisterService

    @Autowired
    @Qualifier(SECURITY_MESSAGE_SOURCE)
    protected lateinit var securityMessageSource: MessageSource

    @Autowired
    protected lateinit var userRepository: TekUserRepository
    @Autowired
    protected lateinit var profileRepository: TekProfileRepository

    @Transactional
    fun processRegistration(form: AbstractRegisterForm): TekUser {
        this.form = form
        log.info("Processing user registration with data: {}...", form)
        registerService.isValidPassword(form.password)
        registerService.isUsernameAlreadyTaken(form.username)
        form.email?.let { registerService.isEmailAlreadyTaken(it) }
        isPasswordAcceptable()
        register(form)
        val user =
            registerService.getUserWithDefaultProfile(
                form.username,
                authSecurityProperties.registerProfile
            )
        log.info("User {} registration completed!", form.username)
        return user
    }

    fun isPasswordAcceptable() {
        log.info("Checking if given [password] is acceptable: {}...", form.password)
        val (isAcceptable, constraintMessage) =
            authService.checkPasswordConstraints(form.username, form.email, form.password)
        if (!isAcceptable) {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageBundle.errorConflictPassword,
                    parameters = arrayOf(constraintMessage!!)
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }
        log.info("Success!")
    }

    abstract fun register(form: AbstractRegisterForm)
}