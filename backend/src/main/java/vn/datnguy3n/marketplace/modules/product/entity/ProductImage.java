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
@Table(name = "product_images")
@Getter
@Setter
public class ProductImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pimg_product_id", nullable = false)
    private Product product;

    @Column(name = "pimg_image_url", nullable = false)
    private String imageUrl;

    @Column(name = "pimg_display_order", nullable = false)
    private int displayOrder = 0;
}
