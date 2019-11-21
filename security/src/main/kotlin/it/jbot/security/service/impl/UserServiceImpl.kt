package it.jbot.security.service.impl

import it.jbot.security.dto.RegisterForm
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorConflictEmail
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorConflictUsername
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorEmptyRole
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorNotValidPassword
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import it.jbot.security.model.User
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.JBotAuthService
import it.jbot.security.service.UserService
import it.jbot.shared.util.JBotDateUtils
import it.jbot.shared.util.ifNot
import it.jbot.web.exception.JBotServiceException
import it.jbot.web.exception.ServiceExceptionData
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val authService: JBotAuthService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val messageSource: SecurityMessageSource = SecurityMessageSource()
) : UserService {

    @Transactional
    override fun registerUser(registerForm: RegisterForm): User {

        authService.isValidPassword(registerForm.password).ifNot {
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = errorNotValidPassword
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        if (userRepository.existsByUserName(registerForm.username))
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = errorConflictUsername,
                    parameters = arrayOf(registerForm.username)
                ),
                httpStatus = HttpStatus.CONFLICT
            )

        if (userRepository.existsByEmail(registerForm.email))
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = errorConflictEmail,
                    parameters = arrayOf(registerForm.email)
                ),
                httpStatus = HttpStatus.CONFLICT
            )

        //TODO check role is empty
        if (registerForm.roles.isEmpty())
            throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = errorEmptyRole,
                    parameters = arrayOf(RegisterForm::roles.name)
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        val user = User(
            userName = registerForm.username,
            passWord = authService.passwordEncoder().encode(registerForm.password),
            email = registerForm.email
        )

        user.pwdExpireAt = JBotDateUtils.addMonthsFromNow(3)

        for (role in registerForm.roles)
            roleRepository.findByName(RoleName.fromString(role))?.let {
                user.roles.add(it)
            } ?: throw JBotServiceException(
                data = ServiceExceptionData(
                    source = messageSource,
                    message = errorRoleNotFound,
                    parameters = arrayOf(role)
                ),
                httpStatus = HttpStatus.NOT_FOUND
            )
        return userRepository.save(user)
    }
}
