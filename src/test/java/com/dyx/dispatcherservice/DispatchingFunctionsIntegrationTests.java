package com.dyx.dispatcherservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

// Spring Function集成测试
@FunctionalSpringBootTest
@Disabled("These tests are only necessary when using the functions alone (no bindings)")
public class DispatchingFunctionsIntegrationTests {
    // 框架管理的所有函数使用FunctionCatalog访问
    @Autowired
    private FunctionCatalog catalog;

    @Test
    void packAndLabelOrder(){
        //获取组合函数
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel=catalog.lookup(Function.class,"pack|label");

        long orderId=121L;
        OrderAcceptedMessage inputMessage=new OrderAcceptedMessage(orderId);
        StepVerifier.create(packAndLabel.apply(inputMessage))
                //断言最后得到的OrderDispatchedMessage符合预期
                .expectNextMatches(outputMessage-> outputMessage.equals(new OrderDispatchedMessage(orderId)))
                .verifyComplete();
    }

    @Test
    void packOrder() {
        Function<OrderAcceptedMessage, Long> pack = catalog.lookup(Function.class, "pack");

        long orderId = 121L;
        assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
    }

    @Test
    void labelOrder() {
        Function<Flux<Long>, Flux<OrderDispatchedMessage>> label = catalog.lookup(Function.class, "label");

        Flux<Long> orderId = Flux.just(121L);
        StepVerifier.create(label.apply(orderId))
                .expectNextMatches(outputMessage -> outputMessage.equals(new OrderDispatchedMessage(121L)))
                .verifyComplete();
    }
}
