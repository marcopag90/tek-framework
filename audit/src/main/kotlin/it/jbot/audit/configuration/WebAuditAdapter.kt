package it.jbot.audit.configuration

import it.jbot.audit.service.AuditService
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.lang.reflect.Type
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class AuditResponseBodyAdviceAdapter(private val auditService: AuditService) :
    ResponseBodyAdvice<Any> {
    
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return true
    }
    
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        
        if (request is ServletServerHttpRequest && response is ServletServerHttpResponse)
            auditService.logResponse(
                request.servletRequest,
                response.servletResponse,
                body
            )
        
        return body
    }
}

@ControllerAdvice
class AuditRequestBodyAdviceAdapter(
    private val auditService: AuditService,
    private val httpServletRequest: HttpServletRequest
) : RequestBodyAdviceAdapter() {
    
    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        
        return true
    }
    
    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any {
        
        auditService.logRequest(httpServletRequest, body)
        
        return super.afterBodyRead(
            body,
            inputMessage,
            parameter,
            targetType,
            converterType
        )
    }
    
    
}