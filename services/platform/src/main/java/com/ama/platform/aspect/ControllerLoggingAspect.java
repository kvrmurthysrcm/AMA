package com.ama.platform.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Value("${ama.logging.controller-debug-enabled:true}")
    private boolean controllerDebugEnabled;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!controllerDebugEnabled || !log.isDebugEnabled()) {
            return joinPoint.proceed();
        }
        log.debug("Entering controller method={} args={}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        log.debug("Exiting controller method={} result={}", joinPoint.getSignature().toShortString(), result);
        return result;
    }
}
