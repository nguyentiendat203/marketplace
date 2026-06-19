package vn.datnguy3n.marketplace.modules.product.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private User seller;

    @Column(name = "prod_title", nullable = false, length = 255)
    private String title;

    @Column(name = "prod_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "prod_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "prod_condition", nullable = false, length = 20)
    private String condition;

    @Column(name = "prod_status", nullable = false, length = 20)
    private ProductStatus status = ProductStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductAttribute> attributes;
}
