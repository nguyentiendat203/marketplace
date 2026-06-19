package vn.datnguy3n.marketplace.modules.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "order_addresses")
@Getter
@Setter
public class OrderAddress extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oadr_order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "oadr_postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "oadr_prefecture", nullable = false, length = 50)
    private String prefecture;

    @Column(name = "oadr_city", nullable = false, length = 100)
    private String city;

    @Column(name = "oadr_street", nullable = false, length = 255)
    private String street;

    @Column(name = "oadr_building", length = 255)
    private String building;

    @Column(name = "oadr_recipient_name", nullable = false, length = 150)
    private String recipientName;

    @Column(name = "oadr_phone", nullable = false, length = 20)
    private String phone;
}
