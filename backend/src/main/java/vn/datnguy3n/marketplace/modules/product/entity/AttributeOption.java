package vn.datnguy3n.marketplace.modules.product.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "attributeOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AttributeOptionTranslation> translations;
}
