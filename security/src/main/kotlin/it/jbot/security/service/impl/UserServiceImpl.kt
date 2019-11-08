package it.jbot.security.service.impl

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.dto.SignUpForm
import it.jbot.security.model.User
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.UserService
import it.jbot.shared.exception.JBotServiceException
import it.jbot.shared.util.JBotDateUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: JBotPasswordEncoder
) : UserService {
    
    //TODO message template refactoring
    @Transactional
    override fun registerUser(signUpForm: SignUpForm): User {
        
        if (userRepository.existsByUserName(signUpForm.username))
            throw JBotServiceException("User with username: ${signUpForm.username} already exists!", HttpStatus.CONFLICT)
        
        if (userRepository.existsByEmail(signUpForm.email))
            throw JBotServiceException("User with email: ${signUpForm.email} already exists!", HttpStatus.CONFLICT)
        
        if (signUpForm.roles.isEmpty())
            throw JBotServiceException("${SignUpForm::roles.name} should not be empty!", HttpStatus.BAD_REQUEST)
        
        var user: User = User(
            userName = signUpForm.username,
            passWord = passwordEncoder.encoder().encode(signUpForm.password),
            email = signUpForm.email
        )
        
        user.pwdExpireAt = JBotDateUtils.addMonthsFromNow(3)
        
        for (role in signUpForm.roles)
            when (role) {
                RoleName.ROLE_ADMIN.name ->
                    roleRepository.findByName(RoleName.ROLE_ADMIN)?.let {
                        user.roles.add(it)
                    } ?: throw JBotServiceException("User role: $role not found!", HttpStatus.NOT_FOUND)
                
                RoleName.ROLE_USER.name ->
                    roleRepository.findByName(RoleName.ROLE_USER)?.let {
                        user.roles.add(it)
                    } ?: throw JBotServiceException("User role: $role not found!", HttpStatus.NOT_FOUND)
                
                else -> throw JBotServiceException("User role: $role not found!", HttpStatus.NOT_FOUND)
            }
        
        return userRepository.save(user)
    }
}