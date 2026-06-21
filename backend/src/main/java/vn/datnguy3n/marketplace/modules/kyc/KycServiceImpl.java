package vn.datnguy3n.marketplace.modules.kyc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.domain.Specification;

import com.turkraft.springfilter.boot.Filter;

import vn.datnguy3n.marketplace.common.ApplicationContextProvider;
import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.common.storage.StorageService;
import vn.datnguy3n.marketplace.common.utils.ImageUtils;
import vn.datnguy3n.marketplace.config.MinioProperties;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycReviewRequest;
import vn.datnguy3n.marketplace.modules.kyc.entity.DocumentType;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycStatus;
import vn.datnguy3n.marketplace.modules.user.UserService;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
public class KycServiceImpl extends BaseCRUDServiceImpl<KycRecord> implements KycService {

    private final KycRepository kycRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final MinioProperties minioProperties;

    public KycServiceImpl(KycRepository kycRepository, StorageService storageService,
            UserService userService, MinioProperties minioProperties) {
        super(kycRepository);
        this.kycRepository = kycRepository;
        this.storageService = storageService;
        this.userService = userService;
        this.minioProperties = minioProperties;
    }

    @Override
    @Transactional
    public KycResponse submitKyc(DocumentType documentType, MultipartFile frontImage,
            MultipartFile backImage, MultipartFile selfieImage) {

        String currentEmail = ApplicationContextProvider.getCurrentUserLogin()
                .orElseThrow(() -> new BusinessException("Không tìm thấy thông tin người dùng", HttpStatus.UNAUTHORIZED));

        User user = userService.findEntityByEmail(currentEmail);
        if (user == null) {
            throw new BusinessException("Người dùng không tồn tại", HttpStatus.NOT_FOUND);
        }

        boolean hasPendingOrApproved = kycRepository.existsByUser_IdAndStatusIn(
                user.getId(), List.of(KycStatus.PENDING, KycStatus.APPROVED));
        if (hasPendingOrApproved) {
            throw new BusinessException("Hồ sơ KYC đang chờ xử lý hoặc đã được xác thực", HttpStatus.CONFLICT);
        }

        ImageUtils.validate(frontImage);
        if (backImage != null && !backImage.isEmpty()) ImageUtils.validate(backImage);
        if (selfieImage != null && !selfieImage.isEmpty()) ImageUtils.validate(selfieImage);

        String bucket = minioProperties.getBuckets().get("kyc-vault");
        String userId = user.getId().toString();

        String frontKey = String.format("kyc/%s/%s_front.jpg", userId, UUID.randomUUID());
        String frontImageKey = storageService.uploadBytes(ImageUtils.compress(frontImage), bucket, frontKey, "image/jpeg");

        String backImageKey = null;
        if (backImage != null && !backImage.isEmpty()) {
            String backKey = String.format("kyc/%s/%s_back.jpg", userId, UUID.randomUUID());
            backImageKey = storageService.uploadBytes(ImageUtils.compress(backImage), bucket, backKey, "image/jpeg");
        }

        String selfieKey = null;
        if (selfieImage != null && !selfieImage.isEmpty()) {
            String selfKey = String.format("kyc/%s/%s_selfie.jpg", userId, UUID.randomUUID());
            selfieKey = storageService.uploadBytes(ImageUtils.compress(selfieImage), bucket, selfKey, "image/jpeg");
        }

        KycRecord record = new KycRecord();
        record.setUser(user);
        record.setDocumentType(documentType);
        record.setFrontImageKey(frontImageKey);
        record.setBackImageKey(backImageKey);
        record.setSelfieKey(selfieKey);
        record.setStatus(KycStatus.PENDING);

        KycRecord saved = kycRepository.save(record);
        return mapToResponse(saved, bucket);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPaginationResponse getAdminKycList(@Filter Specification<KycRecord> spec, Pageable pageable) {
        Page<KycRecord> page;
        if (spec != null) {
            page = kycRepository.findAll(spec, pageable);
        } else {
            page = kycRepository.findAll(pageable);
        }

        String bucket = minioProperties.getBuckets().get("kyc-vault");
        List<KycResponse> responses = page.getContent().stream()
                .map(record -> mapToResponse(record, bucket))
                .collect(Collectors.toList());

        ResultPaginationResponse.Meta meta = new ResultPaginationResponse.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationResponse result = new ResultPaginationResponse();
        result.setMeta(meta);
        result.setResult(responses);
        return result;
    }

    @Override
    @Transactional
    public KycResponse reviewKyc(UUID id, KycReviewRequest request) {
        KycRecord record = getById(id);

        if (record.getStatus() != KycStatus.PENDING) {
            throw new BusinessException("Chỉ có thể xét duyệt hồ sơ đang ở trạng thái PENDING", HttpStatus.BAD_REQUEST);
        }

        String adminEmail = ApplicationContextProvider.getCurrentUserLogin()
                .orElseThrow(() -> new BusinessException("Không tìm thấy thông tin Admin", HttpStatus.UNAUTHORIZED));

        record.setReviewedBy(adminEmail);
        record.setReviewNote(request.getReviewNote());
        record.setStatus(request.getStatus());

        if (request.getStatus() == KycStatus.APPROVED) {
            User user = record.getUser();
            user.setKycVerified(true);
            userService.saveUser(user);
        }

        KycRecord saved = kycRepository.save(record);
        String bucket = minioProperties.getBuckets().get("kyc-vault");
        return mapToResponse(saved, bucket);
    }

    private KycResponse mapToResponse(KycRecord record, String bucket) {
        KycResponse response = new KycResponse();
        response.setId(record.getId());
        response.setUserId(record.getUser().getId());
        response.setUserEmail(record.getUser().getEmail());
        response.setUserPhone(record.getUser().getPhone());
        response.setDocumentType(record.getDocumentType());
        response.setStatus(record.getStatus());
        response.setReviewedBy(record.getReviewedBy());
        response.setReviewNote(record.getReviewNote());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        if (record.getFrontImageKey() != null) {
            response.setFrontImageUrl(storageService.generatePresignedUrl(bucket, record.getFrontImageKey(), 15));
        }
        if (record.getBackImageKey() != null) {
            response.setBackImageUrl(storageService.generatePresignedUrl(bucket, record.getBackImageKey(), 15));
        }
        if (record.getSelfieKey() != null) {
            response.setSelfieUrl(storageService.generatePresignedUrl(bucket, record.getSelfieKey(), 15));
        }

        return response;
    }
}
