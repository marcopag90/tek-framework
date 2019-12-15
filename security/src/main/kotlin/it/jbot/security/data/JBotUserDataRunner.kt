package it.jbot.security.data

import it.jbot.core.util.ifNull
import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.model.JBotUser
import it.jbot.security.model.Privilege
import it.jbot.security.model.Role
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Order(DataOrder.user)
@Component
class JBotUserDataRunner(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        userRepository.findByUsername("admin").ifNull {
            createUser(
                username = "admin",
                password = "admin",
                email = "admin@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.ADMIN).apply {
                        this.privileges = mutableSetOf(
                            Privilege(name = PrivilegeName.MENU)
                        )
                    },
                    Role(name = RoleName.USER)
                ),
                enabled = true,
                pwdExpireAt = GregorianCalendar().apply {
                    this.set(2099, GregorianCalendar.DECEMBER, 31)
                }.time
            )
        }

        userRepository.findByUsername("test").ifNull {
            createUser(
                username = "test",
                password = "test",
                email = "test@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.USER)
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
    ): JBotUser {

        val userRoles = mutableSetOf<Role>()
        for (role in roles)
            roleRepository.findByName(role.name)?.let {
                userRoles.add(it)
            }

        return JBotUser(
            username,
            jBotPasswordEncoder.bcryptEncoder().encode(password),
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

