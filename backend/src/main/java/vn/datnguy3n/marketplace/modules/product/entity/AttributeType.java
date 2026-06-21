package vn.datnguy3n.marketplace.modules.product.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "attribute_types")
@Getter
@Setter
public class AttributeType extends BaseEntity {

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AttributeTypeTranslation> translations;

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AttributeOption> options;
}
