package com.tek.security.service.impl

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.exception.JBotServiceException
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.addMonthsFromNow
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.security.form.auth.RegisterForm
import com.tek.security.i18n.SecurityMessageSource
import com.tek.security.model.TekUser
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.UserRepository
import com.tek.security.service.AuthService
import com.tek.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource
) : UserService {

    private val log by LoggerDelegate()

    @Transactional
    override fun register(registerForm: RegisterForm): TekUser {

        log.debug("Processing user form validation with data: $registerForm")

        authService.isValidPassword(registerForm.password).isFalse {
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorNotValidPassword
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        userRepository.existsByUsername(registerForm.username).isTrue {
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorConflictUsername,
                    parameters = arrayOf(registerForm.username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }

        userRepository.existsByEmail(registerForm.email).isTrue {
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorConflictEmail,
                    parameters = arrayOf(registerForm.email)
                ),
                httpStatus = HttpStatus.CONFLICT
            )
        }

        authService.checkPasswordConstraints(registerForm.username, registerForm.email, registerForm.password).isFalse {
            throw JBotServiceException(
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
        } ?: throw JBotServiceException(
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
            ), HttpStatus.OK)
    }

    override fun readOne(id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw JBotServiceException(
            "Entity ${TekUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        return ResponseEntity(TekResponseEntity(HttpStatus.OK, optional.get()), HttpStatus.OK)
    }

    @Transactional
    override fun update() {
        TODO("Update user to be decided!")
    }

    @Transactional
    override fun delete(id: Long): ResponseEntity<TekResponseEntity<Long>> {
        log.debug("Accessing $userRepository for entity: ${TekUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw JBotServiceException(
            "Entity ${TekUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        userRepository.deleteById(id)
        log.debug("Delete success!")
        return ResponseEntity(TekResponseEntity(HttpStatus.OK, id), HttpStatus.OK)
    }
}