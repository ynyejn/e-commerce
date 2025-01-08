package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Payment extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
