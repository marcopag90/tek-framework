package com.tek.security.model

import com.tek.security.model.enums.PrivilegeName
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*

@Entity
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