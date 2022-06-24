package com.tek.core.aop;

import com.tek.core.properties.TekCoreProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Allows deciding if a mail has to be delivered or not.
 *
 * @author MarcoPagan
 */
@Aspect
@Component
public record MailDeliveryAspect(TekCoreProperties properties) {

  private static final Logger log = LoggerFactory.getLogger(MailDeliveryAspect.class);

  @Around("@annotation(CanSendMail)")
  public Object executeAround(ProceedingJoinPoint joinPoint) throws Throwable {
    final var mailProperties = properties.getMailConfiguration();
    if (!mailProperties.isRealDelivery()) {
      log.warn("Skipping mail delivery since property tek.core.mail.realDelivery is false!");
      return null;
    }
    return joinPoint.proceed();
  }
}
