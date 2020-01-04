package com.tek.core.configuration

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
/**
 * CORS Policy
 * 1) _Access-Control-Allow-Origin_ can't accept wildcard * if the requesting client sends _withCredentials header_: true
 * 2) _Access-Control-Allow-Origin_ can't send multiple origins (except using the widlcard *)
 * 3) _Access-Control-Allow-Headers_ OPTIONS must return [HttpServletResponse.SC_OK] to avoid 401 status code with authorization header
 * 4) _Access-Control-Allow-Credentials_ must be true if the requesting client sends _withCredentials header_: true
 */
class TekCorsFilter(
    private val corsProperties: TekCorsProperties
) : Filter {

    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin", corsProperties.allowedOrigin)
        response.setHeader("Access-Control-Allow-Credentials", corsProperties.allowedCredentials)
        response.setHeader("Access-Control-Allow-Methods", corsProperties.allowedMethods.joinToString())
        response.setHeader("Access-Control-Allow-Headers", corsProperties.allowedHeaders.joinToString())

        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }
    }
}