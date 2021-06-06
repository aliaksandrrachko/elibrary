package by.it.academy.grodno.elibrary.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class ControllerAspectLogger {

    private static final Logger log = LoggerFactory.getLogger(ControllerAspectLogger.class);

    @Pointcut("execution(public * by.it.academy.grodno.elibrary.rest.controllers.AuthorRestController.*(..))")
    public void callAtRestControllerPublic() { }


    @Before("callAtRestControllerPublic()")
    public void logInvokeControllerMethod(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        log.info("before " + jp.toString() + ", args=[" + args + "]");
    }

    @After("callAtRestControllerPublic()")
    public void afterCallAt(JoinPoint jp) {
        log.info("after " + jp.toString());
    }

    @Around("callAtRestControllerPublic()")
    public Object aroundCallAt(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch(call.toString());
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            log.info(clock.prettyPrint());
        }
    }
}
