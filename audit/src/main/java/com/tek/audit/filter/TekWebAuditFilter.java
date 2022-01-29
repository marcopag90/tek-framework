package com.tek.audit.filter;

import com.google.common.collect.ArrayListMultimap;
import com.tek.audit.TekAuditProperties;
import com.tek.audit.dto.ServletRequestDto;
import com.tek.audit.dto.ServletResponseDto;
import com.tek.audit.model.WebAudit;
import com.tek.audit.service.WebAuditService;
import com.tek.core.util.TekStreamEnumeration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * HTTP request persisted auditing.
 * <p>
 *
 * @author MarcoPagan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TekWebAuditFilter extends OncePerRequestFilter {

  @NonNull private final WebAuditService service;
  @NonNull private final TekAuditProperties auditProperties;

  private int payloadLength;

  @PostConstruct
  private void init() {
    this.payloadLength = auditProperties.getPayloadLength();
  }

  @SuppressWarnings("unused")
  @Value("${server.servlet.context-path:}")
  private String servletPath;

  //TODO check authType e userPrincipal
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    WebAudit webAudit = null;
    long startTime = System.currentTimeMillis();

    // Request Log
    ServletRequestDto servletRequestDto = ServletRequestDto.builder()
        .method(request.getMethod())                      // eg: GET
        .requestUrl(request.getRequestURL().toString())   // eg: http://localhost:8080/api/...
        .requestUri(request.getRequestURI())              // eg: /api/...
        .headers(getRequestHeaders(request))
        .parameters(getRequestParameters(request))
        .queryString(request.getQueryString())            // eg: sort=id,desc&username,desc)
        .authType(request.getAuthType())                  // eg: ?
        .principal(request.getUserPrincipal())            // eg: ?
        .build();

    log.debug("{}", servletRequestDto);

        /*
        Avoid to persist unuseful requests
         */
    if (!isSkippable(request)) {
      webAudit = service.logRequest(servletRequestDto);
    }

        /*
         We CANNOT simply read the request payload here, because then the InputStream
         would be consumed and cannot be read again by the actual processing/server.
         We have to wrap its content inside a Spring cache, so that we can proceed.
        */
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    // This performs the actual request
    filterChain.doFilter(wrappedRequest, wrappedResponse);

    long duration = System.currentTimeMillis() - startTime;

        /*
         I can only log the request's body AFTER the request has been made
         and ContentCachingRequestWrapper did its work.
         */
    String requestBody = getBodyContent(
        wrappedRequest.getContentAsByteArray(),
        this.payloadLength,
        request.getCharacterEncoding()
    );
    if (requestBody.length() > 0) {
      log.debug("Request body:\n{}", requestBody);
    }

    if (webAudit != null) {
      webAudit = service.updateRequest(webAudit.getId(), requestBody);
    }

    log.debug(
        "{}: returned status={} in {} ms", servletRequestDto, response.getStatus(), duration
    );

    String responseBody = getBodyContent(
        wrappedResponse.getContentAsByteArray(),
        this.payloadLength,
        response.getCharacterEncoding()
    );
    log.debug("Response body:\n {}", responseBody);

    if (webAudit != null) {
      ServletResponseDto servletResponseDto = ServletResponseDto.builder()
          .status(response.getStatus())
          .duration(duration)
          .build();
      webAudit = service.logResponse(webAudit.getId(), servletResponseDto);
      if (webAudit == null) {
        log.warn("Couldn't log response: {}", servletResponseDto);
      }
    }

    // copy content of response back into original response
    wrappedResponse.copyBodyToResponse();
  }

  /**
   * Method to skip all unuseful requests.
   * <p> Currently skips:
   * <ul>
   *     <li>Swagger webjars requests</li>
   *     <li>Servlet root path</li>
   *     <li>CSRF request</li>
   * </ul>
   */
  private boolean isSkippable(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    if (requestURI.equals(servletPath + "/")) {return true;}
    if (requestURI.equals(servletPath + "/error")) {return true;}
    if (requestURI.equals(servletPath + "/csrf")) {return true;}
    if (requestURI.contains("swagger")) {return true;}
    return requestURI.contains("/v2/api-docs");
  }

  /**
   * Creates a map containing all request parameters, allowing to store multiple occurrences of the
   * same element.
   */
  private ArrayListMultimap<String, String> getRequestParameters(HttpServletRequest request) {
    ArrayListMultimap<String, String> parameters = ArrayListMultimap.create();
    Enumeration<String> parameterNames = request.getParameterNames();
    TekStreamEnumeration.enumerationAsStream(parameterNames).forEach(
        p -> {
          String[] parameterValues = request.getParameterValues(p);
          for (String value : parameterValues) {
            parameters.put(p, value);
          }
        }
    );
    return parameters;
  }

  /**
   * Creates a map containing all request headers
   */
  private HashMap<String, String> getRequestHeaders(HttpServletRequest request) {
    HashMap<String, String> headers = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    TekStreamEnumeration.enumerationAsStream(headerNames)
        .forEach(h -> headers.put(h, request.getHeader(h)));
    return headers;
  }

  private String getBodyContent(byte[] buf, int maxLength, String charsetName) {
    if (buf == null || buf.length == 0) {return "";}
    int length = Math.min(buf.length, maxLength);
    try {
      return new String(buf, 0, length, charsetName);
    } catch (UnsupportedEncodingException ex) {
      log.warn("Unsupported Enconding");
      return "Unsupported Encoding";
    }
  }
}
