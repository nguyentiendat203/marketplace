package vn.datnguy3n.marketplace.modules.payment.entity;

import java.math.BigDecimal;

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
import vn.datnguy3n.marketplace.modules.order.entity.Order;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency = "JPY";

    // Must not be null when status = COMPLETED (invariant #6)
    @Column(length = 100)
    private String stripePaymentIntentId;

    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(length = 100)
    private String webhookEvent;
}
