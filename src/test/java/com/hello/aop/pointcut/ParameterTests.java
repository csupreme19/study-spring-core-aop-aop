package com.hello.aop.pointcut;

import com.hello.aop.member.MemberService;
import com.hello.aop.member.annotation.ClassAop;
import com.hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({ParameterTests.ParameterAspect.class})
@SpringBootTest
public class ParameterTests {

    @Autowired
    MemberService memberService;

    @Test
    void successTest() {
        log.info("memberService proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* com.hello.aop.member..*.*(..))")
        private void allMember(){}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            Object arg1 = proceedingJoinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", proceedingJoinPoint.getSignature(), arg1);
            return proceedingJoinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)") // 파라미터를 args로 직접 Advice의 인자로 받을 수 있다.
        public Object logArgs2(ProceedingJoinPoint proceedingJoinPoint, String arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", proceedingJoinPoint.getSignature(), arg);
            return proceedingJoinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg);
        }

        @Before(value = "allMember() && this(obj)", argNames = "joinPoint,obj") // this는 프록시 객체(스프링 빈으로 등록된 객체)를 가져온다.
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")   // target은 타겟 객체를 직접 가져온다.
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")   // @target은 타입의 애너테이션을 전달 받는다.
        public void atTargetArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target]{}, annotation={}, annotationValue={}", joinPoint.getSignature(), annotation, annotation.value());
        }

        @Before("allMember() && @within(annotation)")   // @within은 타입의 애너테이션을 전달 받는다.
        public void atWithinArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within]{}, annotation={}, annotationValue={}", joinPoint.getSignature(), annotation, annotation.value());
        }

        @Before("allMember() && @annotation(annotation)")   // @annotation은 메서드의 애너테이션을 전달 받는다.
        public void atAnnotationArgs(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation]{}, annotation={}, annotationValue={}", joinPoint.getSignature(), annotation, annotation.value());
        }

    }
}
