package vn.datnguy3n.marketplace.modules.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import vn.datnguy3n.marketplace.common.ApplicationContextProvider;
import vn.datnguy3n.marketplace.common.storage.StorageService;
import vn.datnguy3n.marketplace.common.utils.ImageUtils;
import vn.datnguy3n.marketplace.common.utils.LocaleHelper;
import vn.datnguy3n.marketplace.config.MinioProperties;
import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.product.dto.ProductRequest;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeOption;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeOptionTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeTypeTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Brand;
import vn.datnguy3n.marketplace.modules.product.entity.BrandTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Category;
import vn.datnguy3n.marketplace.modules.product.entity.CategoryTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.product.entity.ProductAttribute;
import vn.datnguy3n.marketplace.modules.product.entity.ProductImage;
import vn.datnguy3n.marketplace.modules.product.entity.ProductStatus;
import vn.datnguy3n.marketplace.modules.product.entity.ProductTranslation;
import vn.datnguy3n.marketplace.modules.product.mapper.ProductMapper;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeOptionRepository;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeOptionTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeTypeTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.brand.BrandRepository;
import vn.datnguy3n.marketplace.modules.product.repository.brand.BrandTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.category.CategoryRepository;
import vn.datnguy3n.marketplace.modules.product.repository.category.CategoryTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductAttributeRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductImageRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductTranslationRepository;
import vn.datnguy3n.marketplace.modules.user.UserService;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
public class ProductServiceImpl extends BaseCRUDServiceImpl<Product, ProductResponse> implements ProductService {

