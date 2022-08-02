package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(2)   // Order는 Aspect 클래스 단위로 작동하므로 내부 클래스로 어드바이스를 분리해야 한다.
    public static class LogAspect {
        @Around("com.hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[log] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TransactionAspect {
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
}
