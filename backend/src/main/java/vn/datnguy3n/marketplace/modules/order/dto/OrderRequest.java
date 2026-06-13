package vn.datnguy3n.marketplace.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private UUID buyerId;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    private String building;

    @NotBlank
    private String recipientName;

    @NotBlank
    private String phone;
}
