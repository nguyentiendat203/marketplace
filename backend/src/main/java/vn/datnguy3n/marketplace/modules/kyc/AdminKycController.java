package vn.datnguy3n.marketplace.modules.kyc;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycReviewRequest;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/kyc")
public class AdminKycController {

    private final KycService kycService;

    @GetMapping()
    public ResponseEntity<ResultPaginationResponse> getKycList(
            @Filter Specification<KycRecord> spec,
            Pageable pageable) {

        return ResponseEntity.ok(kycService.getAdminKycList(spec, pageable));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<KycResponse> reviewKyc(
            @PathVariable("id") UUID id,
            @Valid @RequestBody KycReviewRequest request) {

        return ResponseEntity.ok(kycService.reviewKyc(id, request));
    }
}
