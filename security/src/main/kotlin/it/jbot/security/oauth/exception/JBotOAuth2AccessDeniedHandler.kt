package it.jbot.security.oauth.exception

import com.fasterxml.jackson.databind.ObjectMapper
import it.jbot.shared.web.JBotErrorResponse
import it.jbot.shared.util.JBotDateUtils.jbotTimestamp
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Component providing a custom message handling for [it.jbot.security.oauth.configuration.JBotOAuthResourceServer]
 */
@Component
class JBotOAuth2AccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpStatus.UNAUTHORIZED.value()
        
        response.writer.write(
            objectMapper.writeValueAsString(
                hashMapOf<String, Any?>(
                    JBotErrorResponse::errors.name to accessDeniedException?.message,
                    "timestamp" to jbotTimestamp(),
                    "status" to HttpStatus.UNAUTHORIZED.value(),
                    "path" to request.servletPath
                )
            )
        )
    }
    
}