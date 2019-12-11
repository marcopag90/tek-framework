package it.jbot.security.web

import it.jbot.core.SpringProfile
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Profile(SpringProfile.DEVELOPMENT)
@Order(Ordered.HIGHEST_PRECEDENCE)
class JBotCorsFilter : Filter {

    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "PATCH,POST,GET,OPTIONS,DELETE")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN"
        )

        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }
    }
}