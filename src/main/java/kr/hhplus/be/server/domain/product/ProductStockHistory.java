package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.support.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "product_stock_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_stock_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProductStock productStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;


    @Column(name = "quantity", nullable = false)
    private int quantity;

    private ProductStockHistory(ProductStock productStock, OrderItem orderItem, int quantity) {
        this.productStock = productStock;
        this.orderItem = orderItem;
        this.quantity = -quantity;
    }

    public static ProductStockHistory create(ProductStock productStock, OrderItem orderItem, int quantity) {
        return new ProductStockHistory(productStock, orderItem, quantity);
    }
}
