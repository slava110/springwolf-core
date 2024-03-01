// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.plugins.kafka.scanners.channels.annotation;

import io.github.springwolf.asyncapi.v3.bindings.ChannelBinding;
import io.github.springwolf.asyncapi.v3.bindings.MessageBinding;
import io.github.springwolf.asyncapi.v3.bindings.OperationBinding;
import io.github.springwolf.asyncapi.v3.bindings.kafka.KafkaChannelBinding;
import io.github.springwolf.asyncapi.v3.bindings.kafka.KafkaMessageBinding;
import io.github.springwolf.asyncapi.v3.bindings.kafka.KafkaOperationBinding;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.StringValueResolver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KafkaListenerUtilTest {

    @Test
    void getChannelName() {
        // given
        KafkaListener annotation = mock(KafkaListener.class);
        when(annotation.topics()).thenReturn(Arrays.array("${topic-1}", "${topic-2}"));

        StringValueResolver resolver = mock(StringValueResolver.class);
        when(resolver.resolveStringValue("${topic-1}")).thenReturn("topic-1");
        when(resolver.resolveStringValue("${topic-2}")).thenReturn("topic-2");

        // when
        String channelName = KafkaListenerUtil.getChannelName(annotation, resolver);

        // then
        assertEquals("topic-1", channelName);
    }

    @Test
    void buildChannelBinding() {
        // when
        Map<String, ChannelBinding> channelBinding = KafkaListenerUtil.buildChannelBinding();

        // then
        assertEquals(1, channelBinding.size());
        assertEquals(Sets.newTreeSet("kafka"), channelBinding.keySet());
        assertEquals(new KafkaChannelBinding(), channelBinding.get("kafka"));
    }

    @Test
    void buildOperationBinding() {
        // given
        KafkaListener annotation = mock(KafkaListener.class);
        when(annotation.groupId()).thenReturn("${group-id}");

        StringValueResolver resolver = mock(StringValueResolver.class);
        when(resolver.resolveStringValue("${group-id}")).thenReturn("group-id");

        // when
        Map<String, OperationBinding> operationBinding = KafkaListenerUtil.buildOperationBinding(annotation, resolver);

        // then
        assertEquals(1, operationBinding.size());
        assertEquals(Sets.newTreeSet("kafka"), operationBinding.keySet());

        KafkaOperationBinding expectedOperationBinding = KafkaOperationBinding.builder()
                .groupId(KafkaListenerUtil.buildKafkaGroupIdSchema("group-id"))
                .build();
        assertEquals(expectedOperationBinding, operationBinding.get("kafka"));
    }

    @Test
    void buildMessageBinding() {
        // when
        Map<String, MessageBinding> messageBinding = KafkaListenerUtil.buildMessageBinding();

        // then
        assertEquals(1, messageBinding.size());
        assertEquals(Sets.newTreeSet("kafka"), messageBinding.keySet());
        assertEquals(new KafkaMessageBinding(), messageBinding.get("kafka"));
    }
}