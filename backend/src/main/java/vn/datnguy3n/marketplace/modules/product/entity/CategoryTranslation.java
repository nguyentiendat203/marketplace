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
        name = "category_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ctrn_category_id", "ctrn_language_code"}),
        indexes = @Index(columnList = "ctrn_category_id, ctrn_language_code")
)
@Getter
@Setter
public class CategoryTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctrn_category_id", nullable = false)
    private Category category;

    @Column(name = "ctrn_language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(name = "ctrn_name", nullable = false, length = 150)
    private String name;
}
