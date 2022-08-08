package com.hello.aop.exam.aop;

import com.hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    //    @Around("@annotation(com.hello.aop.exam.annotation.Retry)")와 동일 (인자에 이미 타입이 정의되어 있음)
    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint proceedingJoinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry={}", proceedingJoinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for(int tryCount=0; tryCount < maxRetry; tryCount++) {
            try {
                if(tryCount > 0) log.info("[retry] retry count={}/{}", tryCount, maxRetry-1);
                return proceedingJoinPoint.proceed();
            } catch(Exception e)  {
                exceptionHolder = e;
            }
        }

        throw exceptionHolder;
    }

}
