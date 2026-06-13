package vn.datnguy3n.marketplace.modules.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(UUID sellerId, ProductRequest request) {
        // TODO: resolve Category/Brand, build entity, persist images & attributes, return DTO
        return null;
    }

    @Override
    public ProductResponse getById(UUID id) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public List<ProductResponse> getBySeller(UUID sellerId) {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public List<ProductResponse> getByCategory(UUID categoryId) {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public ProductResponse update(UUID id, UUID requesterId, ProductRequest request) {
        // TODO: ownership check (requesterId == sellerId || admin), patch, persist, return DTO
        return null;
    }

    @Override
    public void softDelete(UUID id, UUID requesterId) {
        // TODO: ownership check, set deletedAt / deletedBy, persist
    }
}
