package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.domain.order.OrderCompletedEvent;
import kr.hhplus.be.server.infra.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.infra.outbox.OrderOutBoxRepository;
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
    private final OrderOutBoxRepository orderOutboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveToOutbox(OrderCompletedEvent event) {
        orderOutboxRepository.save(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        log.info("Send to data platform - event: {}", event);
        dataPlatformClient.send(event.toString());
    }
}