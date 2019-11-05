package it.jbot.audit.service

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuditService {
    
    fun logRequest(httpServletRequest: HttpServletRequest, body: Any?)
    
    fun logResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        body: Any?
    )
}