package com.hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV0 {

    public void external() {
        log.info("call external");
        // this.internal()과 동일하다. this는 프록시가 아닌 자기 자신의 내부 메서드를 의미한다. 따라서 Proxy가 미적용된다.
        internal();
    }

    public void internal() {
        log.info("call internal");
    }

}
