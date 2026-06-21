package vn.datnguy3n.marketplace.modules.product.entity;

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
        name = "attribute_option_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aopt_trn_option_id", "aopt_trn_language_code"}),
        indexes = @Index(columnList = "aopt_trn_option_id, aopt_trn_language_code")
)
@Getter
@Setter
public class AttributeOptionTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aopt_trn_option_id", nullable = false)
    private AttributeOption attributeOption;

    @Column(name = "aopt_trn_language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "aopt_trn_option_name", nullable = false, length = 255)
    private String optionName;
}
