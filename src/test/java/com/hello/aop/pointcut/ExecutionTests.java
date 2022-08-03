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
    Method internalMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
        internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
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
        //            *     com.hello..*.*           (..)
        pointcut.setExpression("execution(* com.hello..*.*(..))");  // 하위 패키지까지 포함하려면 .. 사용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?                              메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.MemberServiceImpl.*     (..)
        pointcut.setExpression("execution(* com.hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeSuperTypeMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?                         메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.MemberService.*     (..)
        pointcut.setExpression("execution(* com.hello.aop.member.MemberService.*(..))");    // 타입 매칭 - 상위 클래스/인터페이스를 지정하더라도 자식 클래스의 메서드가 매칭됨!
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeInternalMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?                              메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.MemberServiceImpl.*     (..)
        pointcut.setExpression("execution(* com.hello.aop.member.MemberServiceImpl.*(..))");    // 타입 매칭 - 부모 타입에 정의되어 있는 메서드에 한정된다.
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeInternalMatchFailTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입   선언타입?                              메서드이름 파라미터 예외?
        //            *     com.hello.aop.member.MemberService.     *     (..)
        pointcut.setExpression("execution(* com.hello.aop.member.MemberService.*(..))");    // 타입 매칭 - 부모 타입에 정의되어 있는 메서드에 한정된다.
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void parameterMatchTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *   (String)
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchNoArgsTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *       ()
        pointcut.setExpression("execution(* *())"); // 파라미터가 없는 메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatchOneArgTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *       (*)
        pointcut.setExpression("execution(* *(*))");    // 파라미터가 단 하나만 존재하는 메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchTwoArgsTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *       (*, *)
        pointcut.setExpression("execution(* *(*, *))");    // 파라미터가 딱 두 개만 존재하는 메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatchAllArgsTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *       (..)
        pointcut.setExpression("execution(* *(..))");   // 모든 파라미터
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchStartsWithStringArgsTest() {
        // PCD Pointcut Designator
        // 접근제어자? 반환타입 선언타입? 메서드이름 파라미터 예외?
        //            *             *       (String, ..)
        pointcut.setExpression("execution(* *(String, ..))");   // 첫번째 인자가 String인 모든 파라미터
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
