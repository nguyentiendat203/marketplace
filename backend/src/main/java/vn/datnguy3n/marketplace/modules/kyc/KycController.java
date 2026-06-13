package vn.datnguy3n.marketplace.modules.kyc;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycSubmitRequest;

@RestController
@RequestMapping("/api/v1/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping
    public ResponseEntity<ApiResponse<KycResponse>> submit(@Valid @RequestBody KycSubmitRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("KYC submitted", 200, kycService.submit(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KycResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("KYC record fetched", 200, kycService.getById(id)));
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<ApiResponse<KycResponse>> getLatestByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok("KYC record fetched", 200, kycService.getLatestByUserId(userId)));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<KycResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending KYC requests fetched", 200, kycService.getPendingRequests()));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<KycResponse>> approve(
            @PathVariable UUID id,
            @RequestParam String reviewedBy) {
        return ResponseEntity.ok(ApiResponse.ok("KYC approved", 200, kycService.approve(id, reviewedBy)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<KycResponse>> reject(
            @PathVariable UUID id,
            @RequestParam String reviewedBy,
            @RequestParam String note) {
        return ResponseEntity.ok(ApiResponse.ok("KYC rejected", 200, kycService.reject(id, reviewedBy, note)));
    }
}
