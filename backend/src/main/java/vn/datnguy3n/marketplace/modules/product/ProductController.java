package vn.datnguy3n.marketplace.modules.product;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseCRUDController<Product> {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        super(productService);
        this.productService = productService;
    }


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(productService.getBySeller(sellerId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(productService.getByCategory(categoryId));
    }
}
