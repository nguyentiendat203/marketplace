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
import vn.datnguy3n.marketplace.core.crud.BaseEntity;
import vn.datnguy3n.marketplace.modules.order.entity.Order;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_buyer_id", nullable = false)
    private User buyer;

    @Column(name = "pay_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "pay_currency", nullable = false, length = 10)
    private String currency = "JPY";

    // Must not be null when status = COMPLETED (invariant #6)
    @Column(name = "pay_stripe_payment_intent_id", length = 100)
    private String stripePaymentIntentId;

    @Column(name = "pay_status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "pay_webhook_event", length = 100)
    private String webhookEvent;
}
