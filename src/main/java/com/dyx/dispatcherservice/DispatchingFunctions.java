package com.dyx.dispatcherservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
// 在配置类中定义函数：Function、Supplier或Consumer
@Configuration
public class DispatchingFunctions {
    /**
     * 打包订单
     */
    //声明为bean，以便Spring Cloud Function发现和管理
    @Bean
    public Function<OrderAcceptedMessage,Long> pack(){
        return message->{
            log.info("the order with id {} is packed.",message.orderId());
            return message.orderId();
        };
    }

    /**
     * 标记订单
     */
    @Bean
    public Function<Flux<Long>,Flux<OrderDispatchedMessage>> label(){
        return orderFlux -> orderFlux.map(orderId->{
            log.info("the order with id {} is labeled.",orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }
}
