package kr.hhplus.be.server.domain.constant;

public enum CouponStatus {
    UNUSED("미사용"),
    USED("사용 완료"),
    EXPIRED("기간 만료");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
