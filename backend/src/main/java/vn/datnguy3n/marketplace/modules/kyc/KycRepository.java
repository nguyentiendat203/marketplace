package vn.datnguy3n.marketplace.modules.kyc;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KycRepository extends JpaRepository<KycRecord, UUID> {

    Optional<KycRecord> findTopByUser_IdOrderByCreatedAtDesc(UUID userId);

    List<KycRecord> findByStatus(String status);
}
