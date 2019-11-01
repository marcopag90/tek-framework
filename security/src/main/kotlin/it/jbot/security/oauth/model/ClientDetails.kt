package it.jbot.security.oauth.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "oauth_client_details")
class ClientDetails {
    
    @Id
    @Column(name = "client_id")
    @Size(max = 256)
    var clientId: String? = null
    
    @Column(name = "resource_ids")
    @Size(max = 256)
    var resourceIds: String? = null
    
    @Column(name = "client_secret")
    @NotBlank
    @Size(max = 256)
    var clientSecret: String? = null
    
    @Size(max = 256)
    var scope: String? = null
    
    @Column(name = "authorized_grant_types")
    @Size(max = 256)
    var authorizedGrantTypes: String? = null
    
    @Column(name = "web_server_redirect_uri")
    @Size(max = 256)
    var redirectUri: String? = null
    
    @Size(max = 256)
    var authorities: String? = null
    
    @Column(name = "access_token_validity")
    var accessTokenValidity: Int? = null
    
    @Column(name = "refresh_token_validity")
    var refreshTokenValidity: Int? = null
    
    @Size(max = 4000)
    @Column(name = "additional_information")
    var additionalInfo: String? = null
    
    @Size(max = 256)
    var autoapprove: String? = null
}