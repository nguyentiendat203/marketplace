package vn.datnguy3n.marketplace.modules.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID buyerId;
}
