package com.tek.security.oauth.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.tek.core.TekErrorResponse
import com.tek.core.util.tekTimestamp
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Component providing a custom message handling for [com.tek.security.oauth.configuration.OAuthResourceServer]
 */
@Component
class OAuth2AccessDeniedHandler(
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
                hashMapOf(
                    TekErrorResponse::errors.name to accessDeniedException?.message,
                    "timestamp" to Date().tekTimestamp(),
                    "status" to HttpStatus.UNAUTHORIZED.value(),
                    "path" to request.servletPath
                )
            )
        )
    }

}