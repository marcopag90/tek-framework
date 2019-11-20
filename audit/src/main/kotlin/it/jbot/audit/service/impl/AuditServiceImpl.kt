package it.jbot.audit.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import it.jbot.audit.model.WebAudit
import it.jbot.audit.repository.WebAuditRepository
import it.jbot.audit.service.AuditService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
class AuditServiceImpl(
    private val webAuditRepository: WebAuditRepository,
    private val objectMapper: ObjectMapper
) : AuditService {

    private val logger: Logger = LoggerFactory.getLogger(AuditServiceImpl::class.java)

    override fun logRequest(
        httpServletRequest: HttpServletRequest,
        body: Any?
    ) {

        val sb: StringBuilder = StringBuilder()
        val jsonRequestMap = hashMapOf<String, Any?>()
        val parameters: Map<String, String> = createRequestParameterMap(httpServletRequest, jsonRequestMap)

        sb.append("REQUEST ")
        sb.append("method=[").append(httpServletRequest.method).append("] ")
        sb.append("path=[").append(httpServletRequest.requestURI).append("] ")
        sb.append("headers=[").append(createRequestHeaderMap(httpServletRequest, jsonRequestMap)).append("] ")
        if (parameters.isNotEmpty())
            sb.append("parameters=[").append(parameters).append("] ")

        body?.let {
            sb.append("body=[$it]")
            jsonRequestMap["body"] = it
        }

        logger.info(sb.toString())

        httpServletRequest.setAttribute("audit",  WebAudit(
            request = objectMapper.writeValueAsString(jsonRequestMap),
            initTime = System.nanoTime(),
            initTimeMillis = System.currentTimeMillis()
        ).apply { webAuditRepository.save(this) })
    }

    override fun logResponse(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        body: Any?
    ) {

        val sb: StringBuilder = StringBuilder()
        val jsonResponseMap = hashMapOf<String, Any?>()

        sb.append("RESPONSE ")
        sb.append("method=[").append(httpServletRequest.method).append("] ")
        sb.append("path=[").append(httpServletRequest.requestURI).append("] ")
        sb.append("responseHeaders=[").append(createResponseParameterMap(httpServletResponse, jsonResponseMap))
        sb.append("] ")

        body?.let {
            sb.append("responseBody=[").append(it).append("] ")
            jsonResponseMap["body"] = it
        }

        logger.info(sb.toString())

        httpServletRequest.getAttribute("audit")?.let {
            (it as WebAudit).apply {
                this.response = objectMapper.writeValueAsString(jsonResponseMap)
                this.stats = collectStats(this.initTime, this.initTimeMillis)
            }.run {
                webAuditRepository.saveAndFlush(this)
            }
        }
    }

    private fun createRequestParameterMap(
        httpServletRequest: HttpServletRequest,
        jsonRequestMap: HashMap<String, Any?>
    ): HashMap<String, String> {

        val resultMap = hashMapOf<String, String>()
        val parameterElements = httpServletRequest.parameterNames

        while (parameterElements.hasMoreElements()) {

            val key = parameterElements.nextElement()
            val param = httpServletRequest.getParameter(key)

            resultMap[key] = param
            if (!jsonRequestMap.containsKey(key))
                jsonRequestMap[key] = param
        }
        return resultMap
    }

    private fun createRequestHeaderMap(
        httpServletRequest: HttpServletRequest,
        jsonRequestMap: HashMap<String, Any?>
    ): HashMap<String, String> {

        val resultMap = hashMapOf<String, String>()
        val headerElements = httpServletRequest.headerNames

        while (headerElements.hasMoreElements()) {

            val key = headerElements.nextElement()
            val header = httpServletRequest.getHeader(key)

            resultMap[key] = header
            if (!jsonRequestMap.containsKey(key))
                jsonRequestMap[key] = header
        }
        return resultMap
    }

    private fun createResponseParameterMap(
        httpServletResponse: HttpServletResponse,
        jsonResponseMap: HashMap<String, Any?>
    ): Map<String, String> {

        val resultMap = hashMapOf<String, String>()

        for (element in httpServletResponse.headerNames) {

            val param = httpServletResponse.getHeader(element)
            resultMap[element] = param

            if (!jsonResponseMap.containsKey(element))
                jsonResponseMap[element] = param
        }
        return resultMap
    }

    private fun collectStats(initTime: Long, initTimeMillis: Long): String {

        val f = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val sb: StringBuilder = StringBuilder()

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