package com.tek.security.common.model

import com.tek.security.common.model.enums.RoleName
import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.TypeName
import java.io.Serializable
import javax.persistence.*

const val TEK_ROLE_FULL = "TekRole.full"

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@TypeName("role")
@Table(name = "role")
@NamedEntityGraph(
    name = TEK_ROLE_FULL,
    includeAllAttributes = true
)
class TekRole(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 10) val name: RoleName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.DETACH])
    @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privileges: MutableSet<TekPrivilege> = mutableSetOf()
}

