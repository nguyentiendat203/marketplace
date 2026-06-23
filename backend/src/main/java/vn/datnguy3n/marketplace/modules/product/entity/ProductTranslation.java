package vn.datnguy3n.marketplace.modules.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(
        name = "product_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ptrn_product_id", "ptrn_language_code"}),
        indexes = @Index(columnList = "ptrn_product_id, ptrn_language_code")
)
@Getter
@Setter
public class ProductTranslation extends BaseEntity {

    @JsonIgnoreProperties({"translations", "images", "attributes", "seller", "category", "brand"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ptrn_product_id", nullable = false)
    private Product product;

    @Column(name = "ptrn_language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "ptrn_title", nullable = false, length = 255)
    private String title;

    @Column(name = "ptrn_description", columnDefinition = "TEXT")
    private String description;
}
