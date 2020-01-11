package com.tek.security.model.auth

import com.tek.security.model.enums.PrivilegeName
import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.TypeName
import java.io.Serializable
import javax.persistence.*

@Entity
@TypeName("privilege")
@Table(name = "privilege")
class Privilege(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 50) val name: PrivilegeName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}