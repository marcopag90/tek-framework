package com.tek.security.common.model

import org.javers.core.metamodel.annotation.TypeName
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

const val TEK_PROFILE_FULL = "TekProfile.full"

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@TypeName("profile")
@Table(name = "profile")
@NamedEntityGraph(
    name = TEK_PROFILE_FULL,
    includeAllAttributes = true
)
class TekProfile(
    @field:NotBlank
    @field:Size(min = 1, max = 10)
    @Column(length = 10, nullable = false)
    var name: String? = null
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "profiles_roles",
        joinColumns = [JoinColumn(name = "profile_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: MutableSet<TekRole> = mutableSetOf()

    @ManyToMany(mappedBy = "profiles")
    var users: MutableSet<TekUser> = hashSetOf()
}
