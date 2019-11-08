package it.jbot.security

import it.jbot.security.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.stream.Collectors

/**
 * POJO to store User information for Authentication purpose
 *
 * All Pojo/Entities extending or implementing this _MUST_ be Serializable
 */
class JBotUserDetails : User, UserDetails, Serializable {
    
    constructor(user: User) : super(user)
    
    override fun getUsername(): String = super.userName
    override fun getPassword(): String = super.passWord
    
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        roles.stream().map { role ->
            SimpleGrantedAuthority(role.name.name)
        }.collect(Collectors.toList())
    
    override fun isEnabled(): Boolean = super.enabled
    
    // wish I was in the brain of the guy who wrote these interface methods!
    override fun isAccountNonExpired(): Boolean = !super.accountExpired
    override fun isAccountNonLocked(): Boolean = !super.accountLocked
    override fun isCredentialsNonExpired(): Boolean = !super.credentialsExpired
}

