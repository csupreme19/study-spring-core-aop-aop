package com.hello.aop.proxyvs;

import com.hello.aop.member.MemberService;
import com.hello.aop.member.MemberServiceImpl;
import com.hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})   // Spring은 기본적으로 CGLIB 프록시 사용
@Import(ProxyDIAspect.class)
class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    /**
     * Spring 3.2부터 CGLIB를 기본 프록시 라이브러리로 제공
     * 문제점: 생성자가 2번 호출된다.
     * 1. target의 생성자 호출 1번(target 객체가 생성될 때)
     * 2. proxy의 생성자 호출 1번(proxy 객체 부모 생성자 = target 생성자)
     * 이유: proxy의 부모 클래스는 target이므로 부모 클래스의 생성자도 동일하게 호출
     * Spring 4.0부터 objenesis 라이브러리를 사용하여 생성자 호출 없이 객체 생성이 가능하므로 생성자 2번 호출의 문제점이 해결
     */
    @Test
    void doTrace() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}