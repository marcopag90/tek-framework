package com.tek.security.oauth2.model

import org.hibernate.annotations.Type
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
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "token")
    var token: ByteArray? = null

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "authentication")
    var authentication: ByteArray? = null
}