package it.jbot.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import it.jbot.audit.service.AuditService
import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.stereotype.Component
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

/**
 * Class extended by Entities to audit create/update time on Entity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    "created_at", "updated_at",
    allowGetters = true
)
class TimeActivityAudit : Serializable {

    @CreatedDate
    @DiffIgnore
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @DiffIgnore
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null

}
