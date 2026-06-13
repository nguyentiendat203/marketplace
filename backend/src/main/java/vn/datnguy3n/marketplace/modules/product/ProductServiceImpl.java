package vn.datnguy3n.marketplace.modules.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends BaseCRUDServiceImpl<Product> implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> getBySeller(UUID sellerId) {
        // TODO: ownership check, resolve Category/Brand, build entity, persist images & attributes
        return productRepository.findBySeller_Id(sellerId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> getByCategory(UUID categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
}
