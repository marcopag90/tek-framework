package com.tek.security.common.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import com.tek.security.common.RolePrefix
import com.tek.security.common.audit.UserActivityAudit
import org.javers.core.metamodel.annotation.DiffIgnore
import org.javers.core.metamodel.annotation.TypeName
import java.time.Instant
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

const val TEK_USER_FULL = "TekUser.full"

/**
 * Persistable User for Authentication purpose
 */
@Entity
@TypeName("users")
@Table(name = "users")
@NamedEntityGraph(
    name = TEK_USER_FULL,
    includeAllAttributes = true,
    attributeNodes = [NamedAttributeNode(value = "profiles", subgraph = "profiles.roles")],
    subgraphs = [
        NamedSubgraph(
            name = "profiles.roles",
            attributeNodes = [NamedAttributeNode(value = "roles")]
        )
    ]
)
@RolePrefix(value = "user")
class TekUser : UserActivityAudit() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    @Column(name = "username", length = 20, unique = true, nullable = false)
    var username: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @field:NotBlank
    @Column(name = "password", nullable = false)
    @DiffIgnore
    var password: String? = null

    @field:Email
    @field:Size(min = 1, max = 50)
    @Column(name = "email", length = 50, unique = true, nullable = true)
    var email: String? = null

    /**
     * Condition: _OPTIONAL_
     *
     * Sets a User expiration date
     */
    @Column(name = "expiration", nullable = true)
    var userExpireAt: LocalDate? = null

    /**
     * Condition: _REQUIRED_
     *
     * Sets a User password expiration date
     */
    @Column(name = "password_expiration", nullable = false)
    var pwdExpireAt: LocalDate? = null

    /**
     * Condition: _OPTIONAL_
     *
     * Sets a User last login date
     */
    @Column(name = "last_login")
    @DiffIgnore
    var lastLogin: Instant? = null

    /**
     * Condition: _REQUIRED_
     *
     * Granted Authorities to access both _Server_ and _Client_ resources.
     *
     * Implementations of authorities are tied to Business Logic requirements.
     */
    @ManyToMany
    @JoinTable(
        name = "users_profiles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "profile_id")]
    )
    var profiles: MutableSet<TekProfile> = mutableSetOf()

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    var preference: TekPreference? = null

    // --------------------------------- Account management --------------------------------------

    //TODO decide user registration path (email with confirm activation, credentials etc..) to enable it
    /**
     * Condition: _REQUIRED_
     *
     * An account has been administratively or automatically _disabled_ for some reason.
     *
     * Usually some action is required to release it (Server or Client side)
     */
    @Column(name = "enabled", length = 1, nullable = false)
    var enabled: Boolean = false

    /**
     * Condition: _OPTIONAL_
     *
     * Checked by [TekUser.userExpireAt].
     *
     * If you don't need your User to expire, you just have to leave [TekUser.userExpireAt] = **null**
     */
    @Column(name = "expired", length = 1, nullable = true)
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
    @Column(name = "locked", length = 1, nullable = false)
    var accountLocked: Boolean = false

    /**
     * Condition: _REQUIRED_
     * Checked by [TekUser.pwdExpireAt]
     *
     * A User with credentials expired has to change password, due to **GDPR** policy
     */
    @Column(name = "credentials_expired", length = 1, nullable = false)
    var credentialsExpired: Boolean = false
}



