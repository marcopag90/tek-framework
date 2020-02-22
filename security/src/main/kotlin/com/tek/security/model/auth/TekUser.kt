package com.tek.security.model.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.tek.security.audit.UserActivityAudit
import com.tek.security.model.business.Preferences
import org.hibernate.annotations.LazyToOne
import org.hibernate.annotations.LazyToOneOption
import org.javers.core.metamodel.annotation.DiffIgnore
import org.javers.core.metamodel.annotation.TypeName
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

const val AUTH_USER = "auth-user"

/**
 * Persistable User for Authentication purpose
 */
@Entity
@TypeName("users")
@Table(name = "users")
@NamedEntityGraphs(
    NamedEntityGraph(
        name = AUTH_USER,
        attributeNodes = [NamedAttributeNode(value = "roles", subgraph = "roles-privileges")],
        subgraphs = [
            NamedSubgraph(
                name = "roles-privileges",
                attributeNodes = [NamedAttributeNode(value = "privileges")]
            )
        ]
    )
)
class TekUser : UserActivityAudit() {

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
    @DiffIgnore
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
    var userExpireAt: LocalDate? = null

    /**
     * Condition: _REQUIRED_
     *
     * Sets a User password expiration date
     */
    @Column(name = "password_expiration", nullable = false)
    var pwdExpireAt: LocalDate? = null

    /**
     * Condition: _REQUIRED_
     *
     * Sets a User last login date
     */
    @Column(name = "last_login")
    var lastLogin: Instant? = null

    /**
     * Condition: _REQUIRED_
     *
     * Granted Authorities to access both _Server_ and _Client_ resources.
     *
     * Implementations of authorities are tied to Business Logic requirements.
     */
    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.DETACH])
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()

    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL]
    )
    var preferences: Preferences? = null

    /*--------------------------------- Account Management ---------------------------------------*/

    companion object {
        const val EXPIRATION_MONTHS = 6L
    }

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
     * Checked by [TekUser.userExpireAt].
     *
     * If you don't need your User to expire, you just have to leave [TekUser.userExpireAt] = **null**
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
     * Checked by [TekUser.pwdExpireAt]
     *
     * A User with credentials expired has to change password, due to **GDPR** policy
     */
    @Column(name = "credentials_expired", length = 1)
    var credentialsExpired: Boolean = false

    /**
     * Check if User account has expired
     */
    fun isAccountExpired(userExpireAt: LocalDate?): Boolean = userExpireAt?.let {
        userExpireAt < LocalDate.now()
    } ?: false

    /**
     * Check if User account has to become locked
     */
    fun isAccountLocked(lastLogin: Instant?): Boolean = lastLogin?.let { it ->
        val today = LocalDateTime.now()
        today.minusMonths(EXPIRATION_MONTHS)
            .isAfter(it.atZone(ZoneId.systemDefault()).toLocalDateTime())
    } ?: false

    /**
     * Check if User password has expired
     */
    fun isCredentialsExpired(passwordExpireAt: LocalDate): Boolean =
        passwordExpireAt < LocalDate.now()

}



