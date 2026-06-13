package vn.datnguy3n.marketplace.modules.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProductRequest {

    @NotNull
    private UUID categoryId;

    private UUID brandId;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotBlank
    private String condition;

    private List<String> imageUrls;

    private Map<String, String> attributes;
}
