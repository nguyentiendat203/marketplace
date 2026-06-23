package vn.datnguy3n.marketplace.modules.product.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_seller_id", nullable = false)
    @JsonIgnoreProperties({"email", "password", "role", "permissions", "hibernateLazyInitializer"})
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_category_id")
    @JsonIgnoreProperties({"parent", "translations","hibernateLazyInitializer"})
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_brand_id")
    @JsonIgnoreProperties({"translations","hibernateLazyInitializer"})
    private Brand brand;

    @Column(name = "prod_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "prod_condition_note", length = 255)
    private String conditionNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "prod_status", nullable = false, length = 20)
    private ProductStatus status = ProductStatus.PENDING;

    @Column(name = "prod_original_language", nullable = false, length = 5)
    private String originalLanguage = "vi";

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductTranslation> translations;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductAttribute> attributes;
}
