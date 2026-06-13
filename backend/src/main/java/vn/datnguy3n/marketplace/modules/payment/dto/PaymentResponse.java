package vn.datnguy3n.marketplace.modules.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
