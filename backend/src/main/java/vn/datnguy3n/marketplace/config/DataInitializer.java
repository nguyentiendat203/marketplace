package vn.datnguy3n.marketplace.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeOption;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeOptionTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeType;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeTypeTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Brand;
import vn.datnguy3n.marketplace.modules.product.entity.BrandTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Category;
import vn.datnguy3n.marketplace.modules.product.entity.CategoryTranslation;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.product.entity.ProductAttribute;
import vn.datnguy3n.marketplace.modules.product.entity.ProductStatus;
import vn.datnguy3n.marketplace.modules.product.entity.ProductTranslation;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeOptionRepository;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeOptionTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeTypeRepository;
import vn.datnguy3n.marketplace.modules.product.repository.attribute.AttributeTypeTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.brand.BrandRepository;
import vn.datnguy3n.marketplace.modules.product.repository.brand.BrandTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.category.CategoryRepository;
import vn.datnguy3n.marketplace.modules.product.repository.category.CategoryTranslationRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductAttributeRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductRepository;
import vn.datnguy3n.marketplace.modules.product.repository.product.ProductTranslationRepository;
import vn.datnguy3n.marketplace.modules.role.RoleRepository;
import vn.datnguy3n.marketplace.modules.role.entity.Role;
import vn.datnguy3n.marketplace.modules.user.UserRepository;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;
    private final BrandRepository brandRepository;
    private final BrandTranslationRepository brandTranslationRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeTranslationRepository attributeTypeTranslationRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final AttributeOptionTranslationRepository attributeOptionTranslationRepository;
    private final ProductRepository productRepository;
    private final ProductTranslationRepository productTranslationRepository;
    private final ProductAttributeRepository productAttributeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        if (productRepository.count() > 0) {
            log.info("[Seed] Product data already exists — skipping seed.");
            return;
        }
        User seller = seedSeller();
        seedProductData(seller);
    }

    // ─── Roles ───────────────────────────────────────────────────────────────

    private void seedRoles() {
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Default role for registered users");
            roleRepository.save(userRole);
        }
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("System administrator");
            roleRepository.save(adminRole);
        }
        log.info("[Seed] Roles OK");
    }

    // ─── Seed Seller ─────────────────────────────────────────────────────────

    private User seedSeller() {
        String email = "seed-seller@secondlife.dev";
        if (userRepository.existsByEmail(email)) {
            return userRepository.findByEmail(email).orElseThrow();
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        User seller = new User();
        seller.setEmail(email);
        seller.setPassword(passwordEncoder.encode("Admin@123"));
        seller.setFullName("SecondLife Seed Seller");
        seller.setRole(adminRole);
        seller.setActivated(true);
        seller.setKycVerified(true);
        User saved = userRepository.save(seller);
        log.info("[Seed] Seed seller created: {}", email);
        return saved;
    }

    // ─── Master seed entry point ──────────────────────────────────────────────

    private void seedProductData(User seller) {
        // Categories
        Category catElectronics  = saveCategory(null, (short) 1);
        Category catFashion       = saveCategory(null, (short) 1);
        Category catHome          = saveCategory(null, (short) 1);
        Category catSmartphones   = saveCategory(catElectronics, (short) 2);
        Category catClothing      = saveCategory(catFashion, (short) 2);

        addCategoryTranslations(catElectronics, "Điện tử",              "Electronics",  "電子機器");
        addCategoryTranslations(catFashion,     "Thời trang",           "Fashion",      "ファッション");
        addCategoryTranslations(catHome,        "Gia dụng",             "Home & Living","ホーム");
        addCategoryTranslations(catSmartphones, "Điện thoại thông minh","Smartphones",  "スマートフォン");
        addCategoryTranslations(catClothing,    "Quần áo",              "Clothing",     "衣類");
        log.info("[Seed] {} categories seeded", 5);

        // Brands
        Brand apple  = saveBrand();
        Brand sony   = saveBrand();
        Brand nike   = saveBrand();
        Brand uniqlo = saveBrand();

        addBrandTranslations(apple,  "Apple",  "Apple",  "アップル");
        addBrandTranslations(sony,   "Sony",   "Sony",   "ソニー");
        addBrandTranslations(nike,   "Nike",   "Nike",   "ナイキ");
        addBrandTranslations(uniqlo, "Uniqlo", "Uniqlo", "ユニクロ");
        log.info("[Seed] {} brands seeded", 4);

        // Attribute Types
        AttributeType atColor    = attributeTypeRepository.save(new AttributeType());
        AttributeType atSize     = attributeTypeRepository.save(new AttributeType());
        AttributeType atCapacity = attributeTypeRepository.save(new AttributeType());

        addAtypeTranslations(atColor,    "Màu sắc",   "Color",    "色");
        addAtypeTranslations(atSize,     "Kích cỡ",   "Size",     "サイズ");
        addAtypeTranslations(atCapacity, "Dung lượng","Capacity", "容量");
        log.info("[Seed] {} attribute types seeded", 3);

        // Attribute Options — Color
        AttributeOption optRed   = saveOption(atColor);
        AttributeOption optBlack = saveOption(atColor);
        AttributeOption optWhite = saveOption(atColor);
        addOptionTranslations(optRed,   "Đỏ",   "Red",   "赤");
        addOptionTranslations(optBlack, "Đen",  "Black", "黒");
        addOptionTranslations(optWhite, "Trắng","White", "白");

        // Attribute Options — Size
        AttributeOption optS = saveOption(atSize);
        AttributeOption optM = saveOption(atSize);
        AttributeOption optL = saveOption(atSize);
        addOptionTranslations(optS, "S", "S", "S");
        addOptionTranslations(optM, "M", "M", "M");
        addOptionTranslations(optL, "L", "L", "L");

        // Attribute Options — Capacity
        AttributeOption opt128 = saveOption(atCapacity);
        AttributeOption opt256 = saveOption(atCapacity);
        addOptionTranslations(opt128, "128GB", "128GB", "128GB");
        addOptionTranslations(opt256, "256GB", "256GB", "256GB");
        log.info("[Seed] {} attribute options seeded", 8);

        // Products (10)
        Product p1 = createProduct(seller, catSmartphones, apple,
                new BigDecimal("12500000"), "Sử dụng 6 tháng, còn 95%", "vi",
                "iPhone 14 Pro 256GB Đen cũ",
                "Máy nguyên zin, không trầy xước, pin 94%, đầy đủ phụ kiện.");
        linkAttrs(p1, optBlack, opt256);

        Product p2 = createProduct(seller, catElectronics, sony,
                new BigDecimal("5900000"), "Còn mới 90%, hộp đủ", "vi",
                "Tai nghe Sony WH-1000XM5 cũ",
                "Chống ồn đỉnh, pin tốt, không có vết trầy đáng kể.");
        linkAttrs(p2, optBlack);

        Product p3 = createProduct(seller, catClothing, nike,
                new BigDecimal("850000"), "Mới 98%, chỉ thử 1 lần", "vi",
                "Nike Air Force 1 trắng size M",
                "Giày chưa qua sử dụng thực tế, đế còn mới tinh.");
        linkAttrs(p3, optWhite, optM);

        Product p4 = createProduct(seller, catClothing, uniqlo,
                new BigDecimal("650000"), "Còn 85%, đã giặt máy", "vi",
                "Áo khoác Uniqlo Ultra Light Down đen",
                "Giữ nhiệt tốt, nhẹ, không mùi. Phù hợp thời tiết lạnh.");
        linkAttrs(p4, optBlack, optM);

        Product p5 = createProduct(seller, catElectronics, apple,
                new BigDecimal("27000000"), "Mới 92%, còn bảo hành 6 tháng", "vi",
                "MacBook Pro M2 13-inch 256GB trắng",
                "Chip M2, màn hình Retina, pin siêu bền. Không có vết trầy.");
        linkAttrs(p5, optWhite, opt256);

        Product p6 = createProduct(seller, catElectronics, sony,
                new BigDecimal("10500000"), "Mới 88%, đầy đủ phụ kiện", "vi",
                "Sony PlayStation 5 trắng",
                "Chạy mượt, ổ đĩa hoạt động tốt, kèm 1 tay cầm DualSense.");
        linkAttrs(p6, optWhite);

        Product p7 = createProduct(seller, catClothing, nike,
                new BigDecimal("280000"), "Còn 80%, đã giặt", "vi",
                "Áo thun Nike Dri-FIT đỏ size L",
                "Chất liệu thấm hút mồ hôi tốt, màu còn đẹp, không phai.");
        linkAttrs(p7, optRed, optL);

        Product p8 = createProduct(seller, catSmartphones, apple,
                new BigDecimal("8900000"), "Pin 89%, còn vỏ bảo vệ", "vi",
                "iPhone 13 Mini 128GB đỏ",
                "Máy zin, màn hình không bể, hoạt động ổn định.");
        linkAttrs(p8, optRed, opt128);

        Product p9 = createProduct(seller, catClothing, uniqlo,
                new BigDecimal("150000"), "Còn 75%, màu hơi nhạt", "vi",
                "Áo thun Uniqlo trắng size S",
                "Vải cotton mềm, phù hợp mặc nhà hoặc thể thao nhẹ.");
        linkAttrs(p9, optWhite, optS);

        Product p10 = createProduct(seller, catSmartphones, sony,
                new BigDecimal("7800000"), "Còn 90%, đầy đủ hộp", "vi",
                "Sony Xperia 5 IV 128GB đen",
                "Màn hình OLED 120Hz, âm thanh Dolby Atmos, pin 5000mAh.");
        linkAttrs(p10, optBlack, opt128);

        log.info("[Seed] 10 products seeded successfully.");
    }

    // ─── Category helpers ─────────────────────────────────────────────────────

    private Category saveCategory(Category parent, short level) {
        Category c = new Category();
        c.setParent(parent);
        c.setLevel(level);
        return categoryRepository.save(c);
    }

    private void addCategoryTranslations(Category c, String vi, String en, String ja) {
        categoryTranslationRepository.saveAll(List.of(
                buildCatTrans(c, "vi", vi),
                buildCatTrans(c, "en", en),
                buildCatTrans(c, "ja", ja)
        ));
    }

    private CategoryTranslation buildCatTrans(Category c, String lang, String name) {
        CategoryTranslation t = new CategoryTranslation();
        t.setCategory(c);
        t.setLanguageCode(lang);
        t.setName(name);
        return t;
    }

    // ─── Brand helpers ────────────────────────────────────────────────────────

    private Brand saveBrand() {
        return brandRepository.save(new Brand());
    }

    private void addBrandTranslations(Brand b, String vi, String en, String ja) {
        brandTranslationRepository.saveAll(List.of(
                buildBrandTrans(b, "vi", vi),
                buildBrandTrans(b, "en", en),
                buildBrandTrans(b, "ja", ja)
        ));
    }

    private BrandTranslation buildBrandTrans(Brand b, String lang, String name) {
        BrandTranslation t = new BrandTranslation();
        t.setBrand(b);
        t.setLanguageCode(lang);
        t.setName(name);
        return t;
    }

    // ─── AttributeType helpers ────────────────────────────────────────────────

    private void addAtypeTranslations(AttributeType at, String vi, String en, String ja) {
        attributeTypeTranslationRepository.saveAll(List.of(
                buildAtypeTrans(at, "vi", vi),
                buildAtypeTrans(at, "en", en),
                buildAtypeTrans(at, "ja", ja)
        ));
    }

    private AttributeTypeTranslation buildAtypeTrans(AttributeType at, String lang, String name) {
        AttributeTypeTranslation t = new AttributeTypeTranslation();
        t.setAttributeType(at);
        t.setLanguageCode(lang);
        t.setAttributeName(name);
        return t;
    }

    // ─── AttributeOption helpers ──────────────────────────────────────────────

    private AttributeOption saveOption(AttributeType type) {
        AttributeOption o = new AttributeOption();
        o.setAttributeType(type);
        return attributeOptionRepository.save(o);
    }

    private void addOptionTranslations(AttributeOption o, String vi, String en, String ja) {
        attributeOptionTranslationRepository.saveAll(List.of(
                buildOptionTrans(o, "vi", vi),
                buildOptionTrans(o, "en", en),
                buildOptionTrans(o, "ja", ja)
        ));
    }

    private AttributeOptionTranslation buildOptionTrans(AttributeOption o, String lang, String name) {
        AttributeOptionTranslation t = new AttributeOptionTranslation();
        t.setAttributeOption(o);
        t.setLanguageCode(lang);
        t.setOptionName(name);
        return t;
    }

    // ─── Product helpers ──────────────────────────────────────────────────────

    private Product createProduct(User seller, Category category, Brand brand,
            BigDecimal price, String conditionNote, String lang,
            String title, String description) {

        Product p = new Product();
        p.setSeller(seller);
        p.setCategory(category);
        p.setBrand(brand);
        p.setPrice(price);
        p.setConditionNote(conditionNote);
        p.setStatus(ProductStatus.AVAILABLE);
        p.setOriginalLanguage(lang);
        Product saved = productRepository.save(p);

        ProductTranslation t = new ProductTranslation();
        t.setProduct(saved);
        t.setLanguageCode(lang);
        t.setTitle(title);
        t.setDescription(description);
        productTranslationRepository.save(t);

        return saved;
    }

    private void linkAttrs(Product product, AttributeOption... options) {
        for (AttributeOption opt : options) {
            ProductAttribute pa = new ProductAttribute();
            pa.setProduct(product);
            pa.setAttributeOption(opt);
            productAttributeRepository.save(pa);
        }
    }
}
