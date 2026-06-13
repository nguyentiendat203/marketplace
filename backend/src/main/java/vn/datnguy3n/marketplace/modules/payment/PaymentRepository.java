package vn.datnguy3n.marketplace.modules.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
