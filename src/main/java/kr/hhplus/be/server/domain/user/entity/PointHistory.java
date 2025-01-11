package kr.hhplus.be.server.domain.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.entity.Payment;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "point_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Point point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id"
    ,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Payment payment;

    @Column(name = "amount", nullable = false, precision = 10, scale = 4)
    private BigDecimal amount;

    private PointHistory(Point point, BigDecimal amount) {
        this.point = point;
        this.amount = amount;
    }

    public static PointHistory create(Point point, BigDecimal amount) {
        return new PointHistory(point, amount);
    }
}
