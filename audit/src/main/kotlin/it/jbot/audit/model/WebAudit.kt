package it.jbot.audit.model

import it.jbot.audit.shared.TimeActivityAudit
import javax.persistence.*

@Entity
@Table(name = "webaudit")
class WebAudit(
    
    @Id @GeneratedValue
    var id: Long? = null,
    
    @Column(columnDefinition = "clob not null")
    @Lob
    val request: String,
    
    @Lob
    var response: String? = null,
    
    var stats: String? = null,
    
    @Transient
    val initTime: Long,
    
    @Transient
    val initTimeMillis: Long

) : TimeActivityAudit()