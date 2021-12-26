package com.tek.core.aop;

import com.tek.core.properties.TekCoreProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Allows to decide if a mail has to be delivered or not.
 *
 * @author MarcoPagan
 */
@Aspect
@Component
@Slf4j
public record MailDeliveryAspect(TekCoreProperties properties) {

  @Around("@annotation(CanSendMail)")
  @SneakyThrows
  public Object executeAround(ProceedingJoinPoint joinPoint) {
    final var mailProperties = properties.getMailConfiguration();
    if (!mailProperties.isRealDelivery()) {
      log.warn("Skipping mail delivery since property tek.core.mail.realDelivery is false!");
      return null;
    }
    return joinPoint.proceed();
  }
}
