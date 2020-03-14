package com.tek.security.common.model

import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.TypeName
import java.io.Serializable
import javax.persistence.*

enum class RoleName {

    // Client roles
    DASHBOARD,
    MENU,

    // Crud roles
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