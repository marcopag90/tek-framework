package it.jbot.security.oauth.exception

import com.fasterxml.jackson.databind.ObjectMapper
import it.jbot.security.configuration.ServletRequestHolder
import it.jbot.shared.JBotErrorResponse
import it.jbot.shared.util.JBotDateUtils.jbotTimestamp
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JBotOAuth2AccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
    
        var mapper = hashMapOf<String, Any?>()
        mapper[JBotErrorResponse::errors.name] = accessDeniedException?.message
        mapper["timestamp"] = jbotTimestamp()
        mapper["path"] = request.servletPath
        
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(objectMapper.writeValueAsString(mapper))
    }
    
}