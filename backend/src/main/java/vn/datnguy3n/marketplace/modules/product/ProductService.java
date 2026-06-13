package vn.datnguy3n.marketplace.modules.product;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService extends BaseCRUDService<Product> {

    List<Product> getBySeller(UUID sellerId);

    List<Product> getByCategory(UUID categoryId);
}
