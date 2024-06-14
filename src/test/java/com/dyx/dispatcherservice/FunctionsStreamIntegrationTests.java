package com.dyx.dispatcherservice;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// 配置test binder
@Import(TestChannelBinderConfiguration.class)
class FunctionsStreamIntegrationTests {
	// 输入绑定：packlabel-in-0，无法装配为误报
	@Autowired
	private InputDestination input;
	// 输出绑定：packlabel-out-0
	@Autowired
	private OutputDestination output;

	//使用jackson反序列化
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void whenOrderAcceptedThenDispatched() throws IOException {
		//运行应用时，框架会进行类型转换，测试时，手动包装Message和解析byte[]为java DTO

		long orderId = 121L;
		Message<OrderAcceptedMessage> inputMessage = MessageBuilder
				.withPayload(new OrderAcceptedMessage(orderId)).build();
		//发送消息至输入通道
		this.input.send(inputMessage);

		Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder
				.withPayload(new OrderDispatchedMessage(orderId)).build();
		//断言
		assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
				.isEqualTo(expectedOutputMessage.getPayload());
	}
}
