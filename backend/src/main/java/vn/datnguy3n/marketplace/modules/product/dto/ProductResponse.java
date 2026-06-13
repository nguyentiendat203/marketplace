package vn.datnguy3n.marketplace.modules.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProductResponse {

    private UUID id;
    private UUID sellerId;
    private String title;
    private String description;
    private BigDecimal price;
    private String condition;
    private String status;
    private UUID categoryId;
    private String categoryName;
    private UUID brandId;
    private String brandName;
    private List<String> imageUrls;
    private Map<String, String> attributes;
    private LocalDateTime createdAt;
}