    private final ProductRepository productRepository;
    private final ProductTranslationRepository productTranslationRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final AttributeTypeTranslationRepository attributeTypeTranslationRepository;
    private final AttributeOptionTranslationRepository attributeOptionTranslationRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;
    private final BrandTranslationRepository brandTranslationRepository;
    private final StorageService storageService;
    private final MinioProperties minioProperties;
    private final LocaleHelper localeHelper;
    private final UserService userService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
            ProductTranslationRepository productTranslationRepository,
            ProductImageRepository productImageRepository,
            ProductAttributeRepository productAttributeRepository,
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            AttributeOptionRepository attributeOptionRepository,
            AttributeTypeTranslationRepository attributeTypeTranslationRepository,
            AttributeOptionTranslationRepository attributeOptionTranslationRepository,
            CategoryTranslationRepository categoryTranslationRepository,
            BrandTranslationRepository brandTranslationRepository,
            StorageService storageService,
            MinioProperties minioProperties,
            LocaleHelper localeHelper,
            UserService userService,
            ProductMapper productMapper) {
        super(productRepository);
        this.productRepository = productRepository;
        this.productTranslationRepository = productTranslationRepository;
        this.productImageRepository = productImageRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.attributeOptionRepository = attributeOptionRepository;
        this.attributeTypeTranslationRepository = attributeTypeTranslationRepository;
        this.attributeOptionTranslationRepository = attributeOptionTranslationRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.brandTranslationRepository = brandTranslationRepository;
        this.storageService = storageService;
        this.minioProperties = minioProperties;
        this.localeHelper = localeHelper;
        this.userService = userService;
        this.productMapper = productMapper;
    }

    @Override
    protected BaseMapper<Product, ProductResponse> getMapper() {
        return productMapper;
    }

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request, MultipartFile thumbnail, MultipartFile[] images) {
        String email = ApplicationContextProvider.getCurrentUserLogin()
                .orElseThrow(() -> new BusinessException("Unauthorized", HttpStatus.UNAUTHORIZED));
        User seller = userService.findEntityByEmail(email);

        if (!seller.isKycVerified()) {
            throw new BusinessException(
                    "Bạn phải hoàn thành xác thực danh tính (KYC) mới có quyền đăng bán sản phẩm!",
                    HttpStatus.FORBIDDEN);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("Danh mục không tồn tại", HttpStatus.NOT_FOUND));

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new BusinessException("Thương hiệu không tồn tại", HttpStatus.NOT_FOUND));
        }

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category);
        product.setBrand(brand);
        product.setPrice(request.getPrice());
        product.setConditionNote(request.getConditionNote());
        product.setStatus(ProductStatus.PENDING);
        product.setOriginalLanguage(
                request.getLanguageCode() != null && !request.getLanguageCode().isBlank()
                        ? request.getLanguageCode()
                        : "vi");
        Product saved = productRepository.save(product);

        ProductTranslation translation = new ProductTranslation();
        translation.setProduct(saved);
        translation.setLanguageCode(saved.getOriginalLanguage());
        translation.setTitle(request.getTitle());
        translation.setDescription(request.getDescription());
        productTranslationRepository.save(translation);

        if (request.getAttributeOptionIds() != null) {
            for (UUID optionId : request.getAttributeOptionIds()) {
                AttributeOption option = attributeOptionRepository.findById(optionId)
                        .orElseThrow(() -> new BusinessException(
                                "Thuộc tính không tồn tại: " + optionId, HttpStatus.NOT_FOUND));
                ProductAttribute attr = new ProductAttribute();
                attr.setProduct(saved);
                attr.setAttributeOption(option);
                productAttributeRepository.save(attr);
            }
        }

        if (thumbnail != null && !thumbnail.isEmpty()) {
            ImageUtils.validate(thumbnail);
            byte[] compressed = ImageUtils.compress(thumbnail);
            String objectKey = saved.getId() + "/0.jpg";
            storageService.uploadBytes(compressed, "products", objectKey, "image/jpeg");
            ProductImage img = new ProductImage();
            img.setProduct(saved);
            img.setImageUrl(objectKey);
            img.setDisplayOrder(0);
            productImageRepository.save(img);
        }

        if (images != null) {
            for (int i = 0; i < images.length; i++) {
                MultipartFile file = images[i];
                ImageUtils.validate(file);
                byte[] compressed = ImageUtils.compress(file);
                String objectKey = saved.getId() + "/" + (i + 1) + ".jpg";
                storageService.uploadBytes(compressed, "products", objectKey, "image/jpeg");
                ProductImage img = new ProductImage();
                img.setProduct(saved);
                img.setImageUrl(objectKey);
                img.setDisplayOrder(i + 1);
                productImageRepository.save(img);
            }
        }

        return toDto(saved);
        // return buildResponse(saved, localeHelper.getCurrentLanguage());
    }

    @Override
    protected ProductResponse toDto(Product entity) {
        return buildResponse(entity, localeHelper.getCurrentLanguage());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> getBySeller(UUID sellerId) {
        return productRepository.findBySeller_Id(sellerId).stream().map(this::toDto).toList();
    }

    private ProductResponse buildResponse(Product product, String lang) {
        ProductResponse resp = new ProductResponse();
        resp.setId(product.getId());
        resp.setPrice(product.getPrice());
        resp.setConditionNote(product.getConditionNote());
        resp.setStatus(product.getStatus().name());
        resp.setOriginalLanguage(product.getOriginalLanguage());
        resp.setCreatedAt(product.getCreatedAt());

        if (product.getSeller() != null) {
            resp.setSellerId(product.getSeller().getId());
            resp.setSellerName(product.getSeller().getFullName());
        }

        ProductTranslation trans = resolveProductTranslation(product.getId(), lang, product.getOriginalLanguage());
        if (trans != null) {
            resp.setTitle(trans.getTitle());
            resp.setDescription(trans.getDescription());
        }

        if (product.getCategory() != null) {
            resp.setCategoryId(product.getCategory().getId());
            resp.setCategoryName(resolveCategoryName(product.getCategory().getId(), lang, product.getOriginalLanguage()));
        }

        if (product.getBrand() != null) {
            resp.setBrandId(product.getBrand().getId());
            resp.setBrandName(resolveBrandName(product.getBrand().getId(), lang, product.getOriginalLanguage()));
        }

        resp.setThumbnailUrl(resolveThumbnailUrl(product.getId()));
        resp.setDetailImages(resolveDetailImageUrls(product.getId()));
        resp.setAttributes(resolveAttributes(product, lang));

        return resp;
    }

    private ProductTranslation resolveProductTranslation(UUID productId, String lang, String fallbackLang) {
        return productTranslationRepository.findByProduct_IdAndLanguageCode(productId, lang)
                .or(() -> productTranslationRepository.findByProduct_IdAndLanguageCode(productId, fallbackLang))
                .orElse(null);
    }

    private String resolveCategoryName(UUID categoryId, String lang, String fallbackLang) {
        return categoryTranslationRepository.findByCategory_IdAndLanguageCode(categoryId, lang)
                .or(() -> categoryTranslationRepository.findByCategory_IdAndLanguageCode(categoryId, fallbackLang))
                .map(CategoryTranslation::getName)
                .orElse("");
    }

    private String resolveBrandName(UUID brandId, String lang, String fallbackLang) {
        return brandTranslationRepository.findByBrand_IdAndLanguageCode(brandId, lang)
                .or(() -> brandTranslationRepository.findByBrand_IdAndLanguageCode(brandId, fallbackLang))
                .map(BrandTranslation::getName)
                .orElse("");
    }

    private String resolveThumbnailUrl(UUID productId) {
        return productImageRepository.findByProduct_IdAndDisplayOrder(productId, 0)
                .map(img -> buildPublicUrl(img.getImageUrl()))
                .orElse(null);
    }

    private List<String> resolveDetailImageUrls(UUID productId) {
        return productImageRepository
                .findByProduct_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(productId, 0)
                .stream()
                .map(img -> buildPublicUrl(img.getImageUrl()))
                .toList();
    }

    private List<ProductResponse.AttributeInfo> resolveAttributes(Product product, String lang) {
        if (product.getAttributes() == null) {
            return List.of();
        }

        List<ProductResponse.AttributeInfo> result = new ArrayList<>();
        String fallbackLang = product.getOriginalLanguage();

        for (ProductAttribute pa : product.getAttributes()) {
            AttributeOption option = pa.getAttributeOption();
            if (option == null) continue;

            ProductResponse.AttributeInfo info = new ProductResponse.AttributeInfo();
            info.setAttributeOptionId(option.getId());

            String optionName = attributeOptionTranslationRepository
                    .findByAttributeOption_IdAndLanguageCode(option.getId(), lang)
                    .or(() -> attributeOptionTranslationRepository
                            .findByAttributeOption_IdAndLanguageCode(option.getId(), fallbackLang))
                    .map(AttributeOptionTranslation::getOptionName)
                    .orElse("");
            info.setAttributeOptionName(optionName);

            if (option.getAttributeType() != null) {
                info.setAttributeTypeId(option.getAttributeType().getId());
                String typeName = attributeTypeTranslationRepository
                        .findByAttributeType_IdAndLanguageCode(option.getAttributeType().getId(), lang)
                        .or(() -> attributeTypeTranslationRepository
                                .findByAttributeType_IdAndLanguageCode(option.getAttributeType().getId(), fallbackLang))
                        .map(AttributeTypeTranslation::getAttributeName)
                        .orElse("");
                info.setAttributeTypeName(typeName);
            }

            result.add(info);
        }

        return result;
    }

    private String buildPublicUrl(String objectKey) {
        return minioProperties.getEndpoint() + "/products/" + objectKey;
    }
}
