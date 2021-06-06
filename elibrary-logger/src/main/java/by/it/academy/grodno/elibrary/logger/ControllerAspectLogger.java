package by.it.academy.grodno.elibrary.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class ControllerAspectLogger {

    private static final Logger log = LoggerFactory.getLogger(ControllerAspectLogger.class);

    @Pointcut("execution(public * by.it.academy.grodno.elibrary.rest.controllers.*.*(..))")
    public void callAtRestControllerPublicMethod() {
        // do nothing because it for simple point cuts
    }

    @Around("callAtRestControllerPublicMethod()")
    public Object aroundCallAt(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch(call.toString());
        Object methodResult = null;
        try {
            clock.start(call.toShortString());
            methodResult = call.proceed();
            return call.proceed();
        } finally {
            clock.stop();
            log.info(getFormattedString(call, methodResult, clock.getTotalTimeMillis()));
        }
    }

    private static final String FORMAT_PATTERN_METHOD_PERFORMANCE = "around %s.%s(), args=[%s], return=[%s], startup in %d millis.}";

    private String getFormattedString(ProceedingJoinPoint call, Object methodResult, long millis) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ((MethodSignature) call.getSignature()).getMethod();
        return String.format(FORMAT_PATTERN_METHOD_PERFORMANCE,
                method.getDeclaringClass().getName(),
                method.getName(),
                Arrays.stream(call.getArgs()).map(Object::toString).collect(Collectors.joining(",")),
                methodResult,
                millis);
    }
}
