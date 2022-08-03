package com.hello.aop.member;

import com.hello.aop.member.annotation.ClassAop;
import com.hello.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop("test class value")
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test method value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }
}
