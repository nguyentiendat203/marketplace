package vn.datnguy3n.marketplace.modules.product.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

    private UUID id;
    private String name;
    private String imageUrl;
    private Short level;
    private UUID parentId;
}
