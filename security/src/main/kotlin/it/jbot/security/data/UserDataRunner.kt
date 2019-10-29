package it.jbot.security.data

import it.jbot.security.model.Role
import it.jbot.security.model.RoleName
import it.jbot.security.model.User
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.shared.ifNull
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserDataRunner(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val jBotPasswordEncoder: BCryptPasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        userRepository.findByUserName("admin").ifNull {
            userRepository.save(
                createUser(
                    username = "admin",
                    password = "admin",
                    email = "admin@gmail.com",
                    roles = mutableSetOf(Role(name = RoleName.ROLE_ADMIN), Role(name = RoleName.ROLE_USER))
                )
            )
        }
        
        userRepository.findByUserName("user").ifNull {
            userRepository.save(
                createUser(
                    username = "user",
                    password = "user",
                    email = "user@gmail.com",
                    roles = mutableSetOf(Role(name = RoleName.ROLE_USER))
                )
            )
        }
    }
    
    private fun createUser(
        username: String,
        password: String,
        email: String,
        roles: MutableSet<Role>
    ): User {
        
        var userRoles = mutableSetOf<Role>()
        for (role in roles)
            roleRepository.findByName(role.name)?.let {
                userRoles.add(it)
            }
        
        return User(username, jBotPasswordEncoder.encode(password), email).apply {
            this.roles = userRoles
        }
    }
}

