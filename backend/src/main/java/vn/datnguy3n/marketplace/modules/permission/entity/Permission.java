package vn.datnguy3n.marketplace.modules.permission.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity {

    @Column(name = "pms_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "pms_resource", nullable = false, length = 100)
    private String resource;

    @Column(name = "pms_action", nullable = false, length = 50)
    private String action;

    @Column(name = "pms_description", length = 255)
    private String description;
}
