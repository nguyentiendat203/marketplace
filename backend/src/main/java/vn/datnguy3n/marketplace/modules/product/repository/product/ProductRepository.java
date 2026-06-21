package vn.datnguy3n.marketplace.modules.product.repository.product;

import java.util.List;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.product.entity.ProductStatus;

public interface ProductRepository extends BaseRepository<Product> {

    List<Product> findBySeller_Id(UUID sellerId);

    List<Product> findByCategory_Id(UUID categoryId);

    List<Product> findByStatus(ProductStatus status);
}
