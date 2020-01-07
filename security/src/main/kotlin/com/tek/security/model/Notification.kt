package com.tek.security.model

import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener::class)
class Notification(
    @field:NotBlank
    @Column(name = "message", nullable = false, length = 1024)
    var message: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    @DiffIgnore
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null

    @Column(name = "is_read", length = 1, nullable = false)
    var isRead: Boolean = false

    @CreatedBy
    @DiffIgnore
    @Column(name = "created_by")
    var createdBy: Long? = null
}