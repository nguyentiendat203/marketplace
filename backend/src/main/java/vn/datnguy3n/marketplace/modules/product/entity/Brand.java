package vn.datnguy3n.marketplace.modules.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand extends BaseEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    private String logoUrl;
}
