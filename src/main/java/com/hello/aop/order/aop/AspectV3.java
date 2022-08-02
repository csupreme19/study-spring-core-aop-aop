package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    // com.hello.aop.order 패키지 하위 모든 메서드
    @Pointcut("execution(* com.hello.aop.order..*(..))")    // Pointcut Expression
    private void allOrder() {}  // Pointcut Signature

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {}

    // 포인트컷 시그니처를 통한 포인트컷 분리
    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("[log] {}", proceedingJoinPoint.getSignature());
        return proceedingJoinPoint.proceed();
    }

    // com.hello.aop.order 패키지 하위의 클래스 이름 패턴이 *Service
    @Around("allOrder() && allService()")
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
