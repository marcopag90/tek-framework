package it.jbot.security.service.impl

import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.service.JBotCrudService
import it.jbot.core.util.addMonthsFromNow
import it.jbot.core.util.isFalse
import it.jbot.core.util.isTrue
import it.jbot.core.validation.EntityValidator
import it.jbot.security.form.RegisterForm
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.JBotUser
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.AuthService
import it.jbot.security.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserCrudService(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource,
    validator: EntityValidator
) : UserService, JBotCrudService<JBotUser, Long, UserRepository>(userRepository, validator) {

    @Transactional
    override fun register(registerForm: RegisterForm): JBotUser {

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
}