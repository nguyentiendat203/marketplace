package vn.datnguy3n.marketplace.modules.product.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

public interface ProductService extends BaseCRUDService<Product> {

    ProductResponse createProduct(ProductRequest request, MultipartFile thumbnail, MultipartFile[] images);

    ProductResponse getProductDetail(UUID id);

    ResultPaginationResponse getProducts(UUID categoryId, BigDecimal minPrice, BigDecimal maxPrice,
            List<UUID> attributeOptionIds, Pageable pageable);

    List<Product> getBySeller(UUID sellerId);
}
