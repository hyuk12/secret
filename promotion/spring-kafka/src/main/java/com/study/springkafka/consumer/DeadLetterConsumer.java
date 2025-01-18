package com.study.springkafka.consumer;

import com.study.springkafka.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadLetterConsumer {

    @KafkaListener(topics = "orders.DLT", groupId = "dlt-group")
    public void listenDLT(
            @Payload OrderEvent event,
            Exception exception
            ) {
        log.error("Received failed order in DLT: {}, ERROR: {}", event.getOrderId(), exception.getMessage());
    }
}
