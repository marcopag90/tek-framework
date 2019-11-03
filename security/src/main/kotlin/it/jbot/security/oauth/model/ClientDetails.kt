package it.jbot.security.oauth.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_client_details")
class ClientDetails {
    
    @Id
    @Size(max = 256)
    @Column(name = "client_id")
    var clientId: String? = null
    
    @Size(max = 256)
    @Column(name = "resource_ids")
    var resourceId: String? = null
    
    @NotBlank
    @Size(max = 256)
    @Column(name = "client_secret", nullable = false)
    var clientSecret: String? = null
    
    @Size(max = 256)
    var scope: String? = null
    
    @NotBlank
    @Size(max = 256)
    @Column(name = "authorized_grant_types", nullable = false)
    var authorizedGrantTypes: String? = null
    
    @Column(name = "web_server_redirect_uri")
    @Size(max = 256)
    var redirectUri: String? = null
    
    @NotBlank
    @Size(max = 256)
    @Column(name = " authorities", nullable = false)
    var authorities: String? = null
    
    @NotNull
    @Column(name = "access_token_validity", nullable = false)
    var accessTokenValidity: Int? = null
    
    @NotNull
    @Column(name = "refresh_token_validity", nullable = false)
    var refreshTokenValidity: Int? = null
    
    @Size(max = 4000)
    @Column(name = "additional_information")
    var additionalInfo: String? = null
    
    @Size(max = 256)
    var autoapprove: String? = null
}