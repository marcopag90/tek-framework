package com.tek.security.common

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.TekProperty
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.core.util.orNull
import com.tek.security.common.form.AbstractRegisterForm
import com.tek.security.common.i18n.SecurityMessageSource
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekAuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Default provider to execute business logic for user registration
 */
@Component
abstract class TekRegistrationProvider(
    private val authService: TekAuthService
) {

    protected val log: Logger = LoggerFactory.getLogger(TekRegistrationProvider::class.java)

    @Autowired
    lateinit var coreMessageSource: CoreMessageSource
    @Autowired
    lateinit var securityMessageSource: SecurityMessageSource
    @Autowired
    lateinit var userRepository: TekUserRepository
    @Autowired
    lateinit var profileRepository: TekProfileRepository

    lateinit var form: AbstractRegisterForm

    /**
     * Provides a default registration profile for the user
     */
    @Value("\${tek.security.module.registerProfile}")
    private lateinit var registerProfile: TekProperty

    /**
     * Provides a password expiration for the user
     */
    @Value("\${tek.security.module.passwordExpiration}")
    lateinit var passwordExpiration: TekProperty

    @Transactional
    fun processRegistration(form: AbstractRegisterForm): TekUser {
        this.form = form
        log.info("Processing user registration with data: $form...")
        isValidPassword()
        isUsernameAlreadyTaken()
        form.email?.let { form.email?.let { isEmailAlreadyTaken() } }
        isPasswordAcceptable()
        register(form)
        val user = getUserWithProfile()
        log.info("User [${form.username}] registration completed!")
        return user
    }

    fun isPasswordAcceptable() {
        log.info("Checking if given [password] is acceptable: ${form.password}...")
        val (isAcceptable, constraintMessage) = authService.checkPasswordConstraints(
            form.username, form.email, form.password
        )
        if (!isAcceptable) {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageSource.errorConflictPassword,
                    parameters = arrayOf(constraintMessage!!)
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }
        log.info("Success!")
    }

    abstract fun register(form: AbstractRegisterForm)

    private fun isValidPassword() {
        log.info("Checking if given [password] is valid: ${form.password}...")
        val message = securityMessageSource.getSecuritySource()
            .getMessage(
                SecurityMessageSource.errorNotValidPassword,
                null,
                LocaleContextHolder.getLocale()
            )
        authService.isValidPassword(form.password).isFalse {
            throw TekValidationException(mutableMapOf(AbstractRegisterForm::password.name to message))
        }
        log.info("Success!")
    }

    private fun isUsernameAlreadyTaken() {
        log.info("Checking if given [username] is taken: ${form.username}...")
        userRepository.existsByUsername(form.username).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageSource.errorConflictUsername,
                    parameters = arrayOf(form.username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }
        log.info("Success!")
    }

    private fun isEmailAlreadyTaken() {
        log.info("Checking if given [email] is taken: ${form.email}...")
        userRepository.existsByEmail(form.email!!).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageSource.errorConflictEmail,
                    parameters = arrayOf(form.email!!)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }
        log.info("Success!")
    }

    private fun getUserWithProfile(): TekUser {
        log.info("Adding default [user profile]: $registerProfile...")
        profileRepository.findByName(registerProfile).orNull()?.let { profile ->
            userRepository.findByUsername(form.username)?.let { user ->
                user.profiles.add(profile)
                return userRepository.save(user)
            } ?: throw Error("User with username: ${form.username} not found!")
        } ?: throw TekResourceNotFoundException(
            data = ServiceExceptionData(
                source = coreMessageSource,
                message = CoreMessageSource.errorNotFoundResource,
                parameters = arrayOf(registerProfile)
            )
        )
    }
}