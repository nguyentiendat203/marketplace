package vn.datnguy3n.marketplace.modules.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

public interface ProductService extends BaseCRUDService<Product, ProductResponse> {

    ProductResponse createProduct(ProductRequest request, MultipartFile thumbnail, MultipartFile[] images);

    List<ProductResponse> getBySeller(UUID sellerId);
}
