package vn.datnguy3n.marketplace.modules.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "attribute_options")
@Getter
@Setter
public class AttributeOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aopt_attribute_type_id", nullable = false)
    private AttributeType attributeType;

    @Column(name = "aopt_attribute_option_name", nullable = false, length = 255)
    private String attributeOptionName;
}
