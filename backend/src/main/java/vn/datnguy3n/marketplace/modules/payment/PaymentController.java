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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentRequest;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> initiate(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Payment initiated", 200, paymentService.initiate(request)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(ApiResponse.ok("Payment fetched", 200, paymentService.getByOrderId(orderId)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {
        paymentService.handleWebhook(payload, stripeSignature);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/{orderId}/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refund(@PathVariable UUID orderId) {
        return ResponseEntity.ok(ApiResponse.ok("Refund initiated", 200, paymentService.refund(orderId)));
    }
}
