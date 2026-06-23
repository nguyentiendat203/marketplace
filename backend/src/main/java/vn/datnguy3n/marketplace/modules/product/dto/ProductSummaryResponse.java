package vn.datnguy3n.marketplace.modules.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSummaryResponse {

    private UUID id;
    private UUID sellerId;
    private String sellerName;

    private BigDecimal price;
    private String conditionNote;
    private String status;
    private String originalLanguage;

    private String title;

    private UUID categoryId;
    private String categoryName;

    private UUID brandId;
    private String brandName;

    private String thumbnailUrl;

    private Instant createdAt;
}
