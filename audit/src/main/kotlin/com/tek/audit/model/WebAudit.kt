package com.tek.audit.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    "requested_at", "served_at",
    allowGetters = true
)
@Table(name = "web_audit")
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
    @Column(name = "requested_at")
    var requestedAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "served_at")
    var servedAt: Date? = null
}




