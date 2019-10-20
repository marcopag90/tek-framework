package it.jbot.security.model

import it.jbot.security.JBotUserDetails
import org.hibernate.annotations.Type
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
    @Column(name = "username", unique = true) @Size(min = 3, max = 20) @NotBlank var userName: String,
    @Column(name = "password") @NotBlank var passWord: String,
    @Column(unique = true) @Size(max = 50) @NotBlank @Email var email: String
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    //TODO UserActivityAudit

    //TODO need to implement custom type for UserDetails(reverse boolean in db)

    @Column(name = "userExpireAt")
    @Temporal(TemporalType.TIMESTAMP)
    var userExpireAt: Date? = null

    @Column(name = "expired")
    @Type(type = "yes_no")
    var accountNonExpired: Boolean = true

    @Column(name = "locked")
    @Type(type = "yes_no")
    var accountNonLocked: Boolean = true

    @Temporal(TemporalType.TIMESTAMP)
    var pwdExpireAt: Date? = null

    @Column(name = "credentialsExpired")
    @Type(type = "yes_no")
    var credentialsNonExpired: Boolean = true

    @Type(type = "yes_no")
    var enabled: Boolean = true

    @Temporal(TemporalType.TIMESTAMP)
    var lastLogin: Date? = null

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = hashSetOf()

    constructor(user: User) : this(
        userName = user.userName,
        passWord = user.passWord,
        email = user.email
    ) {
        this.id = user.id
        this.userName = user.userName
        this.passWord = user.passWord
        this.email = user.email
        this.accountNonExpired = user.accountNonExpired
        this.accountNonLocked = user.accountNonLocked
        this.credentialsNonExpired = user.credentialsNonExpired
        this.enabled = user.enabled
        this.roles = user.roles
    }
}

fun main(args: Array<String>) {

    var user1 = User("marcopag", "fatal1ty", "marcopag90@gmail.com")
    user1.id = 5
    var user2 = User("marcopag", "fatal1ty", "marcopag90@gmail.com")
    user2.id = 5

    print("${(user1 == user2)}") //it's not the same since User is not a data class and no override equals is implemented

    var userDetails: JBotUserDetails =
        JBotUserDetails(User("marcopag", "fatal1ty", "marcopag90@gmail.com")
            .apply {
                //other JBotUserDetails parameters
            })
}
