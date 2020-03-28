package com.tek.core.configuration

import com.tek.core.TekCoreProperties
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
/**
 * CORS Policy
 * 1) _Access-Control-Allow-Origin_ can't accept a wildcard <*> if the requesting client sends _withCredentials header_: true
 * 2) _Access-Control-Allow-Origin_ can't send multiple origins (except using the wildcard <*>)
 * 3) _Access-Control-Allow-Headers_ OPTIONS must return [HttpServletResponse.SC_OK] to avoid 401 status code with authorization header
 * 4) _Access-Control-Allow-Credentials_ must be true if the requesting client sends _withCredentials header_: true
 */
class TekCorsFilter(
    coreProperties: TekCoreProperties
) : Filter {

    private val corsProperties = coreProperties.cors

    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        response.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsProperties.allowedOrigin
        )
        response.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, corsProperties.allowedCredentials
        )
        response.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, corsProperties.allowedMethods.joinToString()
        )
        response.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, corsProperties.allowedHeaders.joinToString()
        )

        if (RequestMethod.OPTIONS.name == request.method) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }
    }
}