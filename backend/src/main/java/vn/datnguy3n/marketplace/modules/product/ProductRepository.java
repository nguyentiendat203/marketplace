package vn.datnguy3n.marketplace.modules.product;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findBySellerId(UUID sellerId);

    List<Product> findByCategoryId(UUID categoryId);

    List<Product> findByStatus(String status);
}
