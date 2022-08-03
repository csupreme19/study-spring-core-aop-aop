package com.hello.aop.pointcut;

import com.hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTests {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    Method helloMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethodTest() {
        // public java.lang.String com.hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입  선언타입?                                 메서드이름 파라미터 예외?
        // public   String  com.hello.aop.member.MemberServiceImpl.hello(String)
        pointcut.setExpression("execution(public String com.hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *         *         (..)
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *            hello   (..)
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameStarPrefixMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *            *llo   (..)
        pointcut.setExpression("execution(* *llo(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameStarSuffixMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *            hel*   (..)
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFailTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *            no   (..)
        pointcut.setExpression("execution(* no(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?                               메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.MemberServiceImpl.hello   (..)
        pointcut.setExpression("execution(* com.hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageStarMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?             메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.*.*   (..)
        pointcut.setExpression("execution(* com.hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageSubPackageMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?         메서드이름 파라미터 예외?
        //            *     com.hello.aop...*.*   (..)
        pointcut.setExpression("execution(* com.hello.aop..*.*(..))");  // 하위 패키지까지 포함하려면 .. 사용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageSubPackage2MatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?         메서드이름 파라미터 예외?
        //            *     com.hello.aop...*.*   (..)
        pointcut.setExpression("execution(* com.hello..*.*(..))");  // 하위 패키지까지 포함하려면 .. 사용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
