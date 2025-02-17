package kr.hhplus.be.server.interfaces.kafka;

import kr.hhplus.be.server.domain.kafka.IKafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
@Slf4j
public class KafkaController {
    private final IKafkaPublisher kafkaPublisher;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody String message) {
        try {
            kafkaPublisher.publish("test-topic", message);
            return ResponseEntity.ok("메시지 발행 성공!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("메시지 발행 실패: " + e.getMessage());
        }
    }
}