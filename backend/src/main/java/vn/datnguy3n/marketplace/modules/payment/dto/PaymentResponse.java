package vn.datnguy3n.marketplace.modules.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {

    private UUID id;
    private UUID orderId;
    private UUID buyerId;
    private BigDecimal amount;
    private String currency;
    private String stripePaymentIntentId;
    private String status;
    private String webhookEvent;
    private Instant createdAt;
    private Instant updatedAt;
}
