package com.tek.security.common.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.tek.audit.TimeActivityAudit
import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * Class extended by Entites to audit create/update time and user on Entity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    "created_at", "updated_at",
    "created_by", "updated_by",
    allowGetters = true
)
class UserActivityAudit : TimeActivityAudit() {

    @CreatedBy
    @DiffIgnore
    @Column(name = "created_by")
    var createdBy: Long? = null

    @LastModifiedBy
    @DiffIgnore
    @Column(name = "updated_by")
    var updatedBy: Long? = null
}