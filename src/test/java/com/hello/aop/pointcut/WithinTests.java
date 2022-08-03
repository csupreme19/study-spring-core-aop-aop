package com.hello.aop.pointcut;

import com.hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class WithinTests {

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
    void withinExactMatchTest() {
        // within은 선언 타입만 지정하여 사용한다.
        // 선언타입 com.hello.aop.member.MemberServiceImpl
        pointcut.setExpression("within(com.hello.aop.member.MemberServiceImpl)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinStarMatchTest() {
        // within은 선언 타입만 지정하여 사용한다.
        // 선언타입 com.hello.aop.member.*Service*
        pointcut.setExpression("within(com.hello.aop.member.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSubPackageMatchTest() {
        // within은 선언 타입만 지정하여 사용한다.
        // 선언타입 com.hello..*
        pointcut.setExpression("within(com.hello..*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타겟의 타입에만 직접 적용, 인터페이스는 적용 불가")
    void withinSuperTypeMatchFailTest() {
        // within은 선언 타입만 지정하여 사용한다.
        // 선언타입 com.hello.MemberService
        pointcut.setExpression("within(com.hello.aop.member.MemberService)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("execution은 타입 기반, 인터페이스는 적용 가능")
    void executionSuperTypeMatchTest() {
        pointcut.setExpression("execution(* com.hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
