package kr.hhplus.be.server.domain.constant;

import lombok.Getter;

@Getter
public enum DiscountType {
    FIXED("정액"),
    PERCENTAGE("정률");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }
}
