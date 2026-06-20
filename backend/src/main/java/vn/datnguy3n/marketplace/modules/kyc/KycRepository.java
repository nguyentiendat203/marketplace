package vn.datnguy3n.marketplace.modules.kyc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycStatus;

public interface KycRepository extends BaseRepository<KycRecord> {

    Optional<KycRecord> findTopByUser_IdOrderByCreatedAtDesc(UUID userId);

    boolean existsByUser_IdAndStatusIn(UUID userId, List<KycStatus> statuses);

    Page<KycRecord> findByStatus(KycStatus status, Pageable pageable);
}
