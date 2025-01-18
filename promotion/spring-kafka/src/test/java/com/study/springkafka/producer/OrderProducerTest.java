package com.study.springkafka.producer;

import com.study.springkafka.model.OrderEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderProducerTest {

    @Autowired
    private OrderProducer producer;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private Consumer<String, OrderEvent> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "localhost:9092",
                "test-group",
                "true"
        );

        consumer = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(OrderEvent.class)
        ).createConsumer();

        consumer.subscribe(List.of("orders")); // test 용 토픽
        consumer.poll(Duration.ofMillis(100)); // 초기 데이터를 읽지 않도록 빠르게 폴링시킨다.
        consumer.seekToBeginning(consumer.assignment()); // 오프셋 초기화
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void testSendOrder() {
        // Given
        OrderEvent testOrderEvent = createTestOrderEvent();

        // When
        producer.sendOrder(testOrderEvent);

        // Then
        ConsumerRecord<String, OrderEvent> record = KafkaTestUtils.getSingleRecord(consumer, "orders");
        assertThat(record).isNotNull();
        assertThat(record.value().getOrderId()).isEqualTo(testOrderEvent.getOrderId());
        assertThat(record.value().getCustomerId()).isEqualTo(testOrderEvent.getCustomerId());
        assertThat(record.value().getTotalAmount()).isEqualTo(testOrderEvent.getTotalAmount());
    }

    private OrderEvent createTestOrderEvent() {
        List<OrderEvent.OrderItem> items = List.of(new OrderEvent.OrderItem("prod-1", 4, BigDecimal.valueOf(20.00)));
        return new OrderEvent("order-234", "cust-567", items, BigDecimal.valueOf(80.00), LocalDateTime.now());
    }
}