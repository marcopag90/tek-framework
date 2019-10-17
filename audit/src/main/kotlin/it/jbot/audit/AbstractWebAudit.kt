package it.jbot.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["requestedAt", "servedAt"], allowGetters = true)
abstract class AbstractWebAudit {

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    var requestedAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    var servedAt: Date? = null
}