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
@Import({AtAnnotationTests.AtAnnotationAspect.class})
@SpringBootTest
public class AtAnnotationTests {

    @Autowired
    MemberService memberService;

    @Test
    void successTest() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class AtAnnotationAspect {

        @Around("@annotation(com.hello.aop.member.annotation.MethodAop)")
        public Object doAtAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[@annotation] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

    }

}
