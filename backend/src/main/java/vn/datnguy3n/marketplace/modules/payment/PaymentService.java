package vn.datnguy3n.marketplace.modules.payment;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

import java.util.UUID;

public interface PaymentService extends BaseCRUDService<Payment> {

    Payment getByOrderId(UUID orderId);

    void handleWebhook(String payload, String stripeSignature);

    Payment refund(UUID orderId);
}
