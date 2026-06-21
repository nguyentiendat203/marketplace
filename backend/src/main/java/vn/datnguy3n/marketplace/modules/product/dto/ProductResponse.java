package vn.datnguy3n.marketplace.modules.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductResponse {

    private UUID id;
    private UUID sellerId;
    private String sellerName;

    private BigDecimal price;
    private String conditionNote;
    private String status;
    private String originalLanguage;

    private String title;
    private String description;

    private UUID categoryId;
    private String categoryName;

    private UUID brandId;
    private String brandName;

    private String thumbnailUrl;
    private List<String> detailImages;

    private List<AttributeInfo> attributes;

    private Instant createdAt;

    @Getter
    @Setter
    public static class AttributeInfo {
        private UUID attributeTypeId;
        private String attributeTypeName;
        private UUID attributeOptionId;
        private String attributeOptionName;
    }
}
