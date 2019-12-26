package it.jbot.security.oauth.exception

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import it.jbot.security.oauth.configuration.OAuthExceptionSerializer
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

/**
 * Custom Exception to provide a standard exception handling for OAuth2 protocol
 */
@JsonSerialize(using = OAuthExceptionSerializer::class)
class JBotOAuth2Exception : OAuth2Exception {
    
    constructor(message: String?) : super(message)
}



