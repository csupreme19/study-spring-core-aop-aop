package com.hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    /*
    Order 미지정시 기본 실행 순서
    1. @Around
    2. @Before
    3. @After
    4. @AfterReturning
    5. @AfterThrowing
    반환 순서는 실행 순서와 반대
     */

    @Before("com.hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doTransactionBefore(JoinPoint joinPoint) {
        log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "com.hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
    public void doTransactionAfterReturning(JoinPoint joinPoint, String result) {   // 실제 리턴 객체의 타입과 일치해야 작동한다.
        result = "YOUR DATA IS ENCRYPTED";
        log.info("[트랜잭션 종료] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "com.hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "exception")
    public void doTransactionAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.info("[트랜잭션 롤백] {} thrown={}", joinPoint.getSignature(), exception);
    }

    @After("com.hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doTransactionAfter(JoinPoint joinPoint) {
        log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
    }

    /*
    Around를 사용하면 호출 대상의 흐름을 제어할 수 있다. 또한 대부분의 기능을 사용할 수 있다.
    Around라는 상위 Advice가 존재하는데 왜 @Before와 같은 어드바이스가 따로 존재하는가?
    1. 개발자가 Around 사용시 ProceedingJoinPoint.proceed() 호출을 깜빡할 수 있다.
    좋은 설계는 제약이 존재하는 것으로 실수를 방지할 수 있음
     */
//    @Around("com.hello.aop.order.aop.Pointcuts.orderAndService()")
//    public Object doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        Object result = null;
//        try {
//            // @Before
//            log.info("[트랜잭션 시작] {}", proceedingJoinPoint.getSignature());
//            result = proceedingJoinPoint.proceed();
//            // @AfterReturning
//            log.info("[트랜잭션 종료] {}", proceedingJoinPoint.getSignature());
//        } catch (Exception e) {
//            // @AfterThrowing
//            log.info("[트랜잭션 롤백] {}", proceedingJoinPoint.getSignature());
//        } finally {
//            // @After
//            log.info("[리소스 릴리즈] {}", proceedingJoinPoint.getSignature());
//        }
//        return result;
//    }
}
