package com.hello.aop;

import com.hello.aop.order.OrderRepository;
import com.hello.aop.order.OrderService;
import com.hello.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
//@Import({AspectV1.class})
//@Import({AspectV2.class})
//@Import({AspectV3.class})
//@Import({AspectV4Pointcut.class})
@Import({AspectV5Order.LogAspect.class, AspectV5Order.TransactionAspect.class}) // 각 Aspect 클래스에 대하여 Bean 등록 필요
public class AopTests {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopTest() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void successTest() {
        orderService.orderItem("itemA");
    }

    @Test
    void exceptionTest() {
        assertThrows(IllegalStateException.class, () -> orderService.orderItem("ex"));
    }

}
