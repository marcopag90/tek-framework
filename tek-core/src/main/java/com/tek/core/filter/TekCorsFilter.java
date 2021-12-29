package com.tek.core.filter;

import com.tek.core.properties.TekCoreProperties;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Cors Filter Policy
 * <p>
 * 1) Access-Control-Allow-Origin can't accept a wildcard <*> if the requesting client sends
 * <i>withCredentials</i> header: true
 * <p>
 * 2) Access-Control-Allow-Origin can't send multiple origins (except using the wildcard <*>)
 * <p>
 * 3) Access-Control-Allow-Headers OPTIONS must return [HttpServletResponse.SC_OK] to avoid 401
 * status code with authorization header
 * <p>
 * 4) Access-Control-Allow-Credentials must be true if the requesting client sends
 * <i>withCredentials</i> header: true
 *
 * @author MarcoPagan
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public record TekCorsFilter(@NonNull TekCoreProperties coreProperties) implements Filter {

  @Override
  @SneakyThrows
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    final var request = (HttpServletRequest) req;
    final var response = (HttpServletResponse) res;
    final var corsProperties = coreProperties.getCorsConfiguration();

    response.setHeader(
        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsProperties.getAllowedOrigin()
    );
    response.setHeader(
        HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
        corsProperties.getAllowedCredentials().toString()
    );
    response.setHeader(
        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
        String.join(",", corsProperties.getAllowedMethods())
    );
    response.setHeader(
        HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
        String.join(",", corsProperties.getAllowedHeaders())
    );

    if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
    }
  }
}
