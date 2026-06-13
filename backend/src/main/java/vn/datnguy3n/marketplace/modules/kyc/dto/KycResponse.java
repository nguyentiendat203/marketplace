package vn.datnguy3n.marketplace.modules.kyc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class KycResponse {

    private UUID id;
    private UUID userId;
    private String documentType;
    private String frontImageUrl;
    private String backImageUrl;
    private String selfieUrl;
    private String status;
    private String reviewedBy;
    private String reviewNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
