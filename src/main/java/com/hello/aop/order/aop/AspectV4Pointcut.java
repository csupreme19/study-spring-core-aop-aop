package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV4Pointcut {

    // 포인트컷 시그니처를 통한 포인트컷 분리
    @Around("com.hello.aop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("[log] {}", proceedingJoinPoint.getSignature());
        return proceedingJoinPoint.proceed();
    }

    // 포인트컷을 외부 클래스에서 가져올 수 있음(public)
    @Around("com.hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        try {
            log.info("[트랜잭션 시작] {}", proceedingJoinPoint.getSignature());
            result = proceedingJoinPoint.proceed();
            log.info("[트랜잭션 종료] {}", proceedingJoinPoint.getSignature());
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}", proceedingJoinPoint.getSignature());
        } finally {
            log.info("[리소스 릴리즈] {}", proceedingJoinPoint.getSignature());
        }
        return result;
    }
}
