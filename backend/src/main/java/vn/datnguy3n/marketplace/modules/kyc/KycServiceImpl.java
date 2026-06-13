package vn.datnguy3n.marketplace.modules.kyc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycSubmitRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final KycRepository kycRepository;

    @Override
    public KycResponse submit(KycSubmitRequest request) {
        // TODO: persist KycRecord with PENDING status, trigger async MinIO upload notification
        return null;
    }

    @Override
    public KycResponse getById(UUID id) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public KycResponse getLatestByUserId(UUID userId) {
        // TODO: fetch latest record, map to DTO
        return null;
    }

    @Override
    public List<KycResponse> getPendingRequests() {
        // TODO: fetch all PENDING, map to DTOs
        return List.of();
    }

    @Override
    public KycResponse approve(UUID id, String reviewedBy) {
        // TODO: set status=APPROVED, reviewedBy, persist; notify user via UserService
        return null;
    }

    @Override
    public KycResponse reject(UUID id, String reviewedBy, String note) {
        // TODO: set status=REJECTED, reviewedBy, reviewNote, persist; notify user
        return null;
    }
}
