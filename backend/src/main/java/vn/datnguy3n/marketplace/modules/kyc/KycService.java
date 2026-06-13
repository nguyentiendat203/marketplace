package vn.datnguy3n.marketplace.modules.kyc;

import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycSubmitRequest;

import java.util.List;
import java.util.UUID;

public interface KycService {

    KycResponse submit(KycSubmitRequest request);

    KycResponse getById(UUID id);

    KycResponse getLatestByUserId(UUID userId);

    List<KycResponse> getPendingRequests();

    KycResponse approve(UUID id, String reviewedBy);

    KycResponse reject(UUID id, String reviewedBy, String note);
}
