package com.study.springkafka.consumer;

import com.study.springkafka.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {
    @KafkaListener(topics = "orders", groupId = "order-group")
    public void listen(
            @Payload OrderEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
            ) {

        try {
            log.info("Received orderId: {}, partition: {}, offset: {}",
                    event.getOrderId(), partition, offset);
            processOrder(event);
        } catch (Exception e) {
            log.error("Error processing order: {}", event.getOrderId(), e);

        }
    }

    protected void processOrder(OrderEvent event) {
        log.info("Processing order: {}", event.getOrderId());
    }
}
