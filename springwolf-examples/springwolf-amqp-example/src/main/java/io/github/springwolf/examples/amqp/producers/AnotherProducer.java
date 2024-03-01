// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.examples.amqp.producers;

import io.github.springwolf.core.asyncapi.scanners.channels.operationdata.annotation.AsyncOperation;
import io.github.springwolf.core.asyncapi.scanners.channels.operationdata.annotation.AsyncPublisher;
import io.github.springwolf.examples.amqp.dtos.AnotherPayloadDto;
import io.github.springwolf.plugins.amqp.scanners.channels.operationdata.annotation.AmqpAsyncOperationBinding;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnotherProducer {
    private final RabbitTemplate rabbitTemplate;

    @AsyncPublisher(
            operation =
                    @AsyncOperation(
                            channelName = "example-producer-channel-publisher",
                            description = "Custom, optional description defined in the AsyncPublisher annotation"))
    @AmqpAsyncOperationBinding()
    public void sendMessage(AnotherPayloadDto msg) {
        // send
        rabbitTemplate.convertAndSend("example-topic-exchange", "example-topic-routing-key", msg);
    }
}