package vn.datnguy3n.marketplace.modules.kyc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class KycSubmitRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String documentType;

    @NotBlank
    private String frontImageUrl;

    private String backImageUrl;

    private String selfieUrl;
}
