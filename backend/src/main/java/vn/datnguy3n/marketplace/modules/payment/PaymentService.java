package vn.datnguy3n.marketplace.modules.payment;

import vn.datnguy3n.marketplace.modules.payment.dto.PaymentRequest;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(PaymentRequest request);

    PaymentResponse getByOrderId(UUID orderId);

    void handleWebhook(String payload, String stripeSignature);

    PaymentResponse refund(UUID orderId);
}
