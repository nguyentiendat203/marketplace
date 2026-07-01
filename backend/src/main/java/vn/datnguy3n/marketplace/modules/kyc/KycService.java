package vn.datnguy3n.marketplace.modules.kyc;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.jpa.domain.Specification;

import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycReviewRequest;
import vn.datnguy3n.marketplace.modules.kyc.entity.DocumentType;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

public interface KycService extends BaseCRUDService<KycRecord, KycResponse> {

    KycResponse submitKyc(DocumentType documentType, MultipartFile frontImage, MultipartFile backImage, MultipartFile selfieImage);

    ResultPaginationResponse getAdminKycList(@Filter Specification<KycRecord> spec, Pageable pageable);

    KycResponse reviewKyc(UUID id, KycReviewRequest request);
}
