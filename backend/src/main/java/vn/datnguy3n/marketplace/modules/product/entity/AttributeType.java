package vn.datnguy3n.marketplace.modules.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(name = "attribute_types")
@Getter
@Setter
public class AttributeType extends BaseEntity {

    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName;
}
