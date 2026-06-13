package vn.datnguy3n.marketplace.modules.kyc;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

import java.util.List;
import java.util.UUID;

public interface KycService extends BaseCRUDService<KycRecord> {

    KycRecord getLatestByUserId(UUID userId);

    List<KycRecord> getPendingRequests();

    KycRecord approve(UUID id, String reviewedBy);

    KycRecord reject(UUID id, String reviewedBy, String note);
}
