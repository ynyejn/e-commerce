package kr.hhplus.be.server.infra.kafka;

import kr.hhplus.be.server.domain.kafka.IKafkaPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPublisher implements IKafkaPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("메시지 발행: {}", message);
    }
}