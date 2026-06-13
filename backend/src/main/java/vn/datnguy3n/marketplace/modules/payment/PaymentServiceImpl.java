package vn.datnguy3n.marketplace.modules.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentRequest;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse initiate(PaymentRequest request) {
        // TODO: call Stripe API (@Async), persist Payment with PENDING status, return DTO
        return null;
    }

    @Override
    public PaymentResponse getByOrderId(UUID orderId) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public void handleWebhook(String payload, String stripeSignature) {
        // TODO: verify Stripe signature, update Payment status, sync OrderService via service call
    }

    @Override
    public PaymentResponse refund(UUID orderId) {
        // TODO: call Stripe refund API, update Payment status=REFUNDED, sync OrderService
        return null;
    }
}
