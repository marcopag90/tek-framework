package com.tek.security.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tek.security.common.RolePrefix
import org.hibernate.annotations.NaturalId
import org.javers.core.metamodel.annotation.DiffIgnore
import org.javers.core.metamodel.annotation.ShallowReference
import org.javers.core.metamodel.annotation.TypeName
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@TypeName("role")
@Table(name = "role")
@RolePrefix(value = "role")
class TekRole(
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    @NaturalId
    @Column(length = 50, nullable = false)
    val name: String
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(mappedBy = "roles")
    @DiffIgnore
    @JsonIgnore
    var profiles: MutableSet<TekProfile> = mutableSetOf()
}