package com.hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // com.hello.aop.order 패키지 하위 모든 메서드
    @Pointcut("execution(* com.hello.aop.order..*(..))")    // Pointcut Expression
    public void allOrder() {}  // Pointcut Signature

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}

    // 포인트컷 시그니처끼리 조합한 포인트컷 시그니처도 가능
    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}

}
