package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.addMonthsFromNow
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.security.form.auth.RegisterForm
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.model.Role
import com.tek.security.model.TekUser
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.UserRepository
import com.tek.security.service.AuthService
import com.tek.security.service.RoleService
import com.tek.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator

@Service
class UserServiceImpl(
    private val authService: AuthService,
    private val roleService: RoleService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val validator: Validator,
    private val messageSource: SecurityMessageSource
) : UserService {

    private val log by LoggerDelegate()

    @Transactional
    override fun register(registerForm: RegisterForm): TekUser {

        log.debug("Processing user form validation with data: $registerForm")

        authService.isValidPassword(registerForm.password).isFalse {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorNotValidPassword
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        userRepository.existsByUsername(registerForm.username).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorConflictUsername,
                    parameters = arrayOf(registerForm.username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }

        userRepository.existsByEmail(registerForm.email).isTrue {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorConflictEmail,
                    parameters = arrayOf(registerForm.email)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }

        authService.checkPasswordConstraints(registerForm.username, registerForm.email, registerForm.password).isFalse {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorConflictPassword,
                    parameters = arrayOf(registerForm.email)
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
                    this.pwdExpireAt = addMonthsFromNow(3)
                    this.roles.add(role)
                }
            )
        } ?: throw TekServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = SecurityMessageSource.errorRoleNotFound,
                parameters = arrayOf(RoleName.USER.name)
            ),
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<TekUser>> {
        log.debug("Fetching data from repository: $userRepository")
        predicate?.let {
            return ResponseEntity(
                TekPageResponse(
                    HttpStatus.OK,
                    userRepository.findAll(predicate, pageable)
                ),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(
            TekPageResponse(
                HttpStatus.OK,
                userRepository.findAll(pageable)
            ), HttpStatus.OK
        )
    }

    override fun readOne(id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw TekServiceException(
            "Entity ${TekUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        return ResponseEntity(TekResponseEntity(HttpStatus.OK, optional.get()), HttpStatus.OK)
    }

    @Transactional
    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw TekServiceException(
            "Entity ${TekUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
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
                        source = messageSource,
                        message = SecurityMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::username.name)
                    ),
                    httpStatus = HttpStatus.CONFLICT
                )
            }
            userRepository.existsByUsername(username).isTrue {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
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
                        source = messageSource,
                        message = SecurityMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::password.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            authService.isValidPassword(password).isFalse {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
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
                        source = messageSource,
                        message = SecurityMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::email.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            userRepository.existsByEmail(email).isTrue {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
                        message = SecurityMessageSource.errorConflictEmail,
                        parameters = arrayOf(email)
                    ),
                    httpStatus = HttpStatus.CONFLICT
                )
            }
            userToUpdate.email = email
        }

        if (properties.containsKey(TekUser::userExpireAt.name)) {
            val userExpireAt = properties[TekUser::userExpireAt.name] as Date?
            userToUpdate.userExpireAt = userExpireAt
        }

        if (properties.containsKey(TekUser::enabled.name)) {
            val enabled = properties[TekUser::enabled.name] as Boolean?
                ?: throw TekServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
                        message = SecurityMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::enabled.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            userToUpdate.enabled = enabled
        }

        if (properties.containsKey(TekUser::roles.name)) {
            userToUpdate.roles = mutableSetOf()
            val roles = properties[TekUser::roles.name] as MutableList<*>
            for (role in roles) {
                val response = roleService.read(role as String)
                val body = response.body as TekResponseEntity<Role>
                userToUpdate.roles.add(body.result!!)
            }
        }

        authService.checkPasswordConstraints(userToUpdate.username!!, userToUpdate.email!!, userToUpdate.password!!)
            .isFalse {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
                        message = SecurityMessageSource.errorConflictPassword,
                        parameters = arrayOf(userToUpdate.email!!)
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

        return ResponseEntity(TekResponseEntity(HttpStatus.OK, userRepository.save(userToUpdate)), HttpStatus.OK)
    }

    @Transactional
    override fun delete(id: Long): ResponseEntity<TekResponseEntity<Long>> {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw TekServiceException(
            "Entity ${TekUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        userRepository.deleteById(id)
        log.debug("Delete success!")
        return ResponseEntity(TekResponseEntity(HttpStatus.OK, id), HttpStatus.OK)
    }
}