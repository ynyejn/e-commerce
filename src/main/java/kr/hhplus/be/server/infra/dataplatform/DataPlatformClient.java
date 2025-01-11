package kr.hhplus.be.server.infra.dataplatform;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformClient {
    public void send(String event) {
        try {
            log.info("Event sent successfully - event: {}", event);
        } catch (Exception e) {
            log.error("Failed to send event to data platform - event: {}", event, e);
            throw e;
        }
    }
}
