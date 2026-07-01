package vn.datnguy3n.marketplace.modules.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.product.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseCRUDController<Product, ProductResponse> {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        super(productService);
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestPart("data") ProductRequest request,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        ProductResponse response = productService.createProduct(request, thumbnail, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
