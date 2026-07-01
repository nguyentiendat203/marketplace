package vn.datnguy3n.marketplace.modules.payment;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

@Service
public class PaymentServiceImpl extends BaseCRUDServiceImpl<Payment, PaymentResponse> implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    protected BaseMapper<Payment, PaymentResponse> getMapper() {
        return paymentMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentResponse getByOrderId(UUID orderId) {
        // TODO: throw NotFoundException if absent
        return paymentRepository.findByOrder_Id(orderId).map(this::toDto).orElse(null);
    }

    @Transactional
    @Override
    public void handleWebhook(String payload, String stripeSignature) {
        // TODO: verify Stripe signature, update Payment status, sync OrderService via service call
    }

    @Transactional
    @Override
    public PaymentResponse refund(UUID orderId) {
        // TODO: call Stripe refund API, update Payment status=REFUNDED, sync OrderService
        return null;
    }
}
