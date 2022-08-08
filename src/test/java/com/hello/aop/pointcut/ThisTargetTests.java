package com.hello.aop.pointcut;

import com.hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = "spring.aop.proxy-target-class=true") // CGLIB Proxy 사용 설정
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false") // JDK Dynamic Proxy 사용 설정
@Import(ThisTargetTests.ThisTargetAspect.class)
public class ThisTargetTests {

    @Autowired
    MemberService memberService;

    @Test
    void successTest() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("classA");
    }

    @Aspect
    static class ThisTargetAspect {

        // 부모 타입 허용
        @Around("this(com.hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[this-interface] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

        // 부모 타입 허용
        @Around("target(com.hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[target-interface] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

        // this는 JDK Dynamic Proxy를 사용할때 interface 기준으로 프록시를 생성하므로 구현체에 대하여 알지 못함
        @Around("this(com.hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[this-implement] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

        // 부모 타입 허용
        @Around("target(com.hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[target-implement] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

    }

}
