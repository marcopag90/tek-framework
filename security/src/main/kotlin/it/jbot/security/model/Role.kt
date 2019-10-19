package it.jbot.security.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * Persistable Roles to be assigned to User
 */
@Entity
@Table(name = "roles")
data class Role(var roleName: String) {
    
    @Id
    @GeneratedValue
    var id: Long? = null
}