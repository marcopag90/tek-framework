package com.tek.security.oauth.model

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_refresh_token")
class RefreshToken {
    
    @Id
    @field:Size(max = 255)
    @Column(name = "token_id")
    var tokenId: String? = null
    
    @Lob
    @Column(name = "token", columnDefinition = "bytea not null")
    var token: ByteArray? = null
    
    @Lob
    @Column(name = "authentication", columnDefinition = "bytea not null")
    var authentication: ByteArray? = null
}