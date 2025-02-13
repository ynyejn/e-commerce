package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.domain.order.OrderConfirmedEvent;
import kr.hhplus.be.server.infra.dataplatform.DataPlatformClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {
    private final DataPlatformClient dataPlatformClient;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        log.info("Send to data platform - event: {}", event);
        dataPlatformClient.send(event.toString());
    }
}