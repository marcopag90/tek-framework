package it.jbot.audit.config

import it.jbot.audit.service.AuditService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.DispatcherType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Configuration to expose custom interceptors
 * @author PaganM
 */
@Configuration
class AuditWebConfig(private val auditInterceptor: AuditInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {

        registry.addInterceptor(auditInterceptor);
        super.addInterceptors(registry);
    }
}

/**
 * Component to intercept GET requests for auditing purpose
 * @author PaganM
 *
 */
@Component
class AuditInterceptor(private val auditService: AuditService) :
    HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        if (DispatcherType.REQUEST.name == request.dispatcherType.name && request.method == HttpMethod.GET.name)
            auditService.logRequest(request, null);

        return true;
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {}

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {}
}