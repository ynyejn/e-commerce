package kr.hhplus.be.server.domain.kafka;

public interface IKafkaPublisher {
    void publish(String topic, String message);
}