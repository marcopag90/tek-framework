package com.tek.audit.service

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuditService {

    fun logRequest(httpServletRequest: HttpServletRequest, body: Any?)

    fun logResponse(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, body: Any?)
}