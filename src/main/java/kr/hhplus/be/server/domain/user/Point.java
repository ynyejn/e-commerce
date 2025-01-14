package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.BaseEntity;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {
    private static final BigDecimal MAX_POINT = new BigDecimal("1000000"); // 최대 포인트
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "point", nullable = false, precision = 10, scale = 2)
    private BigDecimal point;

    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PointHistory> histories = new ArrayList<>();

    private Point(User user, BigDecimal point) {
        this.user = user;
        this.point = point;
    }

    public static Point create(User user) {
        return new Point(user, BigDecimal.ZERO);
    }

    public Point charge(BigDecimal amount) {
        if (amount.compareTo(MAX_POINT) > 0) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
        if (this.point.add(amount).compareTo(MAX_POINT) > 0) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }

        this.point = this.point.add(amount);
        this.histories.add(PointHistory.create(this, amount));
        return this;
    }

    public Point use(BigDecimal amount) {
        if (this.point.compareTo(amount) < 0) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }

        this.point = this.point.subtract(amount);
        this.histories.add(PointHistory.create(this, amount.negate()));
        return this;
    }
}
