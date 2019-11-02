package it.jbot.security.oauth.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_access_token")
class AccessToken {
    
    @Id
    @Size(max = 255)
    @Column(name = "authentication_id")
    var authId: String? = null
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "token_id", nullable = false)
    var tokenId: String? = null
    
    @Lob
    @Column(name = "token", columnDefinition = "bytea not null")
    var token: ByteArray? = null
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "user_name", nullable = false)
    var username: String? = null
    
    @NotBlank
    @Size(max = 255)
    @Column(name = " client_id", nullable = false)
    var clientId: String? = null
    
    @Lob
    @Column(name = "authentication", columnDefinition = "bytea not null")
    var authentication: ByteArray? = null
    
    @Size(max = 255)
    @Column(name = "refresh_token", nullable = false)
    var refreshToken: String? = null
}