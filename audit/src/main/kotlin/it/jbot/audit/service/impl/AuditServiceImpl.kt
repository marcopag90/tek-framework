package it.jbot.audit.service.impl

import it.jbot.audit.model.WebAudit
import it.jbot.audit.repository.WebAuditRepository
import it.jbot.audit.service.AuditService
import it.jbot.shared.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Auditing Service implementation to log all Web requests and responses
 * @author PaganM
 *
 */
@Service
class AuditServiceImpl @Autowired constructor(val webAuditRepository: WebAuditRepository) : AuditService {
    
    private val logger by LoggerDelegate()
    
    //TODO maybe a well-formatted JSON to log request and response?
    override fun logRequest(httpServletRequest: HttpServletRequest, body: Any?) {
        
        var sb: StringBuilder = StringBuilder()
        var parameters: Map<String, String> = createRequestParameterMap(httpServletRequest)
        
        sb.append("REQUEST ")
        sb.append("method=[").append(httpServletRequest.method).append("] ")
        sb.append("path=[").append(httpServletRequest.requestURI).append("] ")
        sb.append("headers=[").append(createRequestHeaderMap(httpServletRequest)).append("] ")
        
        if (parameters.isNotEmpty())
            sb.append("parameters=[").append(parameters).append("] ")
        
        if (body != null)
            sb.append("body=[$body]")
        
        var requestString = sb.toString()
        logger.info(requestString)
        var webAudit =
            WebAudit(request = requestString, initTime = System.nanoTime(), initTimeMillis = System.currentTimeMillis())
        webAuditRepository.save(webAudit)
        httpServletRequest.setAttribute("audit", webAudit)
    }
    
    override fun logResponse(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        body: Any?
    ) {
        
        var sb: StringBuilder = StringBuilder()
        
        sb.append("RESPONSE ")
        sb.append("method=[").append(httpServletRequest.method).append("] ")
        sb.append("path=[").append(httpServletRequest.requestURI).append("] ")
        sb.append("responseHeaders=[").append(createResponseParameterMap(httpServletResponse)).append("] ")
        
        if (body != null)
            sb.append("responseBody=[").append(body).append("] ")
        
        var responseString = sb.toString()
        logger.info(responseString)
        if (httpServletRequest.getAttribute("audit") != null) {
            var webAudit = httpServletRequest.getAttribute("audit") as WebAudit
            webAudit.response = responseString
            webAudit.stats = collectStats(webAudit.initTime, webAudit.initTimeMillis)
            webAuditRepository.saveAndFlush(webAudit)
        }
    }
    
    private fun createRequestParameterMap(httpServletRequest: HttpServletRequest): Map<String, String> {
        
        var resultMap = hashMapOf<String, String>()
        var parameterElements = httpServletRequest.parameterNames
        
        while (parameterElements.hasMoreElements()) {
            
            var key = parameterElements.nextElement()
            resultMap[key] = httpServletRequest.getParameter(key)
        }
        
        return resultMap
    }
    
    private fun createRequestHeaderMap(httpServletRequest: HttpServletRequest): Map<String, String> {
        
        var resultMap = hashMapOf<String, String>()
        var headerElements = httpServletRequest.headerNames
        
        while (headerElements.hasMoreElements()) {
            
            var key = headerElements.nextElement()
            resultMap[key] = httpServletRequest.getHeader(key)
        }
        
        return resultMap
    }
    
    private fun createResponseParameterMap(httpServletResponse: HttpServletResponse): Map<String, String> {
        
        var resultMap = hashMapOf<String, String>()
        
        for (element in httpServletResponse.headerNames)
            resultMap[element] = httpServletResponse.getHeader(element)
        
        return resultMap
    }
    
    private fun collectStats(initTime: Long, initTimeMillis: Long): String {
        
        var f = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        var sb: StringBuilder = StringBuilder()
        
        sb.append("Requested At [")
        sb.append(f.format(Date(initTimeMillis)))
        sb.append("] ")
        
        sb.append("/ Served At [")
        sb.append(f.format(Timestamp(System.currentTimeMillis())))
        sb.append("] ")
        
        sb.append("/ Elapsed Time [")
        sb.append((System.nanoTime() - initTime) / 1000000L).toString()
        sb.append(" ms")
        sb.append("] ")
        
        return sb.toString()
    }
}