package vn.datnguy3n.marketplace.modules.kyc.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.modules.kyc.entity.DocumentType;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycStatus;

@Getter
@Setter
public class KycResponse {

    private UUID id;
    private UUID userId;
    private String userEmail;
    private String userPhone;
    private DocumentType documentType;
    private String frontImageUrl;
    private String backImageUrl;
    private String selfieUrl;
    private KycStatus status;
    private String reviewedBy;
    private String reviewNote;
    private Instant createdAt;
    private Instant updatedAt;
}
