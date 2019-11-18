package it.jbot.security.oauth.exception

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

/**
 * Custom Exception to provide a standard exception handling for OAuth2 protocol
 */
@JsonSerialize(using = JBotOAuthExceptionSerializer::class)
class JBotOAuthException : OAuth2Exception {
    
    constructor(message: String?) : super(message)
}


