package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.LoggerDelegate
import it.jbot.core.util.addMonthsFromNow
import it.jbot.core.util.isFalse
import it.jbot.core.util.isTrue
import it.jbot.security.form.auth.RegisterForm
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.JBotUser
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.AuthService
import it.jbot.security.service.UserService
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
    override fun register(registerForm: RegisterForm): JBotUser {

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
                JBotUser().apply {
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

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<JBotUser>> {
        log.debug("Fetching data from repository: $userRepository")
        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, userRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, userRepository.findAll(pageable)), HttpStatus.OK)
    }

    override fun readOne(id: Long): ResponseEntity<JBotResponseEntity<JBotUser>> {
        log.debug("Accessing $userRepository for entity: ${JBotUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw JBotServiceException(
            "Entity ${JBotUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        return ResponseEntity(JBotResponseEntity(HttpStatus.OK, optional.get()), HttpStatus.OK)
    }

    override fun update() {
        TODO("Update user to be decided!")
    }

    override fun delete(id: Long): ResponseEntity<JBotResponseEntity<Long>> {
        log.debug("Accessing $userRepository for entity: ${JBotUser::class.java.name} with id:$id")

        val optional = userRepository.findById(id)
        if (!optional.isPresent) throw JBotServiceException(
            "Entity ${JBotUser::class.java.name} with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        userRepository.deleteById(id)
        log.debug("Delete success!")
        return ResponseEntity(JBotResponseEntity(HttpStatus.OK, id), HttpStatus.OK)
    }
}