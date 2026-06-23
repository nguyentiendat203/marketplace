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
        name = "attribute_type_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"atyp_trn_type_id", "atyp_trn_language_code"}),
        indexes = @Index(columnList = "atyp_trn_type_id, atyp_trn_language_code")
)
@Getter
@Setter
public class AttributeTypeTranslation extends BaseEntity {

    @JsonIgnoreProperties({"translations", "options"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atyp_trn_type_id", nullable = false)
    private AttributeType attributeType;

    @Column(name = "atyp_trn_language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "atyp_trn_attribute_name", nullable = false, length = 100)
    private String attributeName;
}
