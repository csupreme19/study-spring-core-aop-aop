package com.hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // 애너테이션은 컴파일 이후 런타임에 사라지므로 런타임에 동적 사용을 위한 설정
public @interface MethodAop {
    String value();
}
