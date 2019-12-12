package it.jbot.security.service.impl

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.addMonthsFromNow
import it.jbot.core.util.isFalse
import it.jbot.core.util.isTrue
import it.jbot.security.form.RegisterForm
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.User
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

    @Transactional
    override fun register(registerForm: RegisterForm): User {

        authService.isValidPassword(registerForm.password).isFalse {
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = SecurityMessageSource.errorNotValidPassword
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        userRepository.existsByUserName(registerForm.username).isTrue {
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

        roleRepository.findByName(RoleName.ROLE_USER)?.let { role ->
            return userRepository.save(
                User(
                    userName = registerForm.username,
                    passWord = authService.passwordEncoder().encode(registerForm.password),
                    email = registerForm.email
                ).apply {
                    this.pwdExpireAt = addMonthsFromNow(3)
                    this.roles.add(role)
                }
            )
        } ?: throw JBotServiceException(
            data = ServiceExceptionData(
                source = messageSource,
                message = SecurityMessageSource.errorRoleNotFound,
                parameters = arrayOf(RoleName.ROLE_USER.name)
            ),
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    //TODO update service
    override fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<JBotEntityResponse<User>> {

        val user = User("", "", "")
        return ResponseEntity(
            JBotEntityResponse(HttpStatus.OK, user), HttpStatus.OK
        )
    }

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<User>> {

        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, userRepository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, userRepository.findAll(pageable)), HttpStatus.OK)
    }
}