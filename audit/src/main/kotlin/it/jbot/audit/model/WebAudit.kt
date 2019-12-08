package it.jbot.audit.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.GeneratorType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    "requestedAt", "servedAt",
    allowGetters = true
)
class WebAudit(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(columnDefinition = "text")
    val request: String,

    @Column(columnDefinition = "text")
    var response: String? = null,

    var stats: String? = null,

    @Transient
    val initTime: Long,

    @Transient
    val initTimeMillis: Long
) {

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    var requestedAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    var servedAt: Date? = null
}




