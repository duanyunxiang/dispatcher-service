package com.dyx.dispatcherservice;

/**
 * 订单标记消息dto
 * @param orderId
 */
public record OrderDispatchedMessage(
        Long orderId
) {}
