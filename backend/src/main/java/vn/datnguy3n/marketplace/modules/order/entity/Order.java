package vn.datnguy3n.marketplace.modules.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.common.BaseEntity;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private Orderstatus status = Orderstatus.PENDING;

    @Column(length = 100)
    private String stripePaymentIntentId;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderAddress shippingAddress;
}
