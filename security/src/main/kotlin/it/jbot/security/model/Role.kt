package it.jbot.security.model

import it.jbot.security.model.enums.RoleName
import it.jbot.shared.LabelEnum
import org.hibernate.annotations.NaturalId
import javax.persistence.*

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@Table(name = "role")
data class Role(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 10) val name: RoleName
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

