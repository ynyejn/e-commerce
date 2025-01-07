package kr.hhplus.be.server.domain.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.entity.OrderItem;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
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
    @JoinColumn(name = "product_stock_id", nullable = false)
    private ProductStock productStock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false)
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
