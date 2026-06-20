package vn.datnguy3n.marketplace.common.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.springframework.web.multipart.MultipartFile;

import vn.datnguy3n.marketplace.core.exception.InvalidFileException;

public final class ImageUtils {

    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024L;
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final float COMPRESS_QUALITY = 0.75f;

    private ImageUtils() {}

    public static void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File không được để trống");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new InvalidFileException("Kích thước file không được vượt quá 5MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidFileException("Định dạng file không hợp lệ. Chỉ chấp nhận: JPEG, PNG, WEBP");
        }
    }

    public static byte[] compress(MultipartFile file) {
        try {
            BufferedImage original = ImageIO.read(file.getInputStream());
            if (original == null) {
                throw new InvalidFileException("Không thể đọc file ảnh");
            }

            // JPEG does not support alpha channel — flatten onto white background
            BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgb.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, original.getWidth(), original.getHeight());
            g.drawImage(original, 0, 0, null);
            g.dispose();

            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(COMPRESS_QUALITY);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writer.setOutput(ImageIO.createImageOutputStream(out));
            writer.write(null, new IIOImage(rgb, null, null), params);
            writer.dispose();

            return out.toByteArray();
        } catch (InvalidFileException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidFileException("Không thể xử lý file ảnh: " + e.getMessage());
        }
    }
}
