package vn.datnguy3n.marketplace.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file, String bucket, String objectKey);

    String uploadBytes(byte[] data, String bucket, String objectKey, String contentType);

    String generatePresignedUrl(String bucket, String objectKey, int expiryMinutes);
}
