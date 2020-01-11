package com.tek.security.model.auth

import com.tek.security.model.enums.RoleName
import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.TypeName
import java.io.Serializable
import javax.persistence.*

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@TypeName("role")
@Table(name = "role")
class Role(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 10) val name: RoleName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.DETACH])
    @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privileges: MutableSet<Privilege> = mutableSetOf()
}

