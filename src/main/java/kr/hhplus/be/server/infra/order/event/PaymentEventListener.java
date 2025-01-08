package kr.hhplus.be.server.infra.order.event;

import kr.hhplus.be.server.domain.order.event.PaymentEvent;
import kr.hhplus.be.server.infra.dataplatform.DataPlatformClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {
    private final DataPlatformClient dataPlatformClient;

    @EventListener
    public void handlePaymentEvent(PaymentEvent event) {
        log.info("Send to data platform - event: {}", event);
        dataPlatformClient.send(event.toString());
    }
}