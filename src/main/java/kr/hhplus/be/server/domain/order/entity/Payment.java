package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @Column(name = "payment_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    private Payment(Order order, BigDecimal paymentAmount) {
        this.order = order;
        this.paymentAmount = paymentAmount;
    }

    public static Payment create(Order order, BigDecimal paymentAmount) {
        return new Payment(order, paymentAmount);
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
