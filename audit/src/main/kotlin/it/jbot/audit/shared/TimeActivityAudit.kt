package it.jbot.audit.shared

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["requestedAt", "servedAt"], allowGetters = true)
abstract class TimeActivityAudit {
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    open var requestedAt: Date? = null
    
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    open var servedAt: Date? = null
}