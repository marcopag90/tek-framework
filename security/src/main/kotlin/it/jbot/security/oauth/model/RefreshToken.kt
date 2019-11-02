package it.jbot.security.oauth.model

import org.hibernate.FetchMode.LAZY
import org.hibernate.annotations.Type
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_refresh_token")
class RefreshToken {
    
    @Id
    @Size(max = 255)
    @Column(name = "token_id")
    var tokenId: String? = null
    
    @Lob
    @Column(name = "token", columnDefinition = "bytea not null")
    var token: ByteArray? = null
    
    @Lob
    @Column(name = "authentication", columnDefinition = "bytea not null")
    var authentication: ByteArray? = null
}