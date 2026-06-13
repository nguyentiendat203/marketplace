package vn.datnguy3n.marketplace.modules.kyc;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController extends BaseCRUDController<KycRecord> {

    private final KycService kycService;
    public KycController(KycService kycService) {
        super(kycService);
        this.kycService = kycService;
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<ApiResponse<KycRecord>> getLatestByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok("KYC record fetched", HttpStatus.OK.value(), kycService.getLatestByUserId(userId)));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<KycRecord>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending KYC requests fetched", HttpStatus.OK.value(), kycService.getPendingRequests()));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<KycRecord>> approve(
            @PathVariable UUID id,
            @RequestParam String reviewedBy) {
        return ResponseEntity.ok(ApiResponse.ok("KYC approved", HttpStatus.OK.value(), kycService.approve(id, reviewedBy)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<KycRecord>> reject(
            @PathVariable UUID id,
            @RequestParam String reviewedBy,
            @RequestParam String note) {
        return ResponseEntity.ok(ApiResponse.ok("KYC rejected", HttpStatus.OK.value(), kycService.reject(id, reviewedBy, note)));
    }
}
