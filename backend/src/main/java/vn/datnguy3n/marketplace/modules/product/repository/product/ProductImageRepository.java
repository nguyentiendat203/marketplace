package vn.datnguy3n.marketplace.modules.product.repository.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.ProductImage;

public interface ProductImageRepository extends BaseRepository<ProductImage> {

    Optional<ProductImage> findByProduct_IdAndDisplayOrder(UUID productId, int displayOrder);

    List<ProductImage> findByProduct_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(UUID productId, int displayOrder);
}
