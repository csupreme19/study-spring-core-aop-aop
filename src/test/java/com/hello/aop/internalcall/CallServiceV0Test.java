package com.hello.aop.internalcall;

import com.hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    // 내부 클래스에서 내부 메서드를 직접 호출시 프록시를 통하지 않는다. 따라서 AOP가 미적용 된다.
    @Test
    void external() {
        log.info("target={}", callServiceV0.getClass());
        callServiceV0.external();
    }

    @Test
    void internal() {
        log.info("target={}", callServiceV0.getClass());
        callServiceV0.internal();
    }
}