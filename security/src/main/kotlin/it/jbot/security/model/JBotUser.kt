package it.jbot.security.model

import com.fasterxml.jackson.annotation.JsonProperty
import it.jbot.core.util.isDateExpired
import it.jbot.security.audit.UserActivityAudit
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * Persistable User for Authentication purpose
 */
@Entity
@Table(name = "users")
class JBotUser : UserActivityAudit() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    @Column(name = "username", unique = true, nullable = false)
    var username: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @field:NotBlank
    @Column(name = "password", nullable = false)
    var password: String? = null

    @field:Email
    @field:NotBlank
    @Column(name = "email", unique = true, nullable = false)
    var email: String? = null

    //TODO Check from REST service if user activity and javers works (all crud operations)

    /**
     * Condition: _OPTIONAL_
     *
     * Sets a User expiration date
     */
    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    var userExpireAt: Date? = null

    /**
     * Condition: _REQUIRED_
     *
     * Sets a User password expiration date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "password_expiration", nullable = false)
    var pwdExpireAt: Date? = null

    /**
     * Condition: _REQUIRED_
     *
     * Sets a User last login date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    var lastLogin: Date? = null

    /**
     * Condition: _REQUIRED_
     *
     * Granted Authorities to access both _Server_ and _Client_ resources.
     *
     * Implementations of authorities are tied to Business Logic requirements.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()

    //TODO decide user registration path (email with confirm activation, credentials etc..) to enable it
    /**
     * Condition: _REQUIRED_
     *
     * An account has been administratively or automatically _disabled_ for some reason.
     *
     * Usually some action is required to release it (Server or Client side)
     */
    @Column(name = "enabled", length = 1)
    var enabled: Boolean = true

    /**
     * Condition: _OPTIONAL_
     *
     * Checked by [JBotUser.userExpireAt].
     *
     * If you don't need your User to expire, you just have to leave [JBotUser.userExpireAt] = **null**
     */
    @Column(name = "expired", length = 1)
    var accountExpired: Boolean = false

    //TODO check how to implement login attempts
    /**
     * Condition: _REQUIRED_
     *
     * A Locked User can't perform a login
     *
     * A User becomes locked if one of the following conditions occur:
     * 1) too many login attempts (not yet implemented)
     * 2) has been offline for more than 6 months (standard implementation)
     */
    @Column(name = "locked", length = 1)
    var accountLocked: Boolean = false

    /**
     * Condition: _REQUIRED_
     * Checked by [JBotUser.pwdExpireAt]
     *
     * A User with credentials expired has to change password, due to **GDPR** policy
     */
    @Column(name = "credentials_expired", length = 1)
    var credentialsExpired: Boolean = false

    /**
     * Check if User account has expired
     */
    fun isAccountExpired(userExpireAt: Date?): Boolean = userExpireAt?.let {
        userExpireAt < Date()
    } ?: false

    /**
     * Check if User account has to become locked
     */
    fun isAccountLocked(lastLogin: Date?): Boolean = lastLogin?.let {
        isDateExpired(it, 6)
    } ?: false

    /**
     * Check if User password has expired
     */
    fun isCredentialsExpired(passwordExpireAt: Date): Boolean =
        passwordExpireAt < Date()
}



