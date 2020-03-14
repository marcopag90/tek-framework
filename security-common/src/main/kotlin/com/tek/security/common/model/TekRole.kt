package com.tek.security.common.model

import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.TypeName
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.Serializable
import javax.persistence.*

/**
 * Enum to define user roles, to evaluate Spring [SimpleGrantedAuthority]
 */
enum class RoleName {

    PROFILE_CREATE,
    PROFILE_READ,
    PROFILE_UPDATE,
    PROFILE_DELETE,

    ROLE_READ,

    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,

    AUDIT_READ,

    NOTIFICATION_READ,
    NOTIFICATION_UPDATE,
    NOTIFICATION_DELETE
}

@Entity
@TypeName("role")
@Table(name = "role")
class TekRole(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 50) val name: RoleName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}