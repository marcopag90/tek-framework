package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.security.form.auth.RegisterForm
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.model.auth.TekUser
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.TekUserRepository
import com.tek.security.service.AuthService
import com.tek.security.service.RoleService
import com.tek.security.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import javax.validation.Validator

@Suppress("UNUSED")
@Service
class UserServiceImpl(
    private val authService: AuthService,
    private val roleService: RoleService,
    private val userRepository: TekUserRepository,
    private val roleRepository: RoleRepository,
    @Qualifier("security_validator") private val validator: Validator,
    private val coreMessageSource: CoreMessageSource,
    private val securityMessageSource: SecurityMessageSource
) : UserService {

    private val log by LoggerDelegate()

    companion object {
        const val REFRESH_PASSWORD_EXPIRATION = 3L
    }

    @Transactional
    override fun register(registerForm: RegisterForm): TekUser {

        log.debug("Processing user form validation with data: $registerForm")

        val message = securityMessageSource.getSecuritySource()
            .getMessage(
                SecurityMessageSource.errorNotValidPassword,
                null,
                LocaleContextHolder.getLocale()
            )
        authService.isValidPassword(registerForm.password).isFalse {
            throw TekValidationException(mutableMapOf(RegisterForm::password.name to message))
        }

        userRepository.existsByUsername(registerForm.username).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageSource.errorConflictUsername,
                    parameters = arrayOf(registerForm.username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }

        userRepository.existsByEmail(registerForm.email).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageSource.errorConflictEmail,
                    parameters = arrayOf(registerForm.email)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }
        val (isAcceptable, constraintMessage) = authService.checkPasswordConstraints(
            registerForm.username, registerForm.email, registerForm.password
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

        roleRepository.findByName(RoleName.USER)?.let { role ->
            return userRepository.save(
                TekUser().apply {
                    username = registerForm.username
                    password = authService.passwordEncoder().encode(registerForm.password)
                    email = registerForm.email
                    this.pwdExpireAt = LocalDate.now().plusMonths(REFRESH_PASSWORD_EXPIRATION)
                    this.roles.add(role)
                }
            )
        } ?: throw TekResourceNotFoundException(
            data = ServiceExceptionData(
                source = coreMessageSource,
                message = CoreMessageSource.errorNotFoundResource,
                parameters = arrayOf(RoleName.USER.name)
            )
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekUser> {
        log.debug("Fetching data from repository: $userRepository")
        predicate?.let {
            return userRepository.findAll(predicate, pageable)
        } ?: return userRepository.findAll(pageable)
    }

    override fun readOne(id: Long): TekUser {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
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

    //TODO check if there's a parameter not matching TekUser model
    @Suppress("UNCHECKED_CAST")
    @Transactional
    override fun update(properties: Map<String, Any?>, id: Long): TekUser {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        val userToUpdate = optional.get()
        /*
        Updatable user parameters by administrator:
        username, password, email,
        userExpireAt, roles, enabled
         */
        if (properties.containsKey(TekUser::username.name)) {
            val username = properties[TekUser::username.name] as String?

            if (username.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::username.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            if (userToUpdate.username != username)
                userRepository.existsByUsername(username).isTrue {
                    throw TekServiceException(
                        data = ServiceExceptionData(
                            source = securityMessageSource,
                            message = SecurityMessageSource.errorConflictUsername,
                            parameters = arrayOf(username)
                        ),
                        httpStatus = HttpStatus.CONFLICT
                    )
                }
            userToUpdate.username = username
        }

        if (properties.containsKey(TekUser::password.name)) {
            val password = properties[TekUser::password.name] as String?

            if (password.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::password.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            authService.isValidPassword(password).isFalse {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = securityMessageSource,
                        message = SecurityMessageSource.errorNotValidPassword
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            userToUpdate.password = authService.passwordEncoder().encode(password)
        }

        if (properties.containsKey(TekUser::email.name)) {
            val email = properties[TekUser::email.name] as String?

            if (email.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::email.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            if (userToUpdate.email != email)
                userRepository.existsByEmail(email).isTrue {
                    throw TekServiceException(
                        data = ServiceExceptionData(
                            source = securityMessageSource,
                            message = SecurityMessageSource.errorConflictEmail,
                            parameters = arrayOf(email)
                        ),
                        httpStatus = HttpStatus.CONFLICT
                    )
                }
            userToUpdate.email = email
        }

        if (properties.containsKey(TekUser::userExpireAt.name)) {
            val userExpireAt = properties[TekUser::userExpireAt.name] as LocalDate?
            userToUpdate.userExpireAt = userExpireAt
        }

        if (properties.containsKey(TekUser::enabled.name)) {
            val enabled = properties[TekUser::enabled.name] as Boolean?
                ?: throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::enabled.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            userToUpdate.enabled = enabled
        }

        if (properties.containsKey(TekUser::roles.name)) {
            userToUpdate.roles = mutableSetOf()
            val roles = properties[TekUser::roles.name] as MutableList<Long>
            for (role in roles) {
                userToUpdate.roles.add(roleService.readOne(role))
            }
        }

        val (isAcceptable, constraintMessage) = authService.checkPasswordConstraints(
            userToUpdate.username!!,
            userToUpdate.email!!,
            userToUpdate.password!!
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
        val violations = validator.validate(userToUpdate)
        if (violations.isNotEmpty()) {
            val violationMap = mutableMapOf<String, String>()
            for (v in violations)
                violationMap[v.propertyPath.toList()[0].name] = v.message
            throw TekValidationException(violationMap)
        }
        return userRepository.save(userToUpdate)
    }

    @Transactional
    override fun delete(id: Long): Long {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        userRepository.deleteById(id)
        return id
    }
}