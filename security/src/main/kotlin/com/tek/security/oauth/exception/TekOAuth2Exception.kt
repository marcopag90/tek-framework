package com.tek.security.oauth.exception

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.tek.security.oauth.configuration.OAuthExceptionSerializer
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

/**
 * Custom Exception to provide a standard exception handling for OAuth2 protocol
 */
@JsonSerialize(using = OAuthExceptionSerializer::class)
class TekOAuth2Exception(message: String?) : OAuth2Exception(message)





