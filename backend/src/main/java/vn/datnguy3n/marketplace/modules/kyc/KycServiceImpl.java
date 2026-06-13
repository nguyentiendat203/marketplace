package vn.datnguy3n.marketplace.modules.kyc;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

@Service
public class KycServiceImpl extends BaseCRUDServiceImpl<KycRecord> implements KycService {

    private final KycRepository kycRepository;

    public KycServiceImpl(KycRepository kycRepository) {
        super(kycRepository);
        this.kycRepository = kycRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public KycRecord getLatestByUserId(UUID userId) {
        return kycRepository.findTopByUser_IdOrderByCreatedAtDesc(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<KycRecord> getPendingRequests() {
        return kycRepository.findByStatus("PENDING");
    }

    @Transactional
    @Override
    public KycRecord approve(UUID id, String reviewedBy) {
        // TODO: set status=APPROVED, reviewedBy, persist; notify user via UserService
        return null;
    }

    @Transactional
    @Override
    public KycRecord reject(UUID id, String reviewedBy, String note) {
        // TODO: set status=REJECTED, reviewedBy, reviewNote, persist; notify user
        return null;
    }
}
