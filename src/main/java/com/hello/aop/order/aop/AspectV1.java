package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    // Spring AOP는 AspectJ의 포인트컷 문법을 차용한 것이고 실제로 AspectJ의 기능을 직접 사용하는 것은 아니다.
    // AspectJ의 애노테이션과 인터페이스만 사용하는 것으로 실제 Spring AOP는 프록시 방식의 AOP를 사용한다.
    @Around("execution(* com.hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("[log] {}", proceedingJoinPoint.getSignature());
        return proceedingJoinPoint.proceed();
    }
}
