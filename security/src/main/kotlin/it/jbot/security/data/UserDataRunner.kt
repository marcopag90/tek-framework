package it.jbot.security.data

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.model.Role
import it.jbot.security.model.User
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import it.jbot.core.util.ifNull
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDataRunner(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        userRepository.findByUserName("admin").ifNull {
            createUser(
                username = "admin",
                password = "admin",
                email = "admin@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.ROLE_ADMIN),
                    Role(name = RoleName.ROLE_USER)
                ),
                enabled = true,
                pwdExpireAt = GregorianCalendar().apply {
                    this.set(2099, GregorianCalendar.DECEMBER, 31)
                }.time
            )
        }

        userRepository.findByUserName("test").ifNull {
            createUser(
                username = "test",
                password = "test",
                email = "test@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.ROLE_USER)
                ),
                enabled = true,
                pwdExpireAt = GregorianCalendar().apply {
                    this.set(2099, GregorianCalendar.DECEMBER, 31)
                }.time
            )
        }
    }

    private fun createUser(
        username: String,
        password: String,
        email: String,
        roles: MutableSet<Role>,
        enabled: Boolean,
        userExpireAt: Date? = null,
        pwdExpireAt: Date,
        lastLogin: Date? = null
    ): User {

        var userRoles = mutableSetOf<Role>()
        for (role in roles)
            roleRepository.findByName(role.name)?.let {
                userRoles.add(it)
            }

        return User(
            username,
            jBotPasswordEncoder.encoder().encode(password),
            email
        ).apply {
            this.roles = userRoles
            this.enabled = enabled
            this.userExpireAt = userExpireAt
            this.pwdExpireAt = pwdExpireAt
            this.lastLogin = lastLogin
        }.let(userRepository::save)
    }
}

