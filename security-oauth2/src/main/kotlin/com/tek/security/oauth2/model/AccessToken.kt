package com.tek.security.oauth2.model

import org.hibernate.annotations.Type
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_access_token")
class AccessToken {
    
    @Id
    @field:Size(max = 256)
    @Column(name = "authentication_id")
    var authId: String? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = "token_id", nullable = false)
    var tokenId: String? = null

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "token")
    var token: ByteArray? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = "user_name", nullable = false)
    var username: String? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = " client_id", nullable = false)
    var clientId: String? = null

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "authentication")
    var authentication: ByteArray? = null
    
    @field:Size(max = 255)
    @Column(name = "refresh_token", nullable = false)
    var refreshToken: String? = null
}