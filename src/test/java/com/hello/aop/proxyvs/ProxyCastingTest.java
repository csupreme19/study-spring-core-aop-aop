package com.hello.aop.proxyvs;

import com.hello.aop.member.MemberService;
import com.hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);    // JDK 동적 프록시(기본값)

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService proxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현체로 캐스팅 실패
        // JDK Dyanmic Proxy는 인터페이스를 기반으로 프록시를 생성하므로 구현체는 모른다. ClassCastException 예외 발생
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl proxyImpl = (MemberServiceImpl) proxyFactory.getProxy();
        });

        log.info("isAopProxy={}", AopUtils.isAopProxy(proxy));
        log.info("isJdkDynamicProxy={}", AopUtils.isJdkDynamicProxy(proxy));
        log.info("isCglibProxy={}", AopUtils.isCglibProxy(proxy));
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);    // CGLIB 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService proxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현체로 캐스팅 성공
        // CGLIB 프록시는 구현체를 기반으로 프록시를 생성
        MemberServiceImpl proxyImpl = (MemberServiceImpl) proxyFactory.getProxy();

        log.info("isAopProxy={}", AopUtils.isAopProxy(proxy));
        log.info("isJdkDynamicProxy={}", AopUtils.isJdkDynamicProxy(proxy));
        log.info("isCglibProxy={}", AopUtils.isCglibProxy(proxy));
    }
}
