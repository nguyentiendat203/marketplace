package vn.datnguy3n.marketplace.modules.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(name = "product_attributes")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class ProductAttribute extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "attr_key", nullable = false, length = 100)
    private String attrKey;

    @Column(name = "attr_value", nullable = false, length = 255)
    private String attrValue;
}
