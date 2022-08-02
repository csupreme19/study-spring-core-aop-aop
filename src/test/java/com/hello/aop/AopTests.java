package com.hello.aop;

import com.hello.aop.order.OrderRepository;
import com.hello.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
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
