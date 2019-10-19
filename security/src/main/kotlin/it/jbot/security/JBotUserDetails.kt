package it.jbot.security

import it.jbot.security.model.User
import it.jbot.shared.LoggerDelegate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.logging.Logger
import java.util.stream.Collectors

/**
 * POJO to store User information for Authentication purpose
 */
class JBotUserDetails : User, UserDetails {
    
    private val logger by LoggerDelegate()
    
    constructor(user: User) : super(user)
    
    override fun getUsername(): String = super.userName
    override fun getPassword(): String = super.passWord
    
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        roles.stream().map { role ->
            logger.debug("Granting Authority to ${this.javaClass.simpleName} with role: $role")
            SimpleGrantedAuthority(role.toString())
        }.collect(Collectors.toList())
    
    override fun isEnabled(): Boolean = super.enabled
    override fun isAccountNonExpired(): Boolean = super.accountNonExpired
    override fun isAccountNonLocked(): Boolean = super.accountNonLocked
    override fun isCredentialsNonExpired(): Boolean = super.credentialsNonExpired
}