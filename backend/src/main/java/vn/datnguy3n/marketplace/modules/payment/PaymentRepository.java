package vn.datnguy3n.marketplace.modules.payment;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends BaseRepository<Payment> {

    Optional<Payment> findByOrder_Id(UUID orderId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
