package it.jbot.security.oauth

/**Class sharing constants for _OAuth2_ Spring Security configuration*/
object OAuthConstant {
    
    /**
     * Matching oauth_client_details.client_id to authorize a _jbot client_
     */
    const val DEFAULT_CLIENT_ID = "jBotClientId"
    
    /**
     * Matching oauth_client_details.client_secret to authorize access to a resource for a _jbot client_ with given _secret_
     */
    const val DEFAULT_CLIENT_SECRET = "jBotSecret"
    
    /**
     * Matching oauth_client_details.resource_ids to authorize access to a resource for a _jbot client_
     */
    const val DEFAULT_RESOURCE_ID = "jBotResourceId"
    
    /**
     * Matching oauth_client_details.authorized_grant_types
     */
    const val DEFAULT_GRANTS = "password,refresh_token,client_credentials"
    
    /**
     * Matching oauth_client_details.scope
     */
    const val DEFAULT_SCOPES = "read,write"
    
    /**
     * Matching oauth_client_details.scope to authorize a client request for resource with _read_ scope
     */
    const val SECURED_READER_SCOPE = "#oauth2.hasScope('read')"
    
    /**
     * Matching oauth_client_details.scope to authorize a client request for resource with _write_ scope
     */
    const val SECURED_WRITER_SCOPE = "#oauth2.hasScope('write')"
    
}