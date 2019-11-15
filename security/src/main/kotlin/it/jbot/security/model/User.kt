package it.jbot.security.model

import it.jbot.security.audit.UserActivityAudit
import it.jbot.shared.converter.ItalianBoolean
import it.jbot.shared.util.JBotDateUtils.isDateExpired
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.persistence.TemporalType

/**
 * Persistable User for Authentication purpose
 * @author PaganM
 */
@Entity
@Table(name = "users")
class User(
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "username", unique = true, nullable = false)
    var userName: String,
    
    //TODO password regex
    @NotBlank
    @Column(name = "password", nullable = false)
    var passWord: String,
    
    @Email
    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    var email: String
) : UserActivityAudit(), Serializable {
    
    //Mandatory User fields
    constructor(user: User) : this(
        userName = user.userName,
        passWord = user.passWord,
        email = user.email
    ) {
        this.id = user.id
        this.userName = user.userName
        this.passWord = user.passWord
        this.email = user.email
        this.enabled = user.enabled
        this.roles = user.roles
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    
    //TODO Check from REST service if user activity and javers works (all crud operations)
    
    /**
     * Condition: _OPTIONAL_
     *
     * Sets a User expiration date
     */
    @Column(name = "userExpireAt")
    @Temporal(TemporalType.TIMESTAMP)
    var userExpireAt: Date? = null
    
    /**
     * Condition: _REQUIRED_
     *
     * Sets a User password expiration date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var pwdExpireAt: Date? = null
    
    /**
     * Condition: _REQUIRED_
     *
     * Sets a User last login date
     */
    @Temporal(TemporalType.TIMESTAMP)
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
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
    
    /*##################### Account Management #####################*/
    
    //TODO decide user registration path (email with confirm activation, credentials etc..) to enable it
    /**
     * Condition: _REQUIRED_
     *
     * An account has been administratively or automatically _disabled_ for some reason.
     *
     * Usually some action is required to release it (Server or Client side)
     */
    @Convert(converter = ItalianBoolean::class)
    var enabled: Boolean = true
    
    /**
     * Condition: _OPTIONAL_
     *
     * Checked by [User.userExpireAt].
     *
     * If you don't need your User to expire, you just have to leave [User.userExpireAt] = **null**
     */
    @Convert(converter = ItalianBoolean::class)
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
    @Convert(converter = ItalianBoolean::class)
    var accountLocked: Boolean = false
    
    /**
     * Condition: _REQUIRED_
     * Checked by [User.pwdExpireAt]
     *
     * A User with credentials expired has to change password, due to **GDPR** policy
     */
    @Convert(converter = ItalianBoolean::class)
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
    
    /*##################### Account Management #####################*/
}



