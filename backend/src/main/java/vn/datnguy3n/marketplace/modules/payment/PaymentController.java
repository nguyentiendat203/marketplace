package vn.datnguy3n.marketplace.modules.payment;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController extends BaseCRUDController<Payment, PaymentResponse> {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        super(paymentService);
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {
        paymentService.handleWebhook(payload, stripeSignature);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/{orderId}/refund")
    public ResponseEntity<PaymentResponse> refund(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.refund(orderId));
    }
}
