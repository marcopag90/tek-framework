package it.jbot.security.model

import it.jbot.security.JBotUserDetails
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.persistence.TemporalType
import kotlin.collections.ArrayList

/**
 * Persistable User for Authentication purpose
 * @author PaganM
 */
@Entity
@Table(name = "user")
class User(
    @Column(name = "username", unique = true) @Size(
        min = 3,
        max = 20
    ) @NotBlank var userName: String,
    @Column(name = "password") @NotBlank var passWord: String,
    @Column(unique = true) @Size(max = 50) @NotBlank @Email var email: String
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    
    //TODO UserActivityAudit
    
    @Column(name = "userExpireAt")
    @Temporal(TemporalType.TIMESTAMP)
    var userExpireAt: Date? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    var pwdExpireAt: Date? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    var lastLogin: Date? = null
    
    @Type(type = "numeric_boolean")
    var enabled: Boolean = true
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
    
    //Mandatory user fields
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
}

