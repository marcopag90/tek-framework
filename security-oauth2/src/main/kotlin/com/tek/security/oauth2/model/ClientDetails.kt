package com.tek.security.oauth2.model

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
    @field:Size(max = 256)
    @Column(name = "client_id")
    var clientId: String? = null
    
    @field:Size(max = 256)
    @Column(name = "resource_ids")
    var resourceId: String? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = "client_secret", nullable = false)
    var clientSecret: String? = null
    
    @field:Size(max = 256)
    var scope: String? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = "authorized_grant_types", nullable = false)
    var authorizedGrantTypes: String? = null

    @field:Size(max = 256)
    @Column(name = "web_server_redirect_uri")
    var redirectUri: String? = null
    
    @field:NotBlank
    @field:Size(max = 256)
    @Column(name = " authorities", nullable = false)
    var authorities: String? = null
    
    @field:NotNull
    @Column(name = "access_token_validity", nullable = false)
    var accessTokenValidity: Int? = null
    
    @field:NotNull
    @Column(name = "refresh_token_validity", nullable = false)
    var refreshTokenValidity: Int? = null
    
    @field:Size(max = 4000)
    @Column(name = "additional_information")
    var additionalInfo: String? = null
    
    @field:Size(max = 256)
    var autoapprove: String? = null
}