package it.jbot.security.service.impl

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.dto.RegisterForm
import it.jbot.security.i18n.JBotSecurityMessage.usernameConflict
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.User
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.UserService
import it.jbot.shared.exception.JBotServiceException
import it.jbot.shared.util.JBotDateUtils
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: JBotPasswordEncoder,
    private val messageSource: SecurityMessageSource = SecurityMessageSource()
) : UserService {
    
    //TODO message template refactoring
    @Transactional
    override fun registerUser(registerForm: RegisterForm): User {
        
        if (userRepository.existsByUserName(registerForm.username))
            throw JBotServiceException(
                messageSource.getSecuritySource().getMessage(
                    usernameConflict,
                    arrayOf(registerForm.username),
                    LocaleContextHolder.getLocale()
                ),
                HttpStatus.CONFLICT
            )
        
        if (userRepository.existsByEmail(registerForm.email))
            throw JBotServiceException(
                "User with email: ${registerForm.email} already exists!",
                HttpStatus.CONFLICT
            )
        
        if (registerForm.roles.isEmpty())
            throw JBotServiceException(
                "${RegisterForm::roles.name} should not be empty!",
                HttpStatus.BAD_REQUEST
            )
        
        var user: User = User(
            userName = registerForm.username,
            passWord = passwordEncoder.encoder().encode(registerForm.password),
            email = registerForm.email
        )
        
        user.pwdExpireAt = JBotDateUtils.addMonthsFromNow(3)
        
        for (role in registerForm.roles)
            when (role) {
                RoleName.ROLE_ADMIN.name ->
                    roleRepository.findByName(RoleName.ROLE_ADMIN)?.let {
                        user.roles.add(it)
                    } ?: throw JBotServiceException(
                        "User role: $role not found!",
                        HttpStatus.NOT_FOUND
                    )
                
                RoleName.ROLE_USER.name ->
                    roleRepository.findByName(RoleName.ROLE_USER)?.let {
                        user.roles.add(it)
                    } ?: throw JBotServiceException(
                        "User role: $role not found!",
                        HttpStatus.NOT_FOUND
                    )
                else -> throw JBotServiceException(
                    "User role: $role not found!",
                    HttpStatus.NOT_FOUND
                )
            }
        
        return userRepository.save(user)
    }
}