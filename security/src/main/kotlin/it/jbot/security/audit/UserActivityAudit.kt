package it.jbot.security.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Class extended by Entites to audit create/update time and user on Entity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    "createdAt", "updatedAt",
    "createdBy", "updatedBy",
    allowGetters = true
)
class UserActivityAudit : Serializable {
    
    @CreatedDate
    @DiffIgnore
    var createdAt: LocalDateTime? = null
    
    @LastModifiedDate
    @DiffIgnore
    var updatedAt: LocalDateTime? = null
    
    @CreatedBy
    @DiffIgnore
    var createdBy: Long? = null
    
    @LastModifiedBy
    @DiffIgnore
    var updatedBy: Long? = null
}