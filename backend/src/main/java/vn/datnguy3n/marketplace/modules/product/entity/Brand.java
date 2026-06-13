package vn.datnguy3n.marketplace.modules.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(name = "brands")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Brand extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;
}
