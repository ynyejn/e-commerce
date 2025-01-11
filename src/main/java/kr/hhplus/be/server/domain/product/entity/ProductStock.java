package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "productStock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductStockHistory> histories = new ArrayList<>();

    private ProductStock(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static ProductStock create(Product product) {
        return new ProductStock(product, 0);
    }

    public void allocateStock(int quantity) {
        if (this.quantity < quantity) {
            throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK);
        }
        this.quantity -= quantity;
    }

}
