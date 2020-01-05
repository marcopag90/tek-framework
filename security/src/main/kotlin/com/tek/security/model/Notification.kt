package com.tek.security.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener::class)
class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @field:NotBlank
    @Column(name = "message")
    var message: String? = null

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null

    @Column(name = "read")
    var read: Boolean = false
}