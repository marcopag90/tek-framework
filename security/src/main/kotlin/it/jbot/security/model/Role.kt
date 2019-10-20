package it.jbot.security.model

import it.jbot.shared.LabelEnum
import org.hibernate.annotations.NaturalId
import javax.persistence.*

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@Table(name = "roles")
data class Role(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 10) val name: RoleName
) {

    @Id
    @GeneratedValue
    var id: Long? = null
}

enum class RoleName(override val label: String) : LabelEnum {

    ADMIN("Administrator"),
    USER("User")
}