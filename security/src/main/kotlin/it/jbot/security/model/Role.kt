package it.jbot.security.model

import it.jbot.security.model.enums.RoleName
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@Table(name = "role")
class Role(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 10) val name: RoleName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privileges: MutableSet<Privilege> = mutableSetOf()
}

