package kr.hhplus.be.server.domain.user.entity;


import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.hhplus.be.server.support.exception.ApiErrorCode.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "`user`")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Point point;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponIssue> coupons = new ArrayList<>();

    private User(String name) {
        this.name = name;
    }

    public static User create(String name) {
        return new User(name);
    }

    public CouponIssue findCouponIssue(Long couponIssueId) {
        return this.coupons.stream()
                .filter(issue -> issue.getCoupon().getId().equals(couponIssueId))
                .findFirst()
                .orElseThrow(() -> new ApiException(NOT_FOUND));
    }


    public boolean hasCoupon(Coupon coupon) {
        return this.coupons.stream().anyMatch(issue -> issue.getCoupon().getId().equals(coupon.getId()));
    }

    public Point chargePoint(BigDecimal amount) {
        if (this.point == null) {
            this.point = Point.create(this);
        }
        return this.point.charge(amount);

    }

    public void issueCoupon(Coupon coupon) {
        if (hasCoupon(coupon)){ // 이미 발급된 쿠폰인지 확인
            throw new ApiException(CONFLICT);
        }
        coupon.issueCoupon();   // 쿠폰 상태가 발급 가능한지 확인 후 발급
    }


    public void pay(BigDecimal amount) {
        if (this.point == null) {
            this.point = Point.create(this);
        }
        this.point.use(amount);
    }
}
