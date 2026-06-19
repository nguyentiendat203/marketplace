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
@Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(name = "cat_name", nullable = false, length = 150)
    private String name;

    @Column(name = "cat_slug", nullable = false, unique = true, length = 150)
    private String slug;

    @Column(name = "cat_image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_parent_id")
    private Category parent;
}
