package vn.datnguy3n.marketplace.modules.kyc;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KycRepository extends BaseRepository<KycRecord> {

    Optional<KycRecord> findTopByUser_IdOrderByCreatedAtDesc(UUID userId);

    List<KycRecord> findByStatus(String status);
}
