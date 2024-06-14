package com.dyx.dispatcherservice;

/**
 * 订单打包消息dto
 */
public record OrderAcceptedMessage(
        Long orderId
) {}
