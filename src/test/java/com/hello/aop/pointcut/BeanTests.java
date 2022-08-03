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
@Import({BeanTests.BeanAspect.class})
@SpringBootTest
public class BeanTests {

    @Autowired
    MemberService memberService;

    @Test
    void successTest() {
        log.info("memberService proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class BeanAspect {
        @Around("bean(*Service*)")  // bean의 이름이 확정적일 때 사용한다.
        public Object doBean(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[bean] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }
    }

}
