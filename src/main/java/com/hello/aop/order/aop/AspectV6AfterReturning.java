package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV6AfterReturning {

    @Aspect
    @Order(1)
    public static class AfterReturningAspect {
        @AfterReturning(value = "com.hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
        public void doTransactionAfterReturning(JoinPoint joinPoint, String result) {   // 실제 리턴 객체의 타입과 일치해야 작동한다.
            result = "YOUR DATA IS ENCRYPTED";
            log.info("[트랜잭션 종료] {} return={}", joinPoint.getSignature(), result);
        }
    }

    @Aspect
    @Order(2)
    public static class AfterReturningElseAspect {

//        @Before("com.hello.aop.order.aop.Pointcuts.allOrder()")
//        public void doTransactionBefore(JoinPoint joinPoint) {
//            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
//        }
//
//        @AfterThrowing(value = "com.hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "exception")
//        public void doTransactionAfterThrowing(JoinPoint joinPoint, Throwable exception) {
//            log.info("[트랜잭션 롤백] {} thrown={}", joinPoint.getSignature(), exception);
//        }
//
//        @After("com.hello.aop.order.aop.Pointcuts.orderAndService()")
//        public void doTransactionAfter(JoinPoint joinPoint) {
//            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
//        }

        @Around("com.hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            Object result = null;
            try {
                // @Before
                log.info("[트랜잭션 시작] {}", proceedingJoinPoint.getSignature());
                result = proceedingJoinPoint.proceed();
                // @AfterReturning
                log.info("[트랜잭션 종료] {}, return={}", proceedingJoinPoint.getSignature(), result);
            } catch (Exception e) {
                // @AfterThrowing
                log.info("[트랜잭션 롤백] {}", proceedingJoinPoint.getSignature());
            } finally {
                // @After
                log.info("[리소스 릴리즈] {}", proceedingJoinPoint.getSignature());
            }
            return result;
        }
    }
}
