package it.jbot.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

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
