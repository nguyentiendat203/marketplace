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
        name = "brand_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"btrn_brand_id", "btrn_language_code"}),
        indexes = @Index(columnList = "btrn_brand_id, btrn_language_code")
)
@Getter
@Setter
public class BrandTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btrn_brand_id", nullable = false)
    private Brand brand;

    @Column(name = "btrn_language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "btrn_name", nullable = false, length = 150)
    private String name;
}
