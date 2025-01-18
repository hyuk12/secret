package com.study.springkafka.producer;

import com.study.springkafka.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC  = "orders";

    public void sendOrder(OrderEvent event) {
        kafkaTemplate.send(TOPIC, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send message: {}", event.getOrderId(), ex);
                    } else {
                        log.info("Message sent Successfully: {}, partition: {}",
                                event.getOrderId(), result.getRecordMetadata().partition());
                    }
                });
    }

    public void sendOrderSync(OrderEvent event) throws Exception{
        try {
            SendResult<String, OrderEvent> result = kafkaTemplate.send(TOPIC, event.getOrderId(), event).get();
            log.info("Message sent synchronous Successfully: {}, partition: {}",
                    event.getOrderId(), result.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error sending message", e);
            throw e;
        }
    }
}
