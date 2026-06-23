package vn.datnguy3n.marketplace.modules.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "product_attributes")
@Getter
@Setter
public class ProductAttribute extends BaseEntity {

    @JsonIgnoreProperties({"translations", "images", "attributes", "seller", "category", "brand"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prat_product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prat_attribute_option_id", nullable = false)
    private AttributeOption attributeOption;
}
