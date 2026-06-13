package vn.datnguy3n.marketplace.modules.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(name = "order_addresses")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class OrderAddress extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "prefecture", nullable = false, length = 50)
    private String prefecture;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @Column(name = "building", length = 255)
    private String building;

    @Column(name = "recipient_name", nullable = false, length = 150)
    private String recipientName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
}
