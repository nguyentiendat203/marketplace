package vn.datnguy3n.marketplace.modules.payment;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

@Service
public class PaymentServiceImpl extends BaseCRUDServiceImpl<Payment> implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Payment getByOrderId(UUID orderId) {
        // TODO: throw NotFoundException if absent
        return paymentRepository.findByOrder_Id(orderId).orElse(null);
    }

    @Transactional
    @Override
    public void handleWebhook(String payload, String stripeSignature) {
        // TODO: verify Stripe signature, update Payment status, sync OrderService via service call
    }

    @Transactional
    @Override
    public Payment refund(UUID orderId) {
        // TODO: call Stripe refund API, update Payment status=REFUNDED, sync OrderService
        return null;
    }
}
