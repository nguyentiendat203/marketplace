package vn.datnguy3n.marketplace.modules.kyc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycStatus;

@Getter
@Setter
public class KycReviewRequest {

    @NotNull
    private KycStatus status;

    private String reviewNote;
}
