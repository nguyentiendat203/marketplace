package vn.datnguy3n.marketplace.modules.product;

import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(UUID sellerId, ProductRequest request);

    ProductResponse getById(UUID id);

    List<ProductResponse> getBySeller(UUID sellerId);

    List<ProductResponse> getByCategory(UUID categoryId);

    ProductResponse update(UUID id, UUID requesterId, ProductRequest request);

    void softDelete(UUID id, UUID requesterId);
}
