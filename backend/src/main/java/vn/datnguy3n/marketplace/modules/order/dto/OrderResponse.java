package vn.datnguy3n.marketplace.modules.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {

    private UUID id;
    private UUID buyerId;
    private UUID sellerId;
    private UUID productId;
    private BigDecimal amount;
    private String status;
    private String stripePaymentIntentId;
    private ShippingAddressInfo shippingAddress;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter
    @Setter
    public static class ShippingAddressInfo {
        private String postalCode;
        private String prefecture;
        private String city;
        private String street;
        private String building;
        private String recipientName;
        private String phone;
    }
}
