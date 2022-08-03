package com.hello.aop.pointcut;

import com.hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetWithinTest.Config.class})
@SpringBootTest
public class AtTargetWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child proxy={}", child.getClass());
        child.childMethod();    // 부모, 자식 클래스 모두 존재하는 메서드
        child.parentMethod();   // 부모 클래스에 존재하는 메서드
    }

    static class Config {

        @Bean
        Child child() { return new Child(); }

        @Bean
        AtTargetWithinAspect atTargetWithinAspect() { return new AtTargetWithinAspect(); }

    }

    static class Parent {
        public void parentMethod() {}
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {}
    }

    @Slf4j
    @Aspect
    static class AtTargetWithinAspect {

        @Pointcut("execution(* com.hello.aop..*(..))")
        public void packageAopPointcut(){}

        @Pointcut("@target(com.hello.aop.member.annotation.ClassAop)")
        public void targetAnnotationClassAopPointcut(){}

        @Pointcut("@within(com.hello.aop.member.annotation.ClassAop)")
        public void withinAnnotationClassAopPointcut(){}

        /**
         * @target, @within, args는 객체 인스턴스가 생성되는 런타임에 포인트컷 매칭이 판단되기 때문에
         * 스프링은 실행 시점에 모든 등록된 빈에 대하여 프록시 생성을 시도함!
         * 따라서 위 지시자는 단독 사용하지 말고 execution과 같은 지시자랑 같이 사용할 것
         */

        // @target: 인스턴스 기준 모든 메서드에 매칭, 부모의 메서드도 모두 적용
        @Around("packageAopPointcut() && targetAnnotationClassAopPointcut()")
        public Object atTarget(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[@target] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

        // @within: 선택된 클래스의 메서드만 선택, 부모의 메서드는 미적용
        @Around("packageAopPointcut() && withinAnnotationClassAopPointcut()")
        public Object atWithin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            log.info("[@within] {}", proceedingJoinPoint.getSignature());
            return proceedingJoinPoint.proceed();
        }

    }
}