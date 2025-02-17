package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "order_outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOutbox extends BaseEntity {
   @Id
   @GeneratedValue(strategy = IDENTITY)
   private Long id;

   @Column(name = "order_id")
   private Long orderId;

   @Column(name = "event_type")
   private String eventType;

   @Column(columnDefinition = "json")
   private String payload;

   @Column(name = "published_at")
   private LocalDateTime publishedAt;

   @Builder
   public OrderOutbox(Long orderId, String eventType, String payload) {
       this.orderId = orderId;
       this.eventType = eventType;
       this.payload = payload;
       this.publishedAt = null;
   }

   public void markAsPublished() {
       this.publishedAt = LocalDateTime.now();
   }
}